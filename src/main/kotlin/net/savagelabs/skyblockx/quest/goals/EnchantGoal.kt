package net.savagelabs.skyblockx.quest.goals

import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.enchantment.EnchantItemEvent

/**
 * This [QuestGoal] implementation is for the default goal `ENCHANT`.
 */
internal object EnchantGoal : QuestGoal<EnchantItemEvent>("ENCHANT", "Enchant") {
    /**
     * Handle the event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    override fun EnchantItemEvent.work() {
        // make sure the event is NOT cancelled already
        if (isCancelled) {
            return
        }

        // make sure this event matches our conditions
        if (!isNotInSkyblockWorld(enchanter.world)) {
            return
        }

        // necessity
        val islandPlayer = enchanter.getIPlayer()
        val island = islandPlayer.getIsland() ?: return
        val quest = checkAndGet(islandPlayer, id) {
            val (name, levelString) = it.split("=")

            val enchant = Enchantment.getByName(name) ?: return@checkAndGet false
            val level   = levelString.toIntOrNull() ?: return@checkAndGet false

            enchantsToAdd[enchant] == level
        } ?: return

        // handle the work
        handleQuestData(island, quest, islandPlayer)
    }
}