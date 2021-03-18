package net.savagelabs.skyblockx.upgrade.impl

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.exception.UpgradeException
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeType
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * This upgrade increases the amount of blocks in terms of material(s) an island can place.
 */
object PlacementLimitUpgradeType : UpgradeType<Event>(id = "PLACEMENT_LIMIT") {
    override var listener: Listener? = null

    override fun commence(player: IPlayer, island: Island, level: Int, upgrade: Upgrade) {
        // attempt to take money
        if (!player.takeMoney(this.priceOf(level, upgrade))) {
            return
        }

        // update boost
        val levelInfo = upgrade.levels[level]
        val material = XMaterial.matchXMaterial(upgrade.parameter)
            .orElseThrow { UpgradeException("Material on placement limit (level=$level) is not valid") }
        island.placementLimitBoost[material] =
            levelInfo?.parameter?.toIntOrNull()
                ?: throw UpgradeException("Amount on placement limit (level=$level) is not valid")

        // set level
        val upgradesMap = island.upgrades.getOrPut(this.id) { hashMapOf<XMaterial, Int>() } as HashMap<XMaterial, Int>
        upgradesMap[material] = level

        // call event
        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }
}