package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.info
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.sedit.SkyblockEdit

class CmdSEPasteStructure : SCommand() {

    init {
        aliases.add("paste-struct")
        aliases.add("pastestruct")

        requiredArgs.add("filename")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.SE_REGIONS).build()
    }




    override fun perform(info: info) {
        SkyblockEdit().pasteIsland(info.args[0], info.player!!.location, info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandSESaveStructureHelp
    }

}