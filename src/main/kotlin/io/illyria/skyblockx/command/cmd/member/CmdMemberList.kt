package io.illyria.skyblockx.command.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdMemberList : SCommand() {

    init {
        aliases.add("list")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {


    }

    override fun getHelpInfo(): String {
        return Message.commandMemberList
    }

}