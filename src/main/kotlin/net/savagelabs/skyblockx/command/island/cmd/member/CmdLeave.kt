package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdLeave : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("leave")

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.LEAVE).asIslandMember(true).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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