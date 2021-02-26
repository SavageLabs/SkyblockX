package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDeathEvent

/**
 * This [QuestGoal] implementation is for the default goal `KILL_MOBS`.
 */
internal object KillMobsGoal : QuestGoal<EntityDeathEvent>("KILL_MOBS", "Kill Mobs") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun EntityDeathEvent.work() {
        // make sure the entity is NOT a player
        if (entityType == EntityType.PLAYER) {
            return
        }

        // necessity
        val player = entity.killer ?: return
        val islandPlayer = player.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) {
            it.equals(entityType.name, ignoreCase = true) || it.equals("any", ignoreCase = true)
        } ?: return

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }
}