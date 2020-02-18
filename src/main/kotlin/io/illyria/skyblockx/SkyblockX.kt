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
import java.util.*
import kotlin.collections.HashMap
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
            loadWorld()
            loadDataFiles()
            initWorldBorderUtility()
            setupCommands()
            setupAdminCommands()
            setupOreGeneratorAlgorithm()
            loadPlaceholderAPIHook()
            startIslandTopTask()
            startAutoSaveTask()
            registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener(), EntityListener())
            logger.info("Loaded ${Data.IPlayers.size} players")
            logger.info("Loaded ${Data.islands.size} islands")
        }
        logger.info("Startup Finished (${startupTime}ms)")
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
                runIslandCalc()
            }
            if (Config.islandTopBroadcastMessage) Bukkit.broadcastMessage(color(String.format(Config.islandTopBroadcastMessageEnd, Globals.islandValues?.map?.size, time.duration)))
        }, 20L, Config.islandTopCalcPeriodTicks.toLong())
    }

    private fun loadPlaceholderAPIHook() {
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            logger.info(ChatColor.YELLOW.toString() + "Loading Placeholders...")
            PlacholderAPIIntegration().register()
        }
    }

    private fun loadWorld() {
        logger.info("Loading World ${Config.skyblockWorldName}")
        WorldCreator(Config.skyblockWorldName)
            .generator(VoidWorldGenerator())
            .createWorld()
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
        logger.info("Loading data files.")
        Config.load()
        Data.load()
        BlockValues.load()
        Quests.load()
        Message.load()
    }

    private fun initWorldBorderUtility() {
        logger.info("Starting WorldBorder Packet Util.")
        Globals.worldBorderUtil = WorldBorderUtil(this)
    }

    private fun setupCommands() {
        logger.info("Setting up Commands.")
        val baseCommand = IslandBaseCommand()
        baseCommand.prefix = "is"
        val command = this.getCommand("is")!!
        command.setExecutor(baseCommand)
        command.tabCompleter = baseCommand
        logger.info("${baseCommand.subCommands.size} commands registered.")
    }

    private fun setupAdminCommands() {
        logger.info("Setting up administrator Commands.")
        val baseCommand = SkyblockBaseCommand()
        baseCommand.prefix = "sbx"
        val command = this.getCommand("skyblockx")!!
        command.setExecutor(baseCommand)
        command.tabCompleter = baseCommand
        logger.info("${baseCommand.subCommands.size} admin commands registered.")
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
        logger.info(
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
                    "By: ProSavage - https://github.com/ProSavage - https://illyria.io"
        )
    }


}