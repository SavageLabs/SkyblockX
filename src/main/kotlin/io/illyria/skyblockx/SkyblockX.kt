package io.illyria.skyblockx

import io.illyria.skyblockx.command.BaseCommand
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
import org.bukkit.WorldCreator


class SkyblockX : SavagePlugin() {

    override fun onEnable() {
        super.onEnable()
        printHeader()
        Globals.skyblockX = this
        Globals.worldBorderUtil = WorldBorderUtil(this)
        this.getCommand("skyblockx")!!.setExecutor(BaseCommandTesting())
        val baseCommand = BaseCommand()
        val command = this.getCommand("is")!!
        command.setExecutor(baseCommand)
        command.tabCompleter = baseCommand
        logger.info("${baseCommand.subCommands.size} commands registered.")
        Config.load()
        setupOreGeneratorAlgorithm()
        Data.load()
        Message.load()
        registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener(), EntityListener())
        logger.info("Loaded ${Data.IPlayers.size} players")
        logger.info("Loaded ${Data.islands.size} islands")
    }

    private fun setupOreGeneratorAlgorithm() {
        val generatorStrategyMap = HashMap<Int, Items<XMaterial>>()
        Config.generatorProbability.forEach{ (key, value) -> run {generatorStrategyMap[key] = Items<XMaterial>(value)}}
        Globals.generatorAlgorithm = generatorStrategyMap
        WorldCreator(Config.skyblockWorldName).generator(VoidWorldGenerator()).createWorld()
    }

    override fun onDisable() {
        super.onDisable()
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