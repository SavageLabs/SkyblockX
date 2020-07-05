package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

data class IslandMenuGUIConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val guiMenuItems: List<MenuItem>
)

class IslandMenu : BaseMenu(true,
    MenuConfig(
        GUIConfig.instance.islandMenuGUI.guiTitle,
        GUIConfig.instance.islandMenuGUI.guiBackgroundItem,
        GUIConfig.instance.islandMenuGUI.guiRows,
        GUIConfig.instance.islandMenuGUI.guiMenuItems
    )
) {
    override fun fillContents(player: Player, contents: InventoryContents) {
        // Menu item GUI, nothing to be done.
    }
}