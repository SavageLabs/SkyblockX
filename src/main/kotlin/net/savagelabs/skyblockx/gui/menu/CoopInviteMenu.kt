package net.savagelabs.skyblockx.gui.menu

import com.deanveloper.skullcreator.SkullCreator
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.PagedMenu
import net.savagelabs.skyblockx.gui.PagedMenuConfig
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*


data class CoopMenuInviteConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val browserDirection: SlotIterator.Type,
    val browserNextPageItem: GUIItem,
    val browserPreviousPageItem: GUIItem,
    val browserItemsPerPage: Int,
    val browserPageStartSlot: GUICoordinate,
    val browserItemNameFormat: String,
    val browserItemLoreFormat: List<String>,
    val menuItems: List<MenuItem>
)

class CoopInviteMenu(val player: Player, val island: Island) : PagedMenu(
    PagedMenuConfig(
        GUIConfig.instance.coopMenuInviteConfig.guiTitle,
        GUIConfig.instance.coopMenuInviteConfig.guiBackgroundItem,
        GUIConfig.instance.coopMenuInviteConfig.guiRows,
        GUIConfig.instance.coopMenuInviteConfig.browserDirection,
        GUIConfig.instance.coopMenuInviteConfig.browserPageStartSlot,
        GUIConfig.instance.coopMenuInviteConfig.browserItemsPerPage,
        GUIConfig.instance.coopMenuInviteConfig.browserNextPageItem,
        GUIConfig.instance.coopMenuInviteConfig.browserPreviousPageItem,
        GUIConfig.instance.coopMenuInviteConfig.menuItems
    )
) {
    override fun getPageItems(): List<ClickableItem> {
       return Bukkit.getOnlinePlayers()
           .filter { player -> !island.currentCoopPlayers.contains(player.uniqueId) }
           .filter { player -> !island.getIslandMembers().contains(player.getIPlayer()) }
           .filter { filterPlayer -> filterPlayer != player }
            .map { plyr ->
                ClickableItem.of(
                    ItemBuilder(SkullCreator.itemFromUuid(plyr.uniqueId))
                        .name(GUIConfig.instance.coopMenuInviteConfig.browserItemNameFormat.replace("{player}", plyr.name))
                        .lore(GUIConfig.instance.coopMenuInviteConfig.browserItemLoreFormat)
                        .build()
                ) {
                    player.getIPlayer().attemptToCoopPlayer(plyr.getIPlayer())
                    buildMenu(this).open(player)
                }
            }

    }

}

