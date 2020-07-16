package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandPreLevelCalcEvent(val island: Island, val levelBeforeCalc: Double) : Event(true), Cancellable {


	private var isCancelled = false

	companion object {
		@JvmStatic
		val handlerList = HandlerList()
	}

	override fun getHandlers(): HandlerList {
		return handlerList
	}

	override fun setCancelled(cancel: Boolean) {
		this.isCancelled = cancel
	}

	override fun isCancelled(): Boolean {
		return this.isCancelled
	}


}