package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import org.bukkit.block.Biome
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandBiomeChangeEvent(val island: Island, val biome: Biome) : Event(false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}