package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdLeave : SCommand() {


    init {
        aliases.add("leave")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.LEAVE).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        if (info.iPlayer!!.isLeader()) {
            info.message(Message.instance.commandLeaveDeniedLeader)
            return
        }

        info.message(Message.instance.commandLeaveSuccess)
        info.island!!.messageAllOnlineIslandMembers(
            String.format(
                Message.instance.commandLeaveMemberLeftIsland,
                info.player!!.name
            )
        )
        info.island!!.kickMember(info.player!!.name)


    }

    override fun getHelpInfo(): String {
        return Message.instance.commandLeaveHelp
    }
}