package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdMemberPromote : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("promote")
        aliases.add("leader")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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

class CmdPromote : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {
    init {
        aliases.add("promote")
        aliases.add("leader")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMember }
            ?.subCommands?.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberPromote }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberKickHelp
    }

}