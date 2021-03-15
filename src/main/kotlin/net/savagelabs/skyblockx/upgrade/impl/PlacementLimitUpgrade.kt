package net.savagelabs.skyblockx.upgrade.impl

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.exception.UpgradeException
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeLevelInfo
import net.savagelabs.skyblockx.upgrade.levelItemsOrErrorByPreview
import net.savagelabs.skyblockx.upgrade.maxLevelItemOrErrorByPreview
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * This upgrade increases the amount of blocks in terms of material(s) an island can place.
 */
object PlacementLimitUpgrade : Upgrade<Event>(id = "PLACEMENT_LIMIT") {
    override val preview: Map<Int, UpgradeLevelInfo> by lazy { this.levelItemsOrErrorByPreview() }
    override val maxLevelItem: GUIItem by lazy { this.maxLevelItemOrErrorByPreview() }
    override var listener: Listener? = null

    override fun commence(player: IPlayer, island: Island, level: Int) {
        // attempt to take money
        if (!player.takeMoney(this.priceOf(level))) {
            return
        }

        // set island's level of upgrade correspondingly
        island.upgrades[this.id] = level

        // update boost
        val (type, amount) = preview[level]?.parameter?.split(";") ?: throw UpgradeException("Failed to fetch preview")
        val material = XMaterial.matchXMaterial(type)
            .orElseThrow { UpgradeException("Material on placement limit (level=$level) is not valid") }
        island.placementLimitBoost[material] =
            amount.toIntOrNull() ?: throw UpgradeException("Amount on placement limit (level=$level) is not valid")

        // call event
        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }
}