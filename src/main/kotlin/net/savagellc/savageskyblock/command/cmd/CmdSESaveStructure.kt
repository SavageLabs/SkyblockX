package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.info
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.sedit.SkyblockEdit

class CmdSESaveStructure : SCommand() {

    init {
        aliases.add("save-struct")
        aliases.add("savestruct")

        requiredArgs.add("filename")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.SE_SAVESTUCT).build()
    }




    override fun perform(info: info) {
        if (info.iPlayer!!.pos1 == null || info.iPlayer!!.pos2 == null) {
            info.message(Message.commandSESaveStructurePositionsNotSet)
            return
        }
        SkyblockEdit().saveStructure(info.iPlayer!!.pos1!!, info.iPlayer!!.pos2!!, info.player!!, info.args[0])
    }


    override fun getHelpInfo(): String {
        return Message.commandSESaveStructureHelp
    }

}