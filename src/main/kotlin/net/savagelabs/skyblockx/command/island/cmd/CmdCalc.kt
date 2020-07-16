package net.savagelabs.skyblockx.command.island.cmd

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import kotlin.time.ExperimentalTime

class CmdCalc : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("calc")

		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.CALC).asIslandMember(true).build()
	}

	@ExperimentalTime
	override fun perform(info: SCommandInfo) {
		if (!info.island!!.canManualCalc()) {
			val cooldown = (System.currentTimeMillis() - info.island!!.lastManualCalc) / 1000
			info.message(
					Message.instance.commandCalcCooldown,
				((Config.instance.islandTopManualCalcCooldownMiliseconds / 1000) - cooldown).toString()
			)
			return
		}

		// Async launch.
		GlobalScope.launch {
			info.message(Message.instance.commandCalcStart)
			info.island!!.lastManualCalc = System.currentTimeMillis()
			val calcInfo = info.island!!.calcIsland()
			SkyblockX.islandValues?.map?.put(info.island!!.islandID, calcInfo)
			info.message(Message.instance.commandCalcMessage, calcInfo.timeDuration.toString())
		}

	}

	override fun getHelpInfo(): String {
		return Message.instance.commandCalcHelp
	}
}