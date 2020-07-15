package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.StringArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.SkyblockEdit

class CmdSESaveStructure : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("save-struct")
		aliases.add("savestruct")

		requiredArgs.add(Argument("filename", 0, StringArgument()))

		commandRequirements =
			SCommandRequirementsBuilder()
				.asPlayer(true)
				.withPermission(Permission.SE_SAVESTUCT)
				.build()
	}


	override fun perform(info: SCommandInfo) {
		if (info.iPlayer!!.pos1 == null || info.iPlayer!!.pos2 == null) {
			info.message(Message.instance.commandSESaveStructurePositionsNotSet)
			return
		}
		SkyblockEdit().saveStructure(info.iPlayer!!.pos1!!, info.iPlayer!!.pos2!!, info.player!!, info.args[0])
	}


	override fun getHelpInfo(): String {
		return Message.instance.commandSESaveStructureHelp
	}

}