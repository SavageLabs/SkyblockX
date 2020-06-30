package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdMemberKick : SCommand() {

    init {
        aliases.add("kick")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }


    override fun perform(info: CommandInfo) {
        val island = info.island!!
        if (island.getIslandMembers().isEmpty()) {
            info.message(Message.instance.commandMemberKickLimit)
            return
        }
        val playerNameToRemove = info.args[0]
        if (playerNameToRemove == info.player!!.name) {
            info.message(Message.instance.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getIslandMembers().map { member -> member.name }.contains(playerNameToRemove)) {
            info.message(Message.instance.commandMemberKickNotFound)
            return
        }


        info.island!!.kickMember(playerNameToRemove)
        info.message(String.format(Message.instance.commandMemberKicked, playerNameToRemove))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberKickHelp
    }
}

class CmdKick : SCommand() {
    init {
        aliases.add("kick")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: CommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdMember }
            ?.subCommands?.find { command -> command is CmdMemberKick }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberKickHelp
    }

}