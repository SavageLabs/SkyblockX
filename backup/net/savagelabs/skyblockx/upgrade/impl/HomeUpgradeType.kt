package net.savagelabs.skyblockx.upgrade.impl

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeType
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * This upgrade increases the amount of max homes an Island has.
 */
object HomeUpgradeType : UpgradeType<Event>(id = "MAX_HOMES") {
    override var listener: Listener? = null

    override fun commence(player: IPlayer, island: Island, level: Int, upgrade: Upgrade) {
        if (!player.takeMoney(this.priceOf(level, upgrade))) {
            return
        }

        // set island's level of upgrade correspondingly
        island.upgrades[this.id] = level

        // assign home boost & make sure the parameter is an integer otherwise log failure and break out of the function
        island.homeBoost += upgrade.levels[level]?.parameter?.toIntOrNull() ?: run {
            SkyblockX.skyblockX.logger.info("Home Upgrade failed due to the param not being an integer")
            return
        }

        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }
}