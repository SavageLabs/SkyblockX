package io.illyria.skyblockx.command.skyblock.cmd

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdSbHelp: SCommand() {


    init {
        aliases.add("help")

        requiredArgs.add(Argument("page-number", 0, IntArgument()))

        commandRequirements = CommandRequirementsBuilder().build()
    }

    override fun perform(info: CommandInfo) {
        val page = info.getArgAsInt(0) ?: return
        Globals.islandBaseCommand.generateHelp(page, info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockHelpHelp
    }

}