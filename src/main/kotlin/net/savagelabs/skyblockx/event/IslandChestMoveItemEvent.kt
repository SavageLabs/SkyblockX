package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.listener.InventoryMoveAction
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandChestMoveItemEvent(val iPlayer: IPlayer, val island: Island, val action: Action, val moveActionList: List<InventoryMoveAction>) : Event(), Cancellable {


    private var isCancelled = false

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    override fun setCancelled(cancel: Boolean) {
        this.isCancelled = cancel
    }

    override fun isCancelled(): Boolean {
        return this.isCancelled
    }

    enum class Action {
        CLICK,
        DRAG
    }

}