package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.persist.Message

class CmdHelp : io.illyria.skyblockx.command.SCommand() {


    init {
        aliases.add("help")

        requiredArgs.add(Argument("page-number", 0, IntArgument()))

        commandRequirements = io.illyria.skyblockx.command.CommandRequirementsBuilder().build()
    }

    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        val page = info.getArgAsInt(0) ?: return
        io.illyria.skyblockx.Globals.baseCommand.generateHelp(page, info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandHelpHelp
    }

}