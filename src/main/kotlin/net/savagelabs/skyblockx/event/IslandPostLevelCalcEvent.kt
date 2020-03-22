package net.savagelabs.skyblockx.event

import io.illyria.skyblockx.core.Island
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandPostLevelCalcEvent(val island: Island, var levelAfterCalc: Double?) : Event(false) {

    fun setLevelAfterCalc(levelAfterCalc: Double) {
        this.levelAfterCalc = levelAfterCalc
    }

    private val HANDLERS = HandlerList()
    private var isCancelled = false

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    fun getHandlerList(): HandlerList? {
        return HANDLERS
    }

}