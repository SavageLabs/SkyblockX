package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.updateWorldBorder
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import net.savagelabs.worldborder.WorldBorderUtil
import org.bukkit.entity.Player

data class BorderMenuConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val borderItems: HashMap<WorldBorderUtil.Color, GUIItem>,
    val guiMenuItems: List<MenuItem>
) : MenuConfig(
    guiTitle,
    guiBackgroundItem,
    guiRows,
    guiMenuItems
)

class BorderMenu : BaseMenu(true, MenuConfig(
    GUIConfig.instance.borderMenuConfig.guiTitle,
    GUIConfig.instance.borderMenuConfig.guiBackgroundItem,
    GUIConfig.instance.borderMenuConfig.guiRows,
    GUIConfig.instance.borderMenuConfig.guiMenuItems
)) {
    override fun fillContents(player: Player, contents: InventoryContents) {
        GUIConfig.instance.borderMenuConfig.borderItems.forEach { (color, item) ->
            contents.set(item.guiCoordinate.row, item.guiCoordinate.column, ClickableItem.of(item.item.buildItem()) {
                getIPlayer(player).borderColor = color
                updateWorldBorder(player, player.location, 0L)
            })
        }
    }

}