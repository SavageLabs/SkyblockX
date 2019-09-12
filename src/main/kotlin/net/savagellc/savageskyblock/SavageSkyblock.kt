package net.savagellc.savageskyblock

import net.prosavage.baseplugin.BasePlugin
import net.savagellc.savageskyblock.command.BaseCommand
import net.savagellc.savageskyblock.listener.DataListener
import net.savagellc.savageskyblock.listener.SEditListener
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
        this.getCommand("is")!!.setExecutor(BaseCommand())
        WorldCreator(Config.skyblockWorldName).generator(VoidWorldGenerator()).createWorld()
        Config.load()
        Data.load()
        Message.load()
        registerListeners(DataListener(), SEditListener())
    }

    override fun onDisable() {
        super.onDisable()
        Config.save()
        Data.save()
        Message.save()
    }



}