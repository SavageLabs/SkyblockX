package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import kotlin.time.ExperimentalTime

class CmdSbCalc : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("calc")


		commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.ADMIN_CALC).build()
	}

	@ExperimentalTime
	override fun perform(info: SCommandInfo) {
		info.message(Message.instance.commandSkyblockCalcStart)
		SkyblockX.skyblockX.runIslandCalc()

	}

	override fun getHelpInfo(): String {
		return Message.instance.commandSkyblockCalcHelp
	}
}