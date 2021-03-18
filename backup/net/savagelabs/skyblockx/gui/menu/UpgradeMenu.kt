package net.savagelabs.skyblockx.gui.menu

import com.cryptomorin.xseries.XMaterial
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.manager.UpgradeManager
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import net.savagelabs.skyblockx.upgrade.*
import net.savagelabs.skyblockx.upgrade.impl.PlacementLimitUpgradeType
import org.bukkit.entity.Player

data class UpgradeMenuConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val guiMenuItems: List<MenuItem>
)

class UpgradeMenu(val island: Island) : BaseMenu(
    true,
    MenuConfig(
        GUIConfig.instance.upgradeMenuConfig.guiTitle,
        GUIConfig.instance.upgradeMenuConfig.guiBackgroundItem,
        GUIConfig.instance.upgradeMenuConfig.guiRows,
        GUIConfig.instance.upgradeMenuConfig.guiMenuItems
    )
) {
    override fun fillContents(player: Player, contents: InventoryContents) {
        val types = UpgradeManager.cached.values
		val islandPlayer = player.getIPlayer()

		if (!islandPlayer.hasIsland() && !islandPlayer.hasCoopIsland()) {
			player.closeInventory()
			return
		}

        for (type in types) {
            val registry = type.registry.values
            val isPlacementLimit = type is PlacementLimitUpgradeType
            val levels = island.upgrades[type.id]

            for (upgrade in registry) {
                val level = (if (!isPlacementLimit) (levels?.toString()?.toIntOrNull() ?: 0)
					else (levels as? HashMap<XMaterial, Int>)?.get(XMaterial.matchXMaterial(upgrade.parameter).get()) ?: 0) + 1
				val info = upgrade.levels[level]

				val maxLevelItem = upgrade.maxLevelItem
				val guiItem = info?.itemAtLevel ?: maxLevelItem
				val (column, row) = guiItem.guiCoordinate

				contents.set(row, column, ClickableItem.of(guiItem.item.buildItem()) {
					// if the island's level is max, ignore the click event
					if (guiItem == maxLevelItem) {
						return@of
					}

					// make sure the player actually is owns / is on a COOP island...
					if (!islandPlayer.hasIsland() && !islandPlayer.hasCoopIsland()) {
						player.closeInventory()
						return@of
					}

					// commence the upgrade
					type.commence(islandPlayer, island, level, upgrade)

					// reopen menu to update items
					buildMenu(this).open(player)
				})
            }

            // base handle?
        }
    }
}