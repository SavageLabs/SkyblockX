package net.savagelabs.skyblockx.listener.hooks

import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent
import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.core.canUseBlockAtLocation
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.listener.BlockListener
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * This [Listener] handles all WildStacker events.
 */
object WildStackerListener : Listener {
    /**
     * This event handles the placement limitation on spawners.
     */
    @EventHandler
    private fun SpawnerPlaceEvent.onLimit() {
        // Make sure the event is not cancelled and it's placed on a Skyblock island beforehand.
        if (this.isCancelled || isNotInSkyblockWorld(player.world)) {
            return
        }

        // Make sure the player can actually interact with this island.
        val islandPlayer = player.getIPlayer()
        if (!islandPlayer.hasIsland() && !islandPlayer.hasCoopIsland() && !islandPlayer.inBypass) {
            this.isCancelled = true
            return
        }

        // Necessity.
        val location = spawner.location
        val island = getIslandFromLocation(location)

        // Make sure there is an island present AND the player can interact there.
        if (island == null || !canUseBlockAtLocation(islandPlayer, location)) {
            this.isCancelled = true
            return
        }

        // Work the placement.
        BlockListener.workPlacement(XMaterial.SPAWNER, island, islandPlayer, this)
    }
}