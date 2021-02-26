package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

object EntityListener : Listener {
	@EventHandler
	fun onPlayerTakingDamage(event: EntityDamageByEntityEvent) {
		// If they're not a player or if the entity is not in the skyblock world, we do not care.
		val type = event.entityType
		if (type != EntityType.PLAYER || event.damager.type == EntityType.PLAYER || isNotInSkyblockWorld(event.entity.world)) {
			return
		}

		val iPlayer = (event.entity as Player).getIPlayer()
		if (Config.instance.disableMobDamageWhenIslandVisitor && !iPlayer.isOnOwnIsland()) {
			event.isCancelled = true
		}
	}

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		if (Config.instance.skyblockDeathTeleport || isNotInSkyblockWorld(event.entity.world))
			return

		val iPlayer = event.entity.getIPlayer()
		iPlayer.teleportDeath = if (iPlayer.hasIsland()) {
			iPlayer.getIsland()!!.islandGoPoint!!.getLocation()
		} else {
			Bukkit.getWorld(Config.instance.defaultWorld)!!.spawnLocation
		}
	}

	@EventHandler
	fun onPlayerRespawn(event: PlayerRespawnEvent) {
		if (isNotInSkyblockWorld(event.player.world)) return
		val iPlayer = event.player.getIPlayer()
		if (iPlayer.teleportDeath != null) {
			Bukkit.getScheduler().runTask(SkyblockX.skyblockX, Runnable {
				teleportAsync(event.player, iPlayer.teleportDeath!!, Runnable {
					iPlayer.message(Message.instance.listenerDeathTeleport)
				})
			})

		}

	}

	@EventHandler
	fun onPlayerDamage(event: EntityDamageEvent) {
		if (!Config.instance.preventFallingDeaths
			|| event.entity !is Player
			|| isNotInSkyblockWorld(event.entity.world)
		) {
			return
		}

		val player = event.entity as Player
		val iPlayer = (player).getIPlayer()

		// Triggers when they fall into the void.
		if (event.cause == EntityDamageEvent.DamageCause.VOID && !iPlayer.falling && event.entity.location.y <= 0) {
			iPlayer.falling = true
			Bukkit.getScheduler().runTaskLater(SkyblockX.skyblockX, Runnable { iPlayer.falling = false }, 20L)
			val location: Location = if (iPlayer.hasIsland()) {
				iPlayer.getIsland()!!.islandGoPoint!!.getLocation()
			} else {
				Bukkit.getWorld(Config.instance.defaultWorld)!!.spawnLocation
			}
			teleportAsync(player, location, Runnable {
				player.sendMessage(color(Message.instance.messagePrefix + Message.instance.listenerVoidDeathPrevented))
			})
			event.isCancelled = true
		}

		// Triggers when they fall and the VOID damage registers falling to cancel.
		if (event.cause == EntityDamageEvent.DamageCause.FALL && iPlayer.falling) {
			iPlayer.falling = false
			if (Config.instance.useFallingDeathCommands && Config.instance.fallingDeathPreventionCommands.isNotEmpty()) {
				for (command in Config.instance.fallingDeathPreventionCommands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), color(command.replace("{player}", player.name)))
				}
			}
			event.isCancelled = true
		}

	}
}