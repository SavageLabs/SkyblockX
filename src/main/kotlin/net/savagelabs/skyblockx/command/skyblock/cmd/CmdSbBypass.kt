package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdSbBypass : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("bypass")

		commandRequirements =
			SCommandRequirementsBuilder().asPlayer(true).withPermission(Permission.ADMIN_BYPASS)
				.build()
	}


	override fun perform(info: SCommandInfo) {
		info.iPlayer!!.inBypass = !info.isBypassing()
		info.message(
			String.format(
				Message.instance.commandBypassToggle,
				if (info.iPlayer!!.inBypass) "in" else "out of"
			)
		)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandBypassHelp

	}

}