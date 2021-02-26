package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent

/**
 * This [QuestGoal] implementation is for the default goal `BREAK_BLOCKS`.
 */
internal object BreakBlocksGoal : QuestGoal<BlockBreakEvent>("BREAK_BLOCKS", "Break Blocks") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun BlockBreakEvent.work() {
        // make sure the event is NOT cancelled already
        if (isCancelled) {
            return
        }

        // necessity
        val islandPlayer = player.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) {
            DEFAULT_PARAMETERS_PREDICATE(it, block.type.name)
        } ?: return

        // make sure the block is NOT placed by a player
        if (block.hasMetadata("skyblock-placed-by-player")) {
            return
        }

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }
}