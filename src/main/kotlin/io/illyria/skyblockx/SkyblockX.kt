package io.illyria.skyblockx

import io.illyria.skyblockx.command.island.IslandBaseCommand
import io.illyria.skyblockx.command.skyblock.SkyblockBaseCommand
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.registerAllPermissions
import io.illyria.skyblockx.core.runIslandCalc
import io.illyria.skyblockx.hooks.PlacholderAPIIntegration
import io.illyria.skyblockx.listener.*
import io.illyria.skyblockx.persist.*
import io.illyria.skyblockx.persist.data.Items
import io.illyria.skyblockx.world.VoidWorldGenerator
import net.prosavage.baseplugin.SavagePlugin
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.XMaterial
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import kotlin.math.log
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


class SkyblockX : SavagePlugin() {

    @ExperimentalTime
    override fun onEnable() {
        val startupTime = measureTimeMillis {
            super.onEnable()
            printHeader()
            Globals.skyblockX = this
            registerAllPermissions(server.pluginManager)
            loadDataFiles()
            initWorldBorderUtility()
            setupCommands()
            setupAdminCommands()
            setupOreGeneratorAlgorithm()
            loadPlaceholderAPIHook()
            startIslandTopTask()
            startAutoSaveTask()
            registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener(), EntityListener())
            logInfo("Loaded ${Data.IPlayers.size} players.")
            logInfo("Loaded ${Data.islands.size} islands.")
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
    }

    private fun logInfo(message: String, color: ChatColor = ChatColor.YELLOW) {
        logger.info("$color$message")
    }

    @ExperimentalTime
    private fun startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            if (Config.islandSaveBroadcastMessage) Bukkit.broadcastMessage(color(Config.islandSaveBroadcastMessageStart))
            val time = measureTimedValue {  Data.save() }
            if (Config.islandSaveBroadcastMessage) Bukkit.broadcastMessage(color(String.format(Config.islandSaveBroadcastMessageEnd, time.duration)))
        }, 20L, Config.islandSaveTaskPeriodTicks.toLong())
    }

    @ExperimentalTime
    private fun startIslandTopTask() {
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            if (Config.islandTopBroadcastMessage) Bukkit.broadcastMessage(color(Config.islandTopBroadcastMessageStart))
            val time = measureTimedValue {
                Bukkit.getScheduler().runTask(this, Runnable { runIslandCalc() })
            }
            if (Config.islandTopBroadcastMessage) Bukkit.broadcastMessage(color(String.format(Config.islandTopBroadcastMessageEnd, Globals.islandValues?.map?.size, time.duration)))
        }, 20L, Config.islandTopCalcPeriodTicks.toLong())
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
        Globals.worldBorderUtil = WorldBorderUtil(this)
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
        Globals.generatorAlgorithm = generatorStrategyMap
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
        , ChatColor.AQUA)
    }


}