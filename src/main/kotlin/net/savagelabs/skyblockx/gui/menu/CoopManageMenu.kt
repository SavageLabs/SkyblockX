package net.savagelabs.skyblockx.gui.menu

import com.cryptomorin.xseries.XMaterial
import com.deanveloper.skullcreator.SkullCreator
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIPlayerByUUID
import net.savagelabs.skyblockx.gui.PagedMenu
import net.savagelabs.skyblockx.gui.PagedMenuConfig
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

data class CoopMenuConfig(
	val guiTitle: String,
	val guiBackgroundItem: SerializableItem,
	val guiRows: Int,
	val coopedNextPageItem: GUIItem,
	val coopedPreviousPageItem: GUIItem,
	val coopedItemsPerPage: Int,
	val coopedSlotPageStart: GUICoordinate,
	val coopedItemNameFormat: String,
	val coopedItemLoreFormat: List<String>,
	val menuItems: List<MenuItem>,
	val coopItemNameFormat: String,
	val coopItemLoreFormat: List<String>
)

class CoopManageMenu(val player: Player) : PagedMenu(
	PagedMenuConfig(
		GUIConfig.instance.coopMenu.guiTitle,
		GUIConfig.instance.coopMenu.guiBackgroundItem,
		GUIConfig.instance.coopMenu.guiRows,
		SlotIterator.Type.HORIZONTAL,
		GUIConfig.instance.coopMenu.coopedSlotPageStart,
		GUIConfig.instance.coopMenu.coopedItemsPerPage,
		GUIConfig.instance.coopMenu.coopedNextPageItem,
		GUIConfig.instance.coopMenu.coopedPreviousPageItem,
		GUIConfig.instance.coopMenu.menuItems
	)
) {
	override fun getPageItems(): List<ClickableItem> {
		return player.getIPlayer().getIsland()!!.currentCoopPlayers.map { uuid -> getIPlayerByUUID(uuid) }
			.filter { iPlayer -> !player.getIPlayer().getIsland()!!.getIslandMembers().contains(iPlayer) }
			.map { iPlayer ->
				ClickableItem.of(
					SkullCreator.itemWithUuid(
						ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()!!)
							.name(GUIConfig.instance.coopMenu.coopItemNameFormat.replace("{player}", iPlayer!!.name))
							.lore(GUIConfig.instance.coopMenu.coopItemLoreFormat)
							.build(), iPlayer.uuid
					)
				) {
					player.getIPlayer().attemptExpel(iPlayer)
					buildMenu(this).open(player)
				}
			}
	}
}

