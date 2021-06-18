package net.savagelabs.skyblockx

import com.cryptomorin.xseries.XMaterial
import fr.minuskube.inv.InventoryManager
import io.papermc.lib.PaperLib
import kotlinx.coroutines.runBlocking
import net.savagelabs.savagepluginx.SavagePluginX
import net.savagelabs.savagepluginx.persist.engine.ConfigManager
import net.savagelabs.savagepluginx.persist.engine.FlatDataManager
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.command.skyblock.SkyblockBaseCommand
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.hooks.PlacholderAPIIntegration
import net.savagelabs.skyblockx.hooks.VaultHook
import net.savagelabs.skyblockx.listener.*
import net.savagelabs.skyblockx.listener.ShopListener.handleHologram
import net.savagelabs.skyblockx.listener.hooks.WildStackerListener
import net.savagelabs.skyblockx.manager.UpgradeManager
import net.savagelabs.skyblockx.persist.*
import net.savagelabs.skyblockx.persist.data.Items
import net.savagelabs.skyblockx.placeholder.impl.ChestShopPlaceholder
import net.savagelabs.skyblockx.placeholder.impl.QuestPlaceholder
import net.savagelabs.skyblockx.registry.impl.HologramRegistry
import net.savagelabs.skyblockx.registry.impl.PlaceholderRegistry
import net.savagelabs.skyblockx.world.VoidWorldGenerator
import net.savagelabs.worldborder.WorldBorderUtil
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import java.util.concurrent.Callable
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

class SkyblockX : SavagePluginX() {
	companion object {
		lateinit var skyblockX: SkyblockX
		lateinit var worldBorderUtil: WorldBorderUtil
		lateinit var generatorAlgorithm: Map<Int, Items<XMaterial>>
		var islandValues: IslandTopInfo? = null
		lateinit var inventoryManager: InventoryManager
		internal var wasHologramActive: Boolean = false
		internal val isWildStackerPresent: Boolean by lazyOf(Bukkit.getPluginManager().getPlugin("WildStacker") != null)
	}

	override fun onLoad() {
		with (PlaceholderRegistry) {
			register(QuestPlaceholder::class.java, QuestPlaceholder())
			register(ChestShopPlaceholder::class.java, ChestShopPlaceholder())
		}
	}

	@ExperimentalTime
	override fun enable() {
		val startupTime = measureTimeMillis {
			printHeader()
			skyblockX = this
			loadDataFiles()
			registerAllPermissions(server.pluginManager)
			initWorldBorderUtility()
			setupCommands()
			setupAdminCommands()
			setupOreGeneratorAlgorithm()
			loadPlaceholderAPIHook()
			VaultHook.load()
			startIslandTopTask()
			startAutoSaveTask()
			loadMetrics()
			registerListeners(
				DataListener,
				SEditListener,
				BlockListener,
				PlayerListener,
				EntityListener,
				ShopListener,
				GlideListener(),
				WorldListener,
				if (isWildStackerPresent) WildStackerListener else null
			)
			UpgradeManager.defaults()
			startInventoryManager()
			logInfo("Loaded ${Data.instance.IPlayers.size} players.")
			logInfo("Loaded ${Data.instance.islands.size} islands.")
		}
		logInfo("Startup Finished (${startupTime}ms)")
		logInfo("If you need help with the plugin check out our wiki: https://wiki.savagelabs.net")
		logInfo("Join our discord: https://discordapp.com/invite/savagelabs")
		logInfo("If you want to support my work consider the following:", ChatColor.GREEN)
		logInfo("\t- Patreon: https://patreon.com/ProSavage", ChatColor.GREEN)
		logInfo("\t- Review the plugin on Spigot: https://www.spigotmc.org/resources/73135/", ChatColor.GREEN)
		loadWorlds()
		PaperLib.suggestPaper(this)
		// temporary fix
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, Runnable {
			Data.instance.islands.values.forEach {
				for (shop in it.chestShops.values) {
					val world = Bukkit.getWorld(when (shop.environment) {
						World.Environment.NORMAL -> Config.instance.skyblockWorldName
						World.Environment.NETHER -> Config.instance.skyblockWorldNameNether
						World.Environment.THE_END -> Config.instance.skyblockWorldNameEnd
						else -> continue
					})

					shop.location.world = world
					shop.chestLocation.world = world
					shop.handleHologram()
				}
			}
		}, 20L)
	}

	private fun startInventoryManager() {
		inventoryManager = InventoryManager(this)
		inventoryManager.init()
	}

	private fun loadMetrics() {
		logInfo("Loading Metrics.")
		val metrics = Metrics(this, 6970)
		metrics.addCustomChart(Metrics.SingleLineChart("active_islands", Callable { Data.instance.islands.size }))
	}

	private fun logInfo(message: String, color: ChatColor = ChatColor.YELLOW) {
		logger.info("$color$message")
	}

	@ExperimentalTime
	private fun startAutoSaveTask() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
			if (Config.instance.islandSaveBroadcastMessage) Bukkit.broadcastMessage(color(Config.instance.islandSaveBroadcastMessageStart))
			val time = measureTimedValue { saveDataFiles() }
			if (Config.instance.islandSaveBroadcastMessage) Bukkit.broadcastMessage(
				color(
					String.format(
						Config.instance.islandSaveBroadcastMessageEnd,
						time.duration
					)
				)
			)
		}, Config.instance.islandSaveTaskPeriodTicks.toLong(), Config.instance.islandSaveTaskPeriodTicks.toLong())
	}

	@ExperimentalTime
	fun startIslandTopTask() {
		if (!Config.instance.autoCalcIslands) return
		Bukkit.getScheduler().runTaskTimer(this, Runnable {
			runIslandCalc()
		}, 20L, Config.instance.islandTopCalcPeriodTicks.toLong())
	}

	@ExperimentalTime
	fun runIslandCalc() {
		Bukkit.getScheduler().runTaskAsynchronously(this, Runnable {
			if (Config.instance.islandTopBroadcastMessage) Bukkit.broadcastMessage(color(Config.instance.islandTopBroadcastMessageStart))
			var time: TimedValue<Unit>? = null
			runBlocking {
				time = measureTimedValue {
					calculateAllIslands()
				}
			}
			if (Config.instance.islandTopBroadcastMessage)
				Bukkit.broadcastMessage(
					color(
						String.format(
							Config.instance.islandTopBroadcastMessageEnd,
							islandValues?.map?.size,
							time?.duration ?: Duration.ZERO
						)
					)
				)
		})
	}

	private fun loadPlaceholderAPIHook() {
		if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
			logInfo(ChatColor.YELLOW.toString() + "Loading Placeholders...")
			PlacholderAPIIntegration().register()
		}
	}

	private fun loadWorlds() {
		logInfo("Loading World: ${Config.instance.skyblockWorldName}")
		WorldCreator(Config.instance.skyblockWorldName)
			.generator(VoidWorldGenerator())
			.createWorld()

		logInfo("Loading World: ${Config.instance.skyblockWorldNameNether}")
		WorldCreator(Config.instance.skyblockWorldNameNether)
			.generator(VoidWorldGenerator())
			.environment(World.Environment.NETHER)
			.generateStructures(false)
			.createWorld()

		logInfo("Loading World: ${Config.instance.skyblockWorldNameEnd}")
		WorldCreator(Config.instance.skyblockWorldNameEnd)
			.generator(VoidWorldGenerator())
			.environment(World.Environment.THE_END)
			.generateStructures(false)
			.createWorld()
	}

	override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
		return VoidWorldGenerator()
	}

	override fun disable() {
		saveDataFiles()
		if (wasHologramActive) HologramRegistry.unregisterAll()
		PlaceholderRegistry.unregisterAll()
		UpgradeManager.unregisterAll()
	}

	fun loadDataFiles() {
		logInfo("Loading data files.")
		Config.instance = ConfigManager.readOrSave(Config())
		runBlocking {
			Data.instance = if (Config.instance.useDatabase) MongoManager.load() else FlatDataManager.readOrSave(Data())
			if (Config.instance.useDatabase) {
				Bukkit.getOnlinePlayers().forEach { player -> player.getIPlayer().getIsland()?.syncIsland = true }
			}
		}
		BlockValues.instance = ConfigManager.readOrSave(BlockValues())
		Quests.instance = ConfigManager.readOrSave(Quests())
		Message.instance = ConfigManager.readOrSave(Message())
		GUIConfig.instance = ConfigManager.readOrSave(GUIConfig())
	}

	private fun initWorldBorderUtility() {
		logInfo("Starting WorldBorder Packet Util.")
		worldBorderUtil = WorldBorderUtil(this)
	}

	private fun setupCommands() {
		logInfo("Setting up Commands.")
		val baseCommand = IslandBaseCommand()
		baseCommand.prefix = "is"
		val command = this.getCommand("is")!!
		command.setExecutor(baseCommand)
		command.tabCompleter = baseCommand
		logInfo("${baseCommand.subCommands.size} commands registered.")
	}

	private fun setupAdminCommands() {
		logInfo("Setting up administrator Commands.")
		val baseCommand = SkyblockBaseCommand()
		baseCommand.prefix = "sbx"
		val command = this.getCommand("skyblockx")!!
		command.setExecutor(baseCommand)
		command.tabCompleter = baseCommand
		logInfo("${baseCommand.subCommands.size} admin commands registered.")
	}

	fun setupOreGeneratorAlgorithm() {
		val generatorStrategyMap = HashMap<Int, Items<XMaterial>>()
		Config.instance.generatorUpgrades.forEach { (key, value) ->
			run {
				generatorStrategyMap[key] = Items(value)
			}
		}
		generatorAlgorithm = generatorStrategyMap
	}

	private fun saveDataFiles() {
		// Load and save to take in account changes :P
//        Config.instance.load()
//        Config.instance.save()


		// Don't load this as people shouldn't be touching this file anyways.
		runBlocking {
			logInfo("Saving to ${if (Config.instance.useDatabase) "database" else "flatfile..."}")
			if (Config.instance.useDatabase) MongoManager.save(Data.instance) else FlatDataManager.save(Data.instance)
		}

	}

	private fun printHeader() {
		logInfo(
			"\n" +
					"   ▄████████    ▄█   ▄█▄ ▄██   ▄   ▀█████████▄   ▄█        ▄██████▄   ▄████████    ▄█   ▄█▄ ▀████    ▐████▀ \n" +
					"  ███    ███   ███ ▄███▀ ███   ██▄   ███    ███ ███       ███    ███ ███    ███   ███ ▄███▀   ███▌   ████▀  \n" +
					"  ███    █▀    ███▐██▀   ███▄▄▄███   ███    ███ ███       ███    ███ ███    █▀    ███▐██▀      ███  ▐███    \n" +
					"  ███         ▄█████▀    ▀▀▀▀▀▀███  ▄███▄▄▄██▀  ███       ███    ███ ███         ▄█████▀       ▀███▄███▀    \n" +
					"▀███████████ ▀▀█████▄    ▄██   ███ ▀▀███▀▀▀██▄  ███       ███    ███ ███        ▀▀█████▄       ████▀██▄     \n" +
					"         ███   ███▐██▄   ███   ███   ███    ██▄ ███       ███    ███ ███    █▄    ███▐██▄     ▐███  ▀███    \n" +
					"   ▄█    ███   ███ ▀███▄ ███   ███   ███    ███ ███▌    ▄ ███    ███ ███    ███   ███ ▀███▄  ▄███     ███▄  \n" +
					" ▄████████▀    ███   ▀█▀  ▀█████▀  ▄█████████▀  █████▄▄██  ▀██████▀  ████████▀    ███   ▀█▀ ████       ███▄ \n" +
					"               ▀                                ▀                                 ▀                         \n" +
					"By: ProSavage - https://github.com/ProSavage - https://savagelabs.net"
			, ChatColor.AQUA
		)
	}
}