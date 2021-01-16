package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.StringArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.SkyBlockEdit

class CmdSESaveStructure : Command<SCommandInfo, SCommandRequirements>() {
	init {
		aliases.add("save-struct")
		aliases.add("savestruct")

		requiredArgs.add(Argument("filename", 0, StringArgument()))

		commandRequirements = SCommandRequirementsBuilder()
			.asPlayer(true)
			.withPermission(Permission.SE_SAVESTUCT)
			.build()
	}

	override fun perform(info: SCommandInfo) {
		val islandPlayer = info.iPlayer ?: return
		val positionOne = islandPlayer.pos1
		val positionTwo = islandPlayer.pos2

		if (positionOne == null || positionTwo == null) {
			info.message(Message.instance.commandSESaveStructurePositionsNotSet)
			return
		}

		SkyBlockEdit.saveStructure(positionOne, positionTwo, info.player ?: return, info.args[0])
	}

	override fun getHelpInfo(): String = Message.instance.commandSESaveStructureHelp
}