package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.IntArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.command.skyblock.SkyblockBaseCommand
import net.savagelabs.skyblockx.persist.Message

class CmdSbHelp: Command<SCommandInfo, SCommandRequirements>() {


    init {
        aliases.add("help")

        requiredArgs.add(Argument("page-number", 0, IntArgument()))

        commandRequirements = SCommandRequirementsBuilder().build()
    }

    override fun perform(info: SCommandInfo) {
        val page = info.getArgAsInt(0) ?: return
        SkyblockBaseCommand.instance.generateHelp(page, info.player!!, info.args)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandSkyblockHelpHelp
    }

}