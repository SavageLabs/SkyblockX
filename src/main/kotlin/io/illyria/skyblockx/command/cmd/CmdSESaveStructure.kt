package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.sedit.SkyblockEdit

class CmdSESaveStructure : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("save-struct")
        aliases.add("savestruct")

        requiredArgs.add(Argument("filename", 0, StringArgument()))

        commandRequirements =
            CommandRequirementsBuilder().asPlayer(true)
                .withPermission(Permission.SE_SAVESTUCT).build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
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