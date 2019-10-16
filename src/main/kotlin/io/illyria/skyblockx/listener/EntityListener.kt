package io.illyria.skyblockx.listener

import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.quest.QuestGoal
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent

class EntityListener : Listener {


    // This listener is primarily for mobs taking damage.
    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDamage(event: EntityDeathEvent) {
        // Return if the entity taking the damage is player, or if the damager is NOT a player.
        if (event.entity is Player || event.entity.killer == null || event.entity.killer !is Player) {
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
                Config.islandQuests.find { quest -> quest.type == QuestGoal.KILL_MOBS && quest.name == currentQuest }
            if (targetQuest != null) {
                val isCorrectType = targetQuest.goalParameter.equals(event.entity.type.name, true)

                if (isCorrectType || targetQuest.goalParameter == "ANY") {
                    island.addQuestData(targetQuest.name, 1)
                    // Check if quest is complete :D
                    if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.name))) {
                        island.completeQuest(iPlayer, targetQuest)
                    }
                }
            }


        }

    }


}