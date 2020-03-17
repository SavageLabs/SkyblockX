package io.illyria.skyblockx.listener

import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.core.isNotInSkyblockWorld
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.persist.Quests
import io.illyria.skyblockx.quest.QuestGoal
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerTeleportEvent

class EntityListener : Listener {


    // This listener is primarily for mobs taking damage.
    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDamage(event: EntityDeathEvent) {
        // Return if the entity taking the damage is player, or if the damager is NOT a player.
        if (event.entity is Player || event.entity.killer == null || event.entity.killer !is Player || isNotInSkyblockWorld(event.entity.world)) {
            return
        }

        val iPlayer = getIPlayer(event.entity.killer as Player)

        // Check if they have an island and a quest activated.
        if (iPlayer.hasIsland() && iPlayer.getIsland()!!.currentQuest != null) {
            val island = iPlayer.getIsland()!!
            // Assert non-null because the if check for this block will trigger.
            val currentQuest = island.currentQuest!!
            // Find the quest that the island has activated.
            val targetQuest =
                Quests.islandQuests.find { quest -> quest.type == QuestGoal.KILL_MOBS && quest.id == currentQuest }
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
        // If they're not a player or if the entity is not in the skyblock world, we do not care.
        if (event.entity !is Player || isNotInSkyblockWorld(event.entity.world)) {
            return
        }
        val iPlayer = getIPlayer(event.entity as Player)
        if (Config.disableMobDamageWhenIslandVisitor && !iPlayer.isOnOwnIsland()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (!Config.preventFallingDeaths
            || event.entity !is Player
            || isNotInSkyblockWorld(event.entity.world)
        ) {
            return
        }

        val player = event.entity as Player
        val iPlayer = getIPlayer(player)

        // Triggers when they fall into the void.
        if (event.cause == EntityDamageEvent.DamageCause.VOID && event.entity.location.y <= 0) {
            iPlayer.falling = true
            player.sendMessage(color(Message.messagePrefix + Message.listenerVoidDeathPrevented))
            if (iPlayer.hasIsland()) {
                player.teleport(iPlayer.getIsland()!!.getIslandCenter().add(0.0, 1.0, 0.0), PlayerTeleportEvent.TeleportCause.PLUGIN)
            } else {
                player.teleport(Bukkit.getWorld(Config.defaultWorld)!!.spawnLocation.add(0.0, 1.0, 0.0), PlayerTeleportEvent.TeleportCause.PLUGIN)
            }
            event.isCancelled = true
        }

        // Triggers when they fall and the VOID damage registers falling to cancel.
        if (event.cause == EntityDamageEvent.DamageCause.FALL && iPlayer.falling) {
            iPlayer.falling = false
            if (Config.useFallingDeathCommands && Config.fallingDeathPreventionCommands.isNotEmpty()) {
                for (command in Config.fallingDeathPreventionCommands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), color(command.replace("{player}", player.name)))
                }
            }
            event.isCancelled = true
        }

    }


}