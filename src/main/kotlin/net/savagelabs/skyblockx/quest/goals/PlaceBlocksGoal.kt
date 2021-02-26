package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockPlaceEvent

/**
 * This [QuestGoal] implementation is for the default goal `PLACE_BLOCKS`.
 */
internal object PlaceBlocksGoal : QuestGoal<BlockPlaceEvent>("PLACE_BLOCKS", "Place Blocks") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun BlockPlaceEvent.work() {
        // make sure the event is NOT cancelled already
        if (isCancelled) {
            return
        }

        // necessity
        val islandPlayer = player.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) { DEFAULT_PARAMETERS_PREDICATE(it, block.type.name) } ?: return

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }
}