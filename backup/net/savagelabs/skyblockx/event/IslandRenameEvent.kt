package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandRenameEvent(val island: Island, var newName: String, val oldName: String) : Event(false) {


	private var isCancelled = false

	companion object {
		@JvmStatic
		val handlerList = HandlerList()
	}

	override fun getHandlers(): HandlerList {
		return handlerList
	}

}