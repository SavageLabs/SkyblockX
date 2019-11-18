package io.illyria.skyblockx.command.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdMemberKick : SCommand() {

    init {
        aliases.add("kick")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }



    override fun perform(info: CommandInfo) {
        val island = info.island!!
        if (island.getAllMembers().isEmpty()) {
            info.message(Message.commandMemberKickLimit)
            return
        }
        val playerNameToRemove = info.args[0]
        if (playerNameToRemove == info.player!!.name) {
            info.message(Message.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getAllMembers().contains(playerNameToRemove)) {
            info.message(Message.commandMemberKickNotFound)
            return
        }


        info.island!!.kickMember(playerNameToRemove)
        info.message(String.format(Message.commandMemberKicked, playerNameToRemove))
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberKickHelp
    }
}