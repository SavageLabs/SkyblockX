package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandRenameEvent(val island: Island, var newName: String, val oldName: String) : Event(false) {

	private val HANDLERS = HandlerList()
	private var isCancelled = false

	override fun getHandlers(): HandlerList {
		return HANDLERS
	}

	fun getHandlerList(): HandlerList? {
		return HANDLERS
	}

}