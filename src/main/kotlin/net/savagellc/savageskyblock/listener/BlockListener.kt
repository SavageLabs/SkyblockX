package net.savagellc.savageskyblock.listener

import net.savagellc.savageskyblock.core.canUseBlockAtLocation
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent


class BlockListener : Listener {


    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val iPlayer = getIPlayer(event.player)
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland()) {
            iPlayer.message(Message.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }

        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            iPlayer.message(Message.listenerBlockPlacementDenied)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockBreakEvent) {
        val iPlayer = getIPlayer(event.player)
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland()) {
            iPlayer.message(Message.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }

        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            iPlayer.message(Message.listenerBlockPlacementDenied)
            event.isCancelled = true
        }
    }


}