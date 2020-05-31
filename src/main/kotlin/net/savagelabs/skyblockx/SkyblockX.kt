package net.savagelabs.skyblockx

import com.cryptomorin.xseries.XMaterial
import io.papermc.lib.PaperLib
import kotlinx.coroutines.runBlocking
import net.prosavage.baseplugin.SavagePlugin
import net.prosavage.baseplugin.WorldBorderUtil
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.command.skyblock.SkyblockBaseCommand
import net.savagelabs.skyblockx.core.IslandTopInfo
import net.savagelabs.skyblockx.core.calculateAllIslands
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.registerAllPermissions
import net.savagelabs.skyblockx.hooks.PlacholderAPIIntegration
import net.savagelabs.skyblockx.listener.*
import net.savagelabs.skyblockx.persist.*
import net.savagelabs.skyblockx.persist.data.Items
import net.savagelabs.skyblockx.world.VoidWorldGenerator
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


class SkyblockX : SavagePlugin() {

    companion object {
        lateinit var skyblockX: SkyblockX
        lateinit var islandBaseCommand: IslandBaseCommand
        lateinit var skyblockBaseCommand: SkyblockBaseCommand
        lateinit var worldBorderUtil: WorldBorderUtil
        lateinit var generatorAlgorithm: Map<Int, Items<XMaterial>>
        var islandValues: IslandTopInfo? = null
    }

    @ExperimentalTime
    override fun onEnable() {
        val startupTime = measureTimeMillis {
            super.onEnable()
            printHeader()
            skyblockX = this
            registerAllPermissions(server.pluginManager)
            loadDataFiles()
            initWorldBorderUtility()
            setupCommands()
            setupAdminCommands()
            setupOreGeneratorAlgorithm()
            loadPlaceholderAPIHook()
            startIslandTopTask()
            startAutoSaveTask()
            loadMetrics()
            registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener(), EntityListener(), GlideListener())
            logInfo("Loaded ${Data.IPlayers.size} players.")
            logInfo("Loaded ${Data.islands.size} islands.")
            migrateData()
        }
        logInfo("Startup Finished (${startupTime}ms)")
        logInfo("If you need help with the plugin check out our wiki: https://github.com/SavageLabs/SkyblockX/wiki")
        logInfo("Join our discord: https://discordapp.com/invite/j8CW7x8")
        logInfo("This plugin is open source: https://github.com/SavageLabs/SkyblockX")
        logInfo("If you want to support my work consider the following:", ChatColor.GREEN)
        logInfo("\t- Patreon: https://patreon.com/ProSavage", ChatColor.GREEN)
        logInfo("\t- Leave a Star on Github: https://github.com/SavageLabs/SkyblockX", ChatColor.GREEN)
        logInfo("\t- Review the plugin on Spigot: https://www.spigotmc.org/resources/73135/", ChatColor.GREEN)
        loadWorlds()
        PaperLib.suggestPaper(this)
    }

    private fun migrateData() {
        Data.islands.forEach { id, island ->
            if (island.islandName == null) {
                logInfo("Island Names Update: Migrated ${island.ownerTag}'s Island Data.")
                island.islandName = island.ownerTag
            }
        }
    }

    private fun loadMetrics() {
        logInfo("Loading Metrics.")
        val metrics = Metrics(this, 6970)
        metrics.addCustomChart(Metrics.SingleLineChart("active_islands", Callable { Data.islands.size }))
    }

    private fun logInfo(message: String, color: ChatColor = ChatColor.YELLOW) {
        logger.info("$color$message")
    }

    @ExperimentalTime
    private fun startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            if (Config.islandSaveBroadcastMessage) Bukkit.broadcastMessage(color(Config.islandSaveBroadcastMessageStart))
            val time = measureTimedValue { Data.save() }
            if (Config.islandSaveBroadcastMessage) Bukkit.broadcastMessage(
                color(
                    String.format(
                        Config.islandSaveBroadcastMessageEnd,
                        time.duration
                    )
                )
            )
        }, 20L, Config.islandSaveTaskPeriodTicks.toLong())
    }

    @ExperimentalTime
    fun startIslandTopTask() {
        if (!Config.autoCalcIslands) return
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            runIslandCalc()
        }, 20L, Config.islandTopCalcPeriodTicks.toLong())
    }

    @ExperimentalTime
    fun runIslandCalc() {
        Bukkit.getScheduler().runTaskAsynchronously(this, Runnable {
            if (Config.islandTopBroadcastMessage) Bukkit.broadcastMessage(color(Config.islandTopBroadcastMessageStart))
            var time: TimedValue<Unit>? = null
            runBlocking {
                time = measureTimedValue {
                    calculateAllIslands()
                }
            }
            if (Config.islandTopBroadcastMessage)
                Bukkit.broadcastMessage(
                    color(
                        String.format(
                            Config.islandTopBroadcastMessageEnd,
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
        logInfo("Loading World: ${Config.skyblockWorldName}")
        WorldCreator(Config.skyblockWorldName)
            .generator(VoidWorldGenerator())
            .createWorld()
        logInfo("Loading World: ${Config.skyblockWorldNameNether}")
        WorldCreator(Config.skyblockWorldNameNether)
            .generator(VoidWorldGenerator())
            .environment(World.Environment.NETHER)
            .generateStructures(false)
            .createWorld()
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        return VoidWorldGenerator()
    }

    override fun onDisable() {
        super.onDisable()
        saveDataFiles()
    }

    fun loadDataFiles() {
        logInfo("Loading data files.")
        Config.load()
        Data.load()
        BlockValues.load()
        Quests.load()
        Message.load()
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
        Config.generatorProbability.forEach { (key, value) ->
            run {
                generatorStrategyMap[key] = Items(value)
            }
        }
        generatorAlgorithm = generatorStrategyMap
    }

    private fun saveDataFiles() {
        // Load and save to take in account changes :P
        Config.load()
        Config.save()

        // Load and save to take in account changes :P
        Quests.load()
        Quests.save()

        // Don't load this as people shouldn't be touching this file anyways.
        Data.save()

        BlockValues.load()
        BlockValues.save()

        // Load and save to take in account changes :P
        Message.load()
        Message.save()
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