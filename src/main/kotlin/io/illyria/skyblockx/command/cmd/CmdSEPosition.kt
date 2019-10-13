package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.sedit.Position

class CmdSEPosition : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("pos")
        aliases.add("position")

        requiredArgs.add(Argument("positionIndex", 0, PosArgument()))

        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().asPlayer(true)
                .withPermission(Permission.SE_PASTESTRUCT).build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        val index = info.getArgAsInt(0) ?: return
        if (index < 0 || index > 2) {
            info.message(Message.commandSEPositionInvalidIndex)
            return
        }
        val positionChosen = if (index == 1) Position.POSITION1 else Position.POSITION2
        info.iPlayer!!.chosenPosition = positionChosen
        info.iPlayer!!.choosingPosition = true
        info.message(String.format(Message.commandSEPosition, if (positionChosen == Position.POSITION1) "1" else "2"))
    }


    override fun getHelpInfo(): String {
        return Message.commandSEPostionHelp
    }


}