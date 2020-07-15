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

class CmdSEPasteStructure : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("paste-struct")
		aliases.add("pastestruct")

		requiredArgs.add(Argument("filename", 0, StringArgument()))

		commandRequirements = SCommandRequirementsBuilder().asPlayer(true)
			.withPermission(Permission.SE_PASTESTRUCT).build()
	}


	override fun perform(info: SCommandInfo) {
		SkyblockEdit().pasteIsland(info.args[0], info.player!!.location, info.player!!)
	}


	override fun getHelpInfo(): String {
		return Message.instance.commandSESaveStructureHelp
	}

}