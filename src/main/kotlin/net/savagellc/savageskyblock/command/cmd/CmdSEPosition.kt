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

        requiredArgs.add("positionIndex")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.SE_PASTESTRUCT).build()
    }


    override fun perform(commandInfo: CommandInfo) {
        val index = commandInfo.getArgAsInt(0) ?: return
        if (index < 0 || index > 2) {
            commandInfo.message(Message.commandSEPositionInvalidIndex)
            return
        }
        val positionChosen = if (index == 1) Position.POSITION1 else Position.POSITION2
        commandInfo.iPlayer!!.chosenPosition = positionChosen
        commandInfo.iPlayer!!.choosingPosition = true
        commandInfo.message(String.format(Message.commandSEPosition, if (positionChosen == Position.POSITION1) "1" else "2"))
    }


    override fun getHelpInfo(): String {
        return Message.commandSEPostionHelp
    }


}