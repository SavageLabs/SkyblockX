package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandJoinEvent (val island: Island, val iPlayer: IPlayer) : Event(false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}