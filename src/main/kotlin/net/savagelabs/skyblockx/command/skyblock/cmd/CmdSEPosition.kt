package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.PosArgument
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.Position


class CmdSEPosition : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("pos")
		aliases.add("position")

		requiredArgs.add(Argument("positionIndex", 0, PosArgument()))

		commandRequirements =
			SCommandRequirementsBuilder()
				.asPlayer(true)
				.withPermission(Permission.SE_REGIONS)
				.build()
	}


	override fun perform(info: SCommandInfo) {
		val index = info.getArgAsInt(0) ?: return
		if (index < 0 || index > 2) {
			info.message(Message.instance.commandSEPositionInvalidIndex)
			return
		}
		val positionChosen = if (index == 1) Position.POSITION1 else Position.POSITION2
		info.iPlayer!!.chosenPosition = positionChosen
		info.iPlayer!!.choosingPosition = true
		info.message(
				Message.instance.commandSEPosition,
				if (positionChosen == Position.POSITION1) "1" else "2"
		)
	}


	override fun getHelpInfo(): String {
		return Message.instance.commandSEPostionHelp
	}


}