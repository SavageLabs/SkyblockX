package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdReset : SCommand() {

    init {
        aliases.add("reset")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: CommandInfo) {
        Globals.islandBaseCommand.subCommands.find { command -> command is CmdDelete }?.perform(info)
        Globals.islandBaseCommand.subCommands.find { command -> command is CmdCreate }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandResetHelp
    }

}