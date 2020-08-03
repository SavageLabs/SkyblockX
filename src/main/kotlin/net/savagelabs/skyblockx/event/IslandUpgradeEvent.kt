package net.savagelabs.skyblockx.event

import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.upgrade.UpgradeType
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class IslandUpgradeEvent(val island: Island, val upgradeType: UpgradeType) : Event(false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}