package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.createIsland
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.hasPermission
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.data.IslandCreateInfo
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

data class CreateMenuConfig(
	val guiTitle: String,
	val guiBackgroundItem: SerializableItem,
	val guiRows: Int,
	val islandInfo: List<IslandCreateInfo>,
	val guiMenuItems: List<MenuItem>
)


class CreateMenu(
	player: Player
) : BaseMenu(
	true, MenuConfig(
		GUIConfig.instance.createGUIConfig.guiTitle,
		GUIConfig.instance.createGUIConfig.guiBackgroundItem,
		GUIConfig.instance.createGUIConfig.guiRows,
		GUIConfig.instance.createGUIConfig.guiMenuItems
	)
) {
	override fun fillContents(player: Player, contents: InventoryContents) {
		GUIConfig.instance.createGUIConfig.islandInfo.forEach { islandCreateInfo ->
			contents.set(
				islandCreateInfo.guiCoordinate.row,
				islandCreateInfo.guiCoordinate.column,
				ClickableItem.of(islandCreateInfo.item.buildItem()) {
					if (!hasPermission(player, islandCreateInfo.requirementPermission)) {
						player.sendMessage(
							color(
								String.format(
									Message.instance.messagePrefix + Message.instance.islandCreateGUIYouDontHavePermissionToUseIsland,
									islandCreateInfo.requirementPermission
								)
							)
						)
						return@of
					}
					val createdIsland = createIsland(player, islandCreateInfo.structureFile.replace(".structure", ""))
					createdIsland.netherFilePath = islandCreateInfo.netherFile
					player.sendMessage(color(Message.instance.messagePrefix + Message.instance.commandCreateSuccess))
					player.getIPlayer().lastIslandResetTime = System.currentTimeMillis() / 1000
				}

			)
		}
	}
}