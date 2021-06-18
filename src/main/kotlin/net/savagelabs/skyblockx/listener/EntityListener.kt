package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

object EntityListener : Listener {
	// This listener is primarily for mobs taking damage.
	@EventHandler(priority = EventPriority.HIGH)
	fun onEntityDamage(event: EntityDeathEvent) {
		// Return if the entity taking the damage is player, or if the damager is NOT a player.
		if (event.entity is Player || event.entity.killer == null || event.entity.killer !is Player || isNotInSkyblockWorld(
				event.entity.world
			)
		) {
			return
		}

		val iPlayer = (event.entity.killer as Player).getIPlayer()

		// Check if they have an island and a quest activated.
		if (iPlayer.hasIsland() && iPlayer.getIsland()!!.currentQuest != null) {
			val island = iPlayer.getIsland()!!
			// Assert non-null because the if check for this block will trigger.
			val currentQuest = island.currentQuest!!
			// Find the quest that the island has activated.
			val targetQuest =
				Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.KILL_MOBS && quest.id == currentQuest }
			if (targetQuest != null) {
				val isCorrectType = targetQuest.goalParameter.equals(event.entity.type.name, true)

				if (isCorrectType || targetQuest.goalParameter == "ANY") {
					island.addQuestData(targetQuest.id, 1)
					island.sendTeamQuestProgress(targetQuest, event.entity.killer as Player)
					// Check if quest is complete :D
					if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
						island.completeQuest(iPlayer, targetQuest)
					}
				}
			}
		}
	}

	@EventHandler
	fun onPlayerTakingDamage(event: EntityDamageByEntityEvent) {
		if (event.isCancelled || isNotInSkyblockWorld(event.entity.world)) {
			return
		}

		val type = event.entityType
		val damager = event.damager
		val isDamagerPlayer = damager.type == EntityType.PLAYER

		if (type == EntityType.ARMOR_STAND && isDamagerPlayer) {
			val iPlayer = (damager as Player).getIPlayer()

			// Check if they have an island or co-op island, if not, deny.
			if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
				iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
				event.isCancelled = true
				return
			}

			// Check if they can use the block on the island, or co-op island.
			if (!canUseBlockAtLocation(iPlayer, event.entity.location)) {
				iPlayer.message(Message.instance.listenerBlockPlacementDenied)
				event.isCancelled = true
				return
			}
		}

		// If they're not a player or if the entity is not in the skyblock world, we do not care.
		if (type != EntityType.PLAYER || isDamagerPlayer) {
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