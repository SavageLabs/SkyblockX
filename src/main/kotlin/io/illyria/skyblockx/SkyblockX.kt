package io.illyria.skyblockx

import io.illyria.skyblockx.command.BaseCommand
import io.illyria.skyblockx.core.registerAllPermissions
import io.illyria.skyblockx.hooks.PlacholderAPI
import io.illyria.skyblockx.listener.*
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Data
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.persist.Quests
import io.illyria.skyblockx.persist.data.Items
import io.illyria.skyblockx.world.VoidWorldGenerator
import net.prosavage.baseplugin.SavagePlugin
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.XMaterial
import org.bukkit.ChatColor
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import kotlin.system.measureTimeMillis


class SkyblockX : SavagePlugin() {

    override fun onEnable() {

        val startupTime = measureTimeMillis {
            super.onEnable()
            printHeader()
            Globals.skyblockX = this
            registerAllPermissions(server.pluginManager)
            loadWorld()
            loadDataFiles()
            initWorldBorderUiltity()
            setupCommands()
            setupOreGeneratorAlgorithm()
            loadPlaceholderAPIHook()
            registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener(), EntityListener())
            logger.info("Loaded ${Data.IPlayers.size} players")
            logger.info("Loaded ${Data.islands.size} islands")
        }
        logger.info("Startup Finished ($startupTime ms)")

    }

    private fun loadPlaceholderAPIHook() {
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            server.consoleSender.sendMessage(ChatColor.YELLOW.toString() + "Loading Placeholders...")
            PlacholderAPI().register()
        }
    }

    private fun loadWorld() {
        logger.info("Loading World ${Config.skyblockWorldName}")
        WorldCreator(Config.skyblockWorldName).generator(VoidWorldGenerator()).createWorld()
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
        Quests.load()
        Message.load()
    }

    private fun initWorldBorderUiltity() {
        logger.info("Starting WorldBorder Packet Util.")
        Globals.worldBorderUtil = WorldBorderUtil(this)
    }

    private fun setupCommands() {
        logger.info("Setting up Commands.")
        this.getCommand("skyblockx")!!.setExecutor(BaseCommandTesting())
        val baseCommand = BaseCommand()
        val command = this.getCommand("is")!!
        command.setExecutor(baseCommand)
        command.tabCompleter = baseCommand
        logger.info("${baseCommand.subCommands.size} commands registered.")
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