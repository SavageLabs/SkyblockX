package net.savagellc.savageskyblock

import net.prosavage.baseplugin.BasePlugin
import net.savagellc.savageskyblock.command.BaseCommand
import net.savagellc.savageskyblock.listener.*
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Data
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.world.VoidWorldGenerator
import org.bukkit.WorldCreator


class SavageSkyblock : BasePlugin() {

    override fun onEnable() {
        super.onEnable()
        Globals.savageSkyblock = this
        this.getCommand("savageskyblock")!!.setExecutor(BaseCommandTesting())
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



}