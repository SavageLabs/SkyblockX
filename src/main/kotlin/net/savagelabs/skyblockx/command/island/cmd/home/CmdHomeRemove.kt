package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.HomeArgument
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdHomeRemove : Command<SCommandInfo, SCommandRequirements>() {


	init {
		aliases.add("remove")

		requiredArgs.add(Argument("home-name", 0, HomeArgument()))

		commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.HOME)
			.asIslandMember(true).build()
	}

	override fun perform(info: SCommandInfo) {
		if (!info.island!!.hasHome(info.args[0])) {
			info.message(Message.instance.commandHomeDoesNotExist)
			return
		}

		info.island!!.removeHome(info.args[0])
		info.message(String.format(Message.instance.commandHomeRemoveSuccess, info.args[0]))
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandHomeRemoveHelp
	}
}