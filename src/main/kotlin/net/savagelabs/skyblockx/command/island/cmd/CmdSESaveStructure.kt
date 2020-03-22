package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.sedit.SkyblockEdit

class CmdSESaveStructure : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("save-struct")
        aliases.add("savestruct")

        requiredArgs.add(Argument("filename", 0, StringArgument()))

        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asPlayer(true)
                .withPermission(Permission.SE_SAVESTUCT).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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