package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.persist.Message

class CmdSbHelp: SCommand() {


    init {
        aliases.add("help")

        requiredArgs.add(Argument("page-number", 0, IntArgument()))

        commandRequirements = CommandRequirementsBuilder().build()
    }

    override fun perform(info: CommandInfo) {
        val page = info.getArgAsInt(0) ?: return
        SkyblockX.skyblockBaseCommand.generateHelp(page, info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockHelpHelp
    }

}