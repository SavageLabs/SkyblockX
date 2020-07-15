package net.savagelabs.skyblockx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import org.bukkit.block.Biome
import org.bukkit.entity.Player

class BiomeArgument : ArgumentType {
	override fun getPossibleValues(player: Player): List<String> {
		return Biome.values().map { biome -> biome.name }.toList()
	}

}