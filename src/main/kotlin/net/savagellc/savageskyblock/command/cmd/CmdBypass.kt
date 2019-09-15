package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdBypass : SCommand() {

    init {
        aliases.add("bypass")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.BYPASS).build()
    }


    override fun perform(commandInfo: CommandInfo) {
        commandInfo.iPlayer!!.inBypass = !commandInfo.isBypassing()
        commandInfo.message(String.format(Message.commandBypassToggle, if (commandInfo.iPlayer!!.inBypass) "in" else "out of"))
    }

    override fun getHelpInfo(): String {
        return Message.commandBypassHelp

    }

}