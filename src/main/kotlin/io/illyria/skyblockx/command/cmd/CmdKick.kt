package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.command.cmd.home.CmdHomeGo
import io.illyria.skyblockx.command.cmd.member.CmdMember
import io.illyria.skyblockx.command.cmd.member.CmdMemberKick
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdKick() : SCommand() {

    init {
        aliases.add("kick")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }


    override fun perform(info: CommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        Globals.baseCommand.subCommands.find { command -> command is CmdMember }
            ?.subCommands?.find { command -> command is CmdMemberKick }?.perform(info)
    }


    override fun getHelpInfo(): String {
        return Message.commandMemberKickHelp
    }

}