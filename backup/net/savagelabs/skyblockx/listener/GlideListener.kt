package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerTeleportEvent

/**
 * This class will fail to register on 1.8 servers.
 */
class GlideListener : Listener {


	@EventHandler
	fun onPlayerToggleGlide(event: EntityToggleGlideEvent) {
		// We want them to be allowed to glide, but just check where they end up, so they need to be on the ground.
		if (event.entity !is Player || event.isGliding || !event.entity.isOnGround) return

		val island = getIslandFromLocation(event.entity.location) ?: return
		val iPlayer = (event.entity as Player).getIPlayer()
		if (!island.allowVisitors && !island.hasCoopPlayer(iPlayer) && !island.getIslandMembers()
				.contains(iPlayer)
		) {
			if (iPlayer.hasIsland()) {
				event.entity.teleport(iPlayer.getIsland()!!.islandGoPoint!!.getLocation())
			} else {
				event.entity.teleport(
					Bukkit.getWorld(Config.instance.defaultWorld)!!.spawnLocation.add(0.0, 1.0, 0.0),
					PlayerTeleportEvent.TeleportCause.PLUGIN
				)
			}
		}

	}

}