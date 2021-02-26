package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerFishEvent

/**
 * This [QuestGoal] implementation is for the default goal `FISHING`.
 */
internal object FishingGoal : QuestGoal<PlayerFishEvent>("FISHING", "Fishing") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun PlayerFishEvent.work() {
        // make sure the event is NOT cancelled already
        if (isCancelled) {
            return
        }

        // make sure the state is correct, caught is an item and event is happening in a skyblock world
        if (state != PlayerFishEvent.State.CAUGHT_FISH || caught?.type != EntityType.DROPPED_ITEM || isNotInSkyblockWorld(hook.world)) {
            return
        }

        // necessity
        val islandPlayer = player.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) {
            DEFAULT_PARAMETERS_PREDICATE(it, (caught as? Item)?.itemStack?.type?.name ?: return@checkAndGet false)
        } ?: return

        // make sure the location meets the requirement
        if (!isLocationPartOf(island, hook.location)) {
            return
        }

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }
}