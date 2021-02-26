package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.CraftItemEvent

/**
 * This [QuestGoal] implementation is for the default goal `CRAFT`.
 */
internal object CraftGoal : QuestGoal<CraftItemEvent>("CRAFT", "Craft") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun CraftItemEvent.work() {
        // make sure the event is NOT cancelled already
        if (isCancelled) {
            return
        }

        // get the player whom clicked
        val player = whoClicked as Player

        // make sure the event is happening in a skyblock world
        if (isNotInSkyblockWorld(player.world)) {
            return
        }

        // necessity
        val islandPlayer = player.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) {
            DEFAULT_PARAMETERS_PREDICATE(it, recipe.result.type.name)
        } ?: return

        // make sure the location meets the requirement
        if (!isLocationPartOf(island, player.location)) {
            return
        }

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }
}