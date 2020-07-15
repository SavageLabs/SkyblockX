package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.data.getSLocation

class CmdSetGo : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("set-go")

		commandRequirements =
			SCommandRequirementsBuilder()
				.asIslandMember(true)
				.asPlayer(true)
				.asLeader(true)
				.withPermission(Permission.GO_SET)
				.build()
	}


	override fun perform(info: SCommandInfo) {
		if (getIslandFromLocation(info.player!!.location) != info.island) {
			info.message(Message.instance.commandGoSetYouMustBeOnYourIsland)
			return
		}

		info.island!!.islandGoPoint = getSLocation(info.player!!.location)
		info.message(Message.instance.commandGoSetSuccess)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandGoSetHelp
	}
}