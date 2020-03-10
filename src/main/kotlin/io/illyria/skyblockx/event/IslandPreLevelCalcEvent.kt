package io.illyria.skyblockx.event

import io.illyria.skyblockx.core.Island
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandPreLevelCalcEvent(val island: Island, val levelBeforeCalc: Double?) : Event(false), Cancellable {

    private val HANDLERS = HandlerList()
    private var isCancelled = false

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    fun getHandlerList(): HandlerList? {
        return HANDLERS
    }

    override fun setCancelled(cancel: Boolean) {
        this.isCancelled = cancel
    }

    override fun isCancelled(): Boolean {
        return this.isCancelled
    }



}