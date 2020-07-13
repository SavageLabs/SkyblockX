package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandPostLevelCalcEvent(val island: Island, var levelAfterCalc: Double?) : Event(true) {

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList? {
            return HANDLERS
        }
    }

    fun setLevelAfterCalc(levelAfterCalc: Double) {
        this.levelAfterCalc = levelAfterCalc
    }

    private var isCancelled = false

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

}