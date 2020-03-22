package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.sedit.SkyblockEdit

class CmdSEPasteStructure : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("paste-struct")
        aliases.add("pastestruct")

        requiredArgs.add(Argument("filename", 0, StringArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asPlayer(true)
            .withPermission(Permission.SE_PASTESTRUCT).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        SkyblockEdit().pasteIsland(info.args[0], info.player!!.location, info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandSESaveStructureHelp
    }

}