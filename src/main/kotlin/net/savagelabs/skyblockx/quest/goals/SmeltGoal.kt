package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryAction.MOVE_TO_OTHER_INVENTORY
import org.bukkit.event.inventory.InventoryAction.PICKUP_ALL
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

/**
 * This [QuestGoal] implementation is for the default goal `SMELT`.
 */
internal object SmeltGoal : QuestGoal<InventoryClickEvent>("SMELT", "Smelt") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun InventoryClickEvent.work() {
        // make sure the event is NOT cancelled already
        if (isCancelled) {
            return
        }

        // necessity
        val player = whoClicked as Player

        // make sure this event matches our conditions
        if (!isNotInSkyblockWorld(player.world) || !this.runSmeltCheck()) {
            return
        }

        // necessity
        val islandPlayer = player.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) {
            DEFAULT_PARAMETERS_PREDICATE(it, currentItem?.type?.name ?: return@checkAndGet false)
        } ?: return

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }

    /**
     * Check if the click event matches this goal's conditions.
     *
     * @return [Boolean] whether or not all conditions are met.
     */
    private fun InventoryClickEvent.runSmeltCheck(): Boolean = with (clickedInventory) {
        this?.type != InventoryType.FURNACE
                || slot != 2
                || (action != MOVE_TO_OTHER_INVENTORY && action != PICKUP_ALL)
                || view.bottomInventory == this
    }
}