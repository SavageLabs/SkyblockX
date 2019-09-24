package net.savagellc.savageskyblock.listener

import net.savagellc.savageskyblock.core.IPlayer
import net.savagellc.savageskyblock.core.canUseBlockAtLocation
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent


class BlockListener : Listener {


    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val iPlayer = getIPlayer(event.player)
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland()) {
            event.player.sendMessage(Message.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }

        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            event.player.sendMessage(Message.listenerBlockPlacementDenied)
            event.isCancelled = true
        }
    }









}