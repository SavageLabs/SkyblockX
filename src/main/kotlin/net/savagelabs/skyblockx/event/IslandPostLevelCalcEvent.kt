package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandPostLevelCalcEvent(val island: Island, var levelAfterCalc: Double?) : Event(true) {

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