package io.illyria.skyblockx.command.cmd.member

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdMemberPromote : SCommand() {

    init {
        aliases.add("promote")
        aliases.add("leader")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: CommandInfo) {
        val island = info.island!!
        if (island.getAllMembers().isEmpty()) {
            info.message(Message.commandMemberNoMembers)
            return
        }
        val playerNameToPromote = info.args[0]
        if (playerNameToPromote == info.player!!.name) {
            info.message(Message.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getAllMembers().contains(playerNameToPromote)) {
            info.message(Message.commandMemberPromoteNotFound)
            return
        }

        island.promoteNewLeader(playerNameToPromote)
        island.messageAllOnlineIslandMembers(String.format(Message.commandMemberPromotedSuccess, playerNameToPromote))
        Bukkit.getPlayer(playerNameToPromote)?.sendMessage(color(Message.commandMemberPromoteYouHaveBeenPromoted))
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberPromoteHelp
    }
}

class CmdPromote : SCommand() {
    init {
        aliases.add("promote")
        aliases.add("leader")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }
    override fun perform(info: CommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        Globals.baseCommand.subCommands.find { command -> command is CmdMember }
            ?.subCommands?.find { command -> command is CmdMemberPromote }?.perform(info)
    }
    override fun getHelpInfo(): String {
        return Message.commandMemberKickHelp
    }

}