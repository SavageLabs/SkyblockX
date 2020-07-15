package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdChest : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("chest")
		aliases.add("inventory")
		aliases.add("inv")


		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.CHEST).asIslandMember(true).build()
	}


	override fun perform(info: SCommandInfo) {
		var inventory = info.island?.inventory
		info.player?.openInventory(inventory!!)
		info.message(Message.instance.commandChestOpening)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandChestHelp
	}
}