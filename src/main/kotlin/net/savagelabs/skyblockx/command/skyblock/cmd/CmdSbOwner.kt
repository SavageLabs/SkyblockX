package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.event.IslandTransferEvent
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdSbOwner : Command<SCommandInfo, SCommandRequirements>() {


	init {
		aliases.add("newowner")
		aliases.add("owner")

		requiredArgs.add(Argument("island-member", 0, PlayerArgument()))

		requiredArgs.add(Argument("new-owner", 1, PlayerArgument()))

		commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.ADMIN_NEWOWNER).build()
	}

	override fun perform(info: SCommandInfo) {
		val newOwner = info.getArgAsIPlayer(1, cannotReferenceYourSelf = false) ?: return
		val iPlayerByName = getIPlayerByName(info.args[0])
		val island = iPlayerByName?.getIsland()
		if (island == null || island.getLeader() != iPlayerByName) {
			info.message(Message.instance.commandSkyblockRemoveNotAnIslandOwner)
			return
		}

		Bukkit.getPluginManager().callEvent(IslandTransferEvent(island, island.getLeader()!!, newOwner))

		island.assignNewOwner(newOwner)
		island.getLeader()?.unassignIsland()


		info.message(Message.instance.commandSkyblockNewOwnerSuccess)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandSkyblockNewOwnerHelp
	}

}