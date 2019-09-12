package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.sedit.SkyblockEdit

class CmdSESaveStructure : SCommand() {

    init {
        aliases.add("save-struct")
        aliases.add("savestruct")

        requiredArgs.add("filename")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).build()
    }




    override fun perform(commandInfo: CommandInfo) {
        if (commandInfo.iPlayer!!.pos1 == null || commandInfo.iPlayer!!.pos2 == null) {
            commandInfo.message(Message.commandSESaveStructurePositionsNotSet)
            return
        }
        SkyblockEdit().saveStructure(commandInfo.iPlayer!!.pos1!!, commandInfo.iPlayer!!.pos2!!, commandInfo.player!!, commandInfo.args[0])
    }


    override fun getHelpInfo(): String {
        return Message.commandSESaveStructureHelp
    }

}