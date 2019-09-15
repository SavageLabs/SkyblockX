package net.savagellc.savageskyblock.listener

import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent


class BlockListener : Listener {



    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val iPlayer = getIPlayer(event.player)
        if (!iPlayer.hasIsland()) return
        if (!iPlayer.getIsland()!!.containsBlock(event.block.location)) {
            event.player.sendMessage(Message.listenerBlockPlacementDenied)
            event.isCancelled = true
        }
    }



}