package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.SkyblockEdit

class CmdSEPasteStructure : SCommand() {

    init {
        aliases.add("paste-struct")
        aliases.add("pastestruct")

        requiredArgs.add(Argument("filename", 0, StringArgument()))

        commandRequirements = CommandRequirementsBuilder().asPlayer(true)
            .withPermission(Permission.SE_PASTESTRUCT).build()
    }


    override fun perform(info: CommandInfo) {
        SkyblockEdit().pasteIsland(info.args[0], info.player!!.location, info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandSESaveStructureHelp
    }

}