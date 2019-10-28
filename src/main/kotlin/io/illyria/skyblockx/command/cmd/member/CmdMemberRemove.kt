package io.illyria.skyblockx.command.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdMemberRemove : SCommand() {

    init {
        aliases.add("remove")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()
    }



    override fun perform(info: CommandInfo) {

    }

    override fun getHelpInfo(): String {
        return Message.commandMemberRemove
    }
}