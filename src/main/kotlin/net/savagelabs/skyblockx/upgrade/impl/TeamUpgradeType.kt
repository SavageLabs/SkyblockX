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
 * This upgrade type handles increases of Island team size.
 */
object TeamUpgradeType : UpgradeType<Event>(id = "TEAM_SIZE") {
    override var listener: Listener? = null

    override fun commence(player: IPlayer, island: Island, level: Int, upgrade: Upgrade) {
        if (!player.takeMoney(this.priceOf(level, upgrade))) {
            return
        }

        // assign member boost & make sure the parameter is an integer otherwise log failure and break out of the function
        island.memberBoost += upgrade.levels[level]?.parameter?.toIntOrNull() ?: run {
            SkyblockX.skyblockX.logger.info("Team Upgrade failed due to the param not being an integer")
            return
        }

        // set island's level of upgrade correspondingly
        island.upgrades[this.id] = level

        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }
}