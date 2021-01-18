package net.savagelabs.skyblockx.upgrade.impl

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.updateWorldBorder
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeLevelInfo
import net.savagelabs.skyblockx.upgrade.levelItemsOrErrorByPreview
import net.savagelabs.skyblockx.upgrade.maxLevelItemOrErrorByPreview
import org.bukkit.Bukkit
import org.bukkit.event.Listener

/**
 * This upgrade increases the size of an Island.
 */
object SizeUpgrade : Upgrade {
    override val id: String = "ISLAND_SIZE"
    override val preview: Map<Int, UpgradeLevelInfo> by lazy { this.levelItemsOrErrorByPreview() }
    override val maxLevelItem: GUIItem by lazy { this.maxLevelItemOrErrorByPreview() }
    override val listener: Listener? = null

    override fun commence(player: IPlayer, island: Island, level: Int) {
        if (!player.takeMoney(this.priceOf(level))) {
            return
        }

        // set island's level of upgrade correspondingly
        island.upgrades[this.id] = level

        // assign island size & make sure the parameter is an integer otherwise log failure and break out of the function
        island.islandSize = Config.instance.upgrades[this.id]?.upgradeInfoPerLevel?.get(level)?.parameter?.toIntOrNull() ?: run {
            SkyblockX.skyblockX.logger.info("Island Size Upgrade failed due to the param not being an integer")
            return
        }

        // update all online island members' borders
        for (member in island.getIslandMembers()) {
            val bukkitPlayer = member.getPlayer() ?: continue
            updateWorldBorder(bukkitPlayer, bukkitPlayer.location, 10L)
        }

        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }
}