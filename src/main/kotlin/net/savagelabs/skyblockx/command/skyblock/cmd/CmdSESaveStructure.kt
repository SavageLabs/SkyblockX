package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.SkyblockEdit

class CmdSESaveStructure : SCommand() {

    init {
        aliases.add("save-struct")
        aliases.add("savestruct")

        requiredArgs.add(Argument("filename", 0, StringArgument()))

        commandRequirements =
            CommandRequirementsBuilder().asPlayer(true)
                .withPermission(Permission.SE_SAVESTUCT).build()
    }


    override fun perform(info: CommandInfo) {
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