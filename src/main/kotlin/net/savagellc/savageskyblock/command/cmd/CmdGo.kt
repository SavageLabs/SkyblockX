package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdGo : SCommand() {


    init {
        aliases.add("go")
        this.optionalArgs.add("player")


        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).asPlayer(true).withPermission(Permission.GO).build()
    }


    override fun perform(info: CommandInfo) {
        val island = info.iPlayer!!.getIsland()!!
        info.message(Message.commandGoTeleporting)
        info.player!!.teleport(island.getIslandSpawn())
    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
