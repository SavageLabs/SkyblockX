package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Data
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent

/**
 * This [Listener] handles all World related events.
 */
object WorldListener : Listener {
    /**
     * Scan blocks placed upon world loading.
     */
    @EventHandler private fun WorldLoadEvent.onIslandLoad() {
        val worldName = world.name
        val config = Config.instance

        val isNether = worldName == config.skyblockWorldNameNether
        val isEnd = worldName == config.skyblockWorldNameEnd

        if (worldName != config.skyblockWorldName && !isNether && !isEnd) {
            return
        }

        for (island in Data.instance.islands.values) {
            if (isNether && !island.beenToNether || isEnd && !island.beenToEnd) {
                continue
            }
            island.scanPlacedBlocks(world)
        }
    }
}