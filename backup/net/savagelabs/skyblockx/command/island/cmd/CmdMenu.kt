package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.menu.IslandMenu
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message

class CmdMenu : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("menu")


		commandRequirements = SCommandRequirementsBuilder()
			.withPermission(Permission.MENU)
			.asIslandMember(true)
			.build()
	}

	override fun perform(info: SCommandInfo) {
		if (Config.instance.openIslandMenuOnBaseCommand) buildMenu(IslandMenu()).open(info.player)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMenuHelp
	}

}