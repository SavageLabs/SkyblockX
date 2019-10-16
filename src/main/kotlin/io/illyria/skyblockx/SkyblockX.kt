package io.illyria.skyblockx

import io.illyria.skyblockx.command.BaseCommand
import io.illyria.skyblockx.listener.BlockListener
import io.illyria.skyblockx.listener.DataListener
import io.illyria.skyblockx.listener.PlayerListener
import io.illyria.skyblockx.listener.SEditListener
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Data
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.world.VoidWorldGenerator
import net.prosavage.baseplugin.BasePlugin
import org.bukkit.WorldCreator


class SkyblockX : BasePlugin() {

    override fun onEnable() {
        super.onEnable()
        printHeader()
        Globals.skyblockX = this
        this.getCommand("skyblockx")!!.setExecutor(BaseCommandTesting())
        val baseCommand = BaseCommand()
        val command = this.getCommand("is")!!
        command.setExecutor(baseCommand)
        command.tabCompleter = baseCommand
        logger.info("Command Registered")

        WorldCreator(Config.skyblockWorldName).generator(VoidWorldGenerator()).createWorld()
        Config.load()
        Data.load()
        Message.load()
        registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener())
        logger.info("Loaded ${Data.IPlayers.size} players")
        logger.info("Loaded ${Data.islands.size} islands")
    }

    override fun onDisable() {
        super.onDisable()
        // Load and save to take in account changes :P
        Config.load()
        Config.save()

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
                    "               ▀                                ▀                                 ▀                         \n"
        )
    }


}