package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIslandById
import net.savagelabs.skyblockx.core.updateWorldBorder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class DataListener : Listener {


	@EventHandler
	fun onPlayerConnect(event: PlayerJoinEvent) {
		val iPlayer = getIPlayer(event.player)
		iPlayer.falling = false
		iPlayer.name = event.player.name
		// Update owner tag changes :)
		val island = getIslandById(iPlayer.islandID) ?: return
		island.lastLoginTime = Date()
		island.syncIsland = true
		// Delay to handle other plugins teleporting on login etc...
		updateWorldBorder(event.player, event.player.location, 10L)
	}

	@EventHandler
	fun onPlayerDisconnect(event: PlayerQuitEvent) {
		val iPlayer = getIPlayer(event.player)
		for (authorizedUser in iPlayer.coopedPlayersAuthorized) {
			val island = iPlayer.getIsland() ?: continue
			island.removeCoopPlayer(authorizedUser)
		}
	}


}