package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

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
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (Config.skyblockDeathTeleport && isNotInSkyblockWorld(event.entity.world))
            return

        val iPlayer = getIPlayer(event.entity)
        iPlayer.teleportDeath = if (iPlayer.hasIsland()) {
            iPlayer.getIsland()!!.islandGoPoint.getLocation()
        } else {
            Bukkit.getWorld(Config.defaultWorld)!!.spawnLocation
        }
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val iPlayer = getIPlayer(event.player)
        if (iPlayer.teleportDeath != null) {
            Bukkit.getScheduler().runTask(SkyblockX.skyblockX, Runnable {
                teleportAsync(event.player, iPlayer.teleportDeath!!, Runnable {
                    iPlayer.message(Message.listenerDeathTeleport)
                })
            })

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
        if (event.cause == EntityDamageEvent.DamageCause.VOID && !iPlayer.falling && event.entity.location.y <= 0) {
            iPlayer.falling = true
            Bukkit.getScheduler().runTaskLater(SkyblockX.skyblockX, Runnable { iPlayer.falling = false }, 20L)
            val location: Location = if (iPlayer.hasIsland()) {
                iPlayer.getIsland()!!.islandGoPoint.getLocation()
            } else {
                Bukkit.getWorld(Config.defaultWorld)!!.spawnLocation
            }
            teleportAsync(player, location, Runnable {
                player.sendMessage(color(Message.messagePrefix + Message.listenerVoidDeathPrevented))
            })
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