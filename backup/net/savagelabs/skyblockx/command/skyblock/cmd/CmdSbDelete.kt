package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message


class CmdSbDelete : Command<SCommandInfo, SCommandRequirements>() {


	init {
		aliases.add("delete")
		aliases.add("remove")

		requiredArgs.add(Argument("owner-tag", 0, PlayerArgument()))

		commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.ADMIN_DELETEISLAND).build()
	}

	override fun perform(info: SCommandInfo) {
		val iPlayerByName = getIPlayerByName(info.args[0])
		if (iPlayerByName?.getIsland() == null) {
			info.message(Message.instance.commandSkyblockRemoveNotAnIslandOwner)
			return
		}
		iPlayerByName.getIsland()?.delete()
		info.message(Message.instance.commandSkyblockRemoveSuccess)

	}

	override fun getHelpInfo(): String {
		return Message.instance.commandSkyblockRemoveHelp
	}

}