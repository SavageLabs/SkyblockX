package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.event.IslandLeaveEvent
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdLeave : Command<SCommandInfo, SCommandRequirements>() {


	init {
		aliases.add("leave")

		commandRequirements = SCommandRequirementsBuilder()
			.withPermission(Permission.LEAVE)
			.asIslandMember(true)
			.build()
	}


	override fun perform(info: SCommandInfo) {
		if (info.iPlayer!!.isLeader()) {
			info.message(Message.instance.commandLeaveDeniedLeader)
			return
		}

		info.message(Message.instance.commandLeaveSuccess)
		info.island!!.messageAllOnlineIslandMembers(
			String.format(
				Message.instance.commandLeaveMemberLeftIsland,
				info.player!!.name
			)
		)
		info.island!!.kickMember(info.player!!.name)
		Bukkit.getPluginManager().callEvent(IslandLeaveEvent(info.island!!, getIPlayer(info.player!!)))

	}

	override fun getHelpInfo(): String {
		return Message.instance.commandLeaveHelp
	}
}