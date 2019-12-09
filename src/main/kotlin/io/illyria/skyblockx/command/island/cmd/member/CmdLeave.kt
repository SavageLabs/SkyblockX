package io.illyria.skyblockx.command.island.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdLeave : SCommand() {


    init {
        aliases.add("leave")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.LEAVE).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        if (info.iPlayer!!.isLeader()) {
            info.message(Message.commandLeaveDeniedLeader)
            return
        }

        info.message(Message.commandLeaveSuccess)
        info.island!!.messageAllOnlineIslandMembers(
            String.format(
                Message.commandLeaveMemberLeftIsland,
                info.player!!.name
            )
        )
        info.island!!.kickMember(info.player!!.name)


    }

    override fun getHelpInfo(): String {
        return Message.commandLeaveHelp
    }
}