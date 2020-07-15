package net.savagelabs.skyblockx.gui.menu

import com.deanveloper.skullcreator.SkullCreator
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.gui.PagedMenu
import net.savagelabs.skyblockx.gui.PagedMenuConfig
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.Bukkit

data class InviteMenuConfig(
	val guiTitle: String,
	val guiBackgroundItem: SerializableItem,
	val guiRows: Int,
	val itemsPageDirection: SlotIterator.Type,
	val itemPageStartCoordinate: GUICoordinate,
	val itemsPerPage: Int,
	val nextPageItem: GUIItem,
	val previousPageItem: GUIItem,
	val itemNameFormat: String,
	val itemLoreFormat: List<String>,
	val guiMenuItems: List<MenuItem>
)


class InviteMenu(val island: Island, val iPlayer: IPlayer) : PagedMenu(
	PagedMenuConfig(
		GUIConfig.instance.inviteMenuConfig.guiTitle,
		GUIConfig.instance.inviteMenuConfig.guiBackgroundItem,
		GUIConfig.instance.inviteMenuConfig.guiRows,
		GUIConfig.instance.inviteMenuConfig.itemsPageDirection,
		GUIConfig.instance.inviteMenuConfig.itemPageStartCoordinate,
		GUIConfig.instance.inviteMenuConfig.itemsPerPage,
		GUIConfig.instance.inviteMenuConfig.nextPageItem,
		GUIConfig.instance.inviteMenuConfig.previousPageItem,
		GUIConfig.instance.inviteMenuConfig.guiMenuItems
	)
) {
	override fun getPageItems(): List<ClickableItem> {
		val inviteMenuConfig = GUIConfig.instance.inviteMenuConfig
		return Bukkit.getOnlinePlayers()
			.filter { player ->
				val loopIplayer = player.getIPlayer()
				!island.getIslandMembers().contains(loopIplayer) && !loopIplayer.isInvitedToIsland(island)
			}
			.map { player ->
				ClickableItem.of(
					ItemBuilder(SkullCreator.itemFromUuid(player.uniqueId))
						.name(inviteMenuConfig.itemNameFormat.replace("{player}", player.name))
						.lore(inviteMenuConfig.itemLoreFormat).build()
				) {
					island.attemptInvite(iPlayer, player.getIPlayer())
					buildMenu(this).open(iPlayer.getPlayer())
				}
			}
	}

}

