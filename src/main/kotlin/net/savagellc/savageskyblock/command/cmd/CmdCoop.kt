package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdCoop : SCommand() {

    init {
        aliases.add("co-op")
        aliases.add("coop")

        requiredArgs.add("player")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).asIslandMember(true).withPermission(Permission.COOP).build()
    }


    override fun perform(commandInfo: CommandInfo) {
        val target = commandInfo.getArgAsIPlayer(0) ?: return
        commandInfo.iPlayer!!.coopPlayer(target)
        target.message(String.format(Message.commandCoopMessageRecipient, commandInfo.player!!.name))
        commandInfo.iPlayer!!.message(String.format(Message.commandCoopInvokerSuccess, target.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.commandCoopHelp
    }


}