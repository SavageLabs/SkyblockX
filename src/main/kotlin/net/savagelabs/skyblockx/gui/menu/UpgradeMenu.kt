package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.Config
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
		Config.instance.upgrades.forEach { (type, upgradeTypeInfo) ->
			val upgradeLevel = (island.upgrades[type]?.plus(1) ?: 1)
			val upgradeLevelInfo = upgradeTypeInfo.upgradeInfoPerLevel[upgradeLevel]
			val upgradeItem = upgradeLevelInfo?.itemAtLevel ?: upgradeTypeInfo.maxLevelItem
			contents.set(
				upgradeItem.guiCoordinate.row,
				upgradeItem.guiCoordinate.column,
				ClickableItem.of(upgradeItem.item.buildItem()) {
					if (upgradeItem == upgradeTypeInfo.maxLevelItem) {
						return@of
					}

					when (type) {
						UpgradeType.GENERATOR -> {
							GeneratorUpgrade.runUpgradeEffect(island, upgradeLevel)
						}
						UpgradeType.BORDER -> {
							BorderUpgrade.runUpgradeEffect(island, upgradeLevel)
						}
						UpgradeType.MAX_HOMES -> {
							HomeUpgrade.runUpgradeEffect(island, upgradeLevel)
						}
						UpgradeType.TEAM_SIZE -> {
							TeamUpgrade.runUpgradeEffect(island, upgradeLevel)
						}
					}
					buildMenu(this).open(player)
				})


		}
	}

}