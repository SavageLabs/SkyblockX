package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.event.IslandKickEvent
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdSbKick : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("kick")

		requiredArgs.add(Argument("player-to-kick", 0, PlayerArgument()))

		commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.ADMIN_KICKFROMISLAND).build()
	}


	override fun perform(info: SCommandInfo) {
		val iPlayerByName = getIPlayerByName(info.args[0])
		if (iPlayerByName?.getIsland() == null) {
			info.message(Message.instance.genericPlayerNotAnIslandMember)
			return
		}

		// They're not the owner so we process removing the member.
		val island = iPlayerByName.getIsland()!!
		if (island.getLeader() != iPlayerByName) {
			if (!island.getIslandMembers(false).contains(iPlayerByName)) {
				info.message(Message.instance.commandMemberKickNotFound)
				return
			}

			island.kickMember(iPlayerByName.name)
			Bukkit.getPluginManager().callEvent(IslandKickEvent(island, info.player!!.getIPlayer(), iPlayerByName))
		} else {
			// Theyre an island owner if we're here.
			iPlayerByName.unassignIsland()
			if (island.members.isEmpty()) {
				island.delete()
				info.message(Message.instance.commandSkyblockKickIslandDeleted)
				return
			}
			val firstMember = island.getIslandMembers(false).first()

			// just in case
			firstMember.assignIsland(island)
			island.leaderUUID = firstMember.uuid
			info.message(Message.instance.commandSkyblockKickMemberKickedOwner, firstMember.name)
		}
		info.message(Message.instance.commandSkyblockKickMemberKicked, iPlayerByName.name)


	}

	override fun getHelpInfo(): String {
		return Message.instance.commandSkyblockKickHelp
	}
}