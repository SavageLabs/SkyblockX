package net.savagelabs.skyblockx.gui.menu

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
		for (upgrade in UpgradeManager.getAll().values) {
			val level = island.upgrades[upgrade.id]?.plus(1) ?: 1
			val info = upgrade.preview[level]

			val guiItem = info?.itemAtLevel ?: upgrade.maxLevelItem
			val (column, row) = guiItem.guiCoordinate

			val item = guiItem.item
			contents.set(row, column, ClickableItem.of(item.buildItem()) {
				// if their level is max, ignore clicks
				if (guiItem == upgrade.maxLevelItem) {
					return@of
				}

				// commence upgrade
				val islandPlayer = player.getIPlayer()
				upgrade.commence(islandPlayer, island, level)

				// reopen menu to update items
				buildMenu(this).open(player)
			})
		}
	}
}