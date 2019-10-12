package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.Globals
import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.persist.Message

class CmdHelp : SCommand() {


    init {
        aliases.add("help")

        requiredArgs.add(Argument("page-number", 0, IntArgument()))

        commandRequirements = CommandRequirementsBuilder().build()
    }

    override fun perform(info: CommandInfo) {
        val page = info.getArgAsInt(0) ?: return
        Globals.baseCommand.generateHelp(page, info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandHelpHelp
    }

}