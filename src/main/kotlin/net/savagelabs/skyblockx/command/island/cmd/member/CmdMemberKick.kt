package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdMemberKick : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("kick")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val island = info.island!!
        if (island.getIslandMembers().isEmpty()) {
            info.message(Message.commandMemberKickLimit)
            return
        }
        val playerNameToRemove = info.args[0]
        if (playerNameToRemove == info.player!!.name) {
            info.message(Message.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getIslandMembers().map { member -> member.name }.contains(playerNameToRemove)) {
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

class CmdKick : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {
    init {
        aliases.add("kick")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMember }
            ?.subCommands?.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberKick }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberKickHelp
    }

}