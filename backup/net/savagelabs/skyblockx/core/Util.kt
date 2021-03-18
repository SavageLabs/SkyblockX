package net.savagelabs.skyblockx.core

import io.papermc.lib.PaperLib
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.worldborder.WorldBorderUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

fun color(message: String): String {
	return ChatColor.translateAlternateColorCodes('&', message)
}

fun buildBar(element: String, barLength: Int = Config.instance.barLength): String {
	val bar = StringBuilder()
	repeat(barLength) {
		bar.append(element)
	}
	return color(bar.toString())
}

fun broadcastDebug(message: String) {
	Bukkit.broadcastMessage(color("Debug -> $message"))
}

fun isNotInSkyblockWorld(world: World): Boolean {
	return world.name != Config.instance.skyblockWorldName && world.name != Config.instance.skyblockWorldNameNether && world.name != Config.instance.skyblockWorldNameEnd
}

fun teleportAsync(player: Player?, location: Location, runnable: Runnable) {
	if (player == null) return
	val locationtoTP = location.add(0.0, 1.0, 0.0)
	PaperLib.teleportAsync(player, locationtoTP, PlayerTeleportEvent.TeleportCause.PLUGIN).thenAccept {
		if (it) runnable.run() else {
			SkyblockX.skyblockX.logger.severe("Something went wrong when trying to teleport ${player.name} async - running sync.")
			player.teleport(locationtoTP, PlayerTeleportEvent.TeleportCause.PLUGIN)
		}
	}
}


inline fun <reified T : Enum<*>> enumValueOrNull(name: String): T? =
	T::class.java.enumConstants.firstOrNull { it.name == name }

fun updateWorldBorder(player: Player, location: Location, delay: Long) {
	if (isNotInSkyblockWorld(location.world!!)) {
		val worldBorder = location.world?.worldBorder
		SkyblockX.worldBorderUtil.sendWorldBorder(
			player,
			WorldBorderUtil.Color.NONE,
			worldBorder!!.size,
			worldBorder.center
		)
	} else {
		Bukkit.getScheduler()
			.runTaskLater(
				SkyblockX.skyblockX,
				Runnable {
					val islandFromLocation = getIslandFromLocation(location)
					val iPlayer = player.getIPlayer()
					if (islandFromLocation != null) {
						val wbCenter = islandFromLocation.getIslandCenter()
						wbCenter.world = player.world
						WorldBorderUtil(SkyblockX.skyblockX).sendWorldBorder(
							player,
							iPlayer.borderColor,
							islandFromLocation.islandSize.toDouble(),
							wbCenter
						)
					}
				},
				delay
			)
	}
}

fun isPlaceholderAPIPresent(): Boolean {
	return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
}

