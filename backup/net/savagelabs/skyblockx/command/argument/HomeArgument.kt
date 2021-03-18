package net.savagelabs.skyblockx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import net.savagelabs.skyblockx.core.getIPlayer
import org.bukkit.entity.Player

class HomeArgument : ArgumentType {
	override fun getPossibleValues(player: Player): List<String> {
		val iPlayer = player.getIPlayer()
		return if (iPlayer.hasIsland()) iPlayer.getIsland()!!.getAllHomes().keys.toList() else emptyList()
	}

}