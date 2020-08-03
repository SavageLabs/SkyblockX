package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandTransferEvent (val island: Island, val oldIslandLeader: IPlayer, val islandLeader: IPlayer) : Event(false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}