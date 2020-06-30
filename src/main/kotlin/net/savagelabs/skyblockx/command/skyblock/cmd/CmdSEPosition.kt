package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.Position
import net.savagelabs.skyblockx.command.CommandInfo


class CmdSEPosition : SCommand() {

    init {
        aliases.add("pos")
        aliases.add("position")

        requiredArgs.add(Argument("positionIndex", 0, PosArgument()))

        commandRequirements =
            CommandRequirementsBuilder().asPlayer(true)
                .withPermission(Permission.SE_REGIONS).build()
    }


    override fun perform(info: CommandInfo) {
        val index = info.getArgAsInt(0) ?: return
        if (index < 0 || index > 2) {
            info.message(Message.instance.commandSEPositionInvalidIndex)
            return
        }
        val positionChosen = if (index == 1) Position.POSITION1 else Position.POSITION2
        info.iPlayer!!.chosenPosition = positionChosen
        info.iPlayer!!.choosingPosition = true
        info.message(String.format(Message.instance.commandSEPosition, if (positionChosen == Position.POSITION1) "1" else "2"))
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandSEPostionHelp
    }


}