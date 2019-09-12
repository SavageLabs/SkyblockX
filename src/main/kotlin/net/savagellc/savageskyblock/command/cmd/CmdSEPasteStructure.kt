package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.sedit.SkyblockEdit

class CmdSEPasteStructure : SCommand() {

    init {
        aliases.add("paste-struct")
        aliases.add("pastestruct")

        requiredArgs.add("filename")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).build()
    }




    override fun perform(commandInfo: CommandInfo) {
        SkyblockEdit().pasteStructure(commandInfo.args[0], commandInfo.player!!.location, commandInfo.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandSESaveStructureHelp
    }

}