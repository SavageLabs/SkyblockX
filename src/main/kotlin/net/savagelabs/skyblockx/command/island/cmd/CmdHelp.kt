package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdHelp : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("help")

        requiredArgs.add(Argument("page-number", 0, IntArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val page = info.getArgAsInt(0) ?: return
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand.generateHelp(page, info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandHelpHelp
    }

}