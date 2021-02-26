package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.canUseBlockAtLocation
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.metadata.FixedMetadataValue

object BlockListener : Listener {
	@EventHandler
	fun onBlockPlace(event: BlockPlaceEvent) {
		// FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
		if (isNotInSkyblockWorld(event.blockPlaced.world)) {
			return
		}

		// We're gonna need this.
		val iPlayer = event.player.getIPlayer()

		// Check if they dont have an island or a co-op island, if not, deny.
		if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
			iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
			event.isCancelled = true
			return
		}

		// Check using the general #canUseBlockAtLocation, will actually check co-op and own island.
		if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
			iPlayer.message(Message.instance.listenerBlockPlacementDenied)
			event.isCancelled = true
			return
		}

		// Anti-abuse for skyblock.
		event.block.setMetadata("skyblock-placed-by-player", FixedMetadataValue(SkyblockX.skyblockX, true))
	}

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		// FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
		if (isNotInSkyblockWorld(event.block.world)) {
			return
		}

		// Need this a lot.
		val iPlayer = event.player.getIPlayer()

		// Check if they have an island or co-op island, if not, deny.
		if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
			iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
			event.isCancelled = true
			return
		}

		// Check if they can use the block on the island, or co-op island.
		if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
			iPlayer.message(Message.instance.listenerBlockPlacementDenied)
			event.isCancelled = true
		}
	}
}