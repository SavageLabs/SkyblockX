package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.sedit.Position

class CmdSEPosition : SCommand() {

    init {
        aliases.add("pos")
        aliases.add("position")

        requiredArgs.add(Argument("positionIndex", 0, PosArgument()))

        commandRequirements =
            CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.SE_PASTESTRUCT).build()
    }


    override fun perform(info: CommandInfo) {
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