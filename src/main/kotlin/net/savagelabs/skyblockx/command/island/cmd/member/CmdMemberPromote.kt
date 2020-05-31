package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message
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
        if (island.getIslandMembers().isEmpty()) {
            info.message(Message.commandMemberNoMembers)
            return
        }


        val playerNameToPromote = info.args[0]
        if (playerNameToPromote == info.player!!.name) {
            info.message(Message.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getIslandMembers().contains(getIPlayerByName(playerNameToPromote))) {
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
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdMember }
            ?.subCommands?.find { command -> command is CmdMemberPromote }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberKickHelp
    }

}