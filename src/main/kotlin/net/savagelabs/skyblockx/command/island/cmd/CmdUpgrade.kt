package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.menu.UpgradeMenu
import net.savagelabs.skyblockx.persist.Message

class CmdUpgrade : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("upgrade")


		commandRequirements = SCommandRequirementsBuilder().asIslandMember(true).build()
	}

	override fun perform(info: SCommandInfo) {
		info.message(Message.instance.commandUpgradesOpening)
		buildMenu(UpgradeMenu(info.island!!)).open(info.player!!)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandUpgradesHelp
	}

}
