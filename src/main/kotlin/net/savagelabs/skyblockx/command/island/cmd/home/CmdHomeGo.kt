package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.HomeArgument
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Message

class CmdHomeGo : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("go")

		requiredArgs.add(Argument("home-name", 0, HomeArgument()))

		commandRequirements = SCommandRequirementsBuilder().asIslandMember(true).build()
	}

	override fun perform(info: SCommandInfo) {
		val home = info.args[0]
		if (!info.island!!.hasHome(home)) {
			info.message(Message.instance.commandHomeDoesNotExist)
			return
		}

		teleportAsync(
			info.player!!,
			info.island!!.getHome(home)!!.getLocation(),
			Runnable { info.message(Message.instance.commandHomeGoSuccess, home) })
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandHomeGoHelp
	}


}