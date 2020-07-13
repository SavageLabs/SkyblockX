package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

data class PermsMenuConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val guiMenuItems: List<MenuItem>
)

class PermsMenu : BaseMenu(true, MenuConfig(
    GUIConfig.instance.permsMenuConfig.guiTitle,
    GUIConfig.instance.permsMenuConfig.guiBackgroundItem,
    GUIConfig.instance.permsMenuConfig.guiRows,
    GUIConfig.instance.permsMenuConfig.guiMenuItems
)) {

    override fun fillContents(player: Player, contents: InventoryContents) {
        // Nothing here
    }

}