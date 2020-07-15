package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.HomeArgument
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdHome : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("home")

		optionalArgs.add(Argument("home-name", 0, HomeArgument()))

		commandRequirements =
			SCommandRequirementsBuilder()
				.withPermission(Permission.HOME)
				.asPlayer(true)
				.asIslandMember(true)
				.build()

		subCommands.add(CmdHomeList())
		subCommands.add(CmdHomeSet())
		subCommands.add(CmdHomeRemove())
		subCommands.add(CmdHomeGo())
	}


	override fun perform(info: SCommandInfo) {
		// No Args / Invalid args specified.
		if (info.args.size != 1) {
			generateHelp(1, info.player!!, info.args)
			return
		}

		// Execute command go just to make a shorthand version for /is home go <home>.
		this.subCommands.find { command -> command is CmdHomeGo }?.perform(info)


	}

	override fun getHelpInfo(): String {
		return Message.instance.commandHomeHelp
	}
}

