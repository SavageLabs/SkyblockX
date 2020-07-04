package net.savagelabs.skyblockx.gui

import com.cryptomorin.xseries.XMaterial
import com.github.stefvanschie.inventoryframework.GuiItem
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.gui.wrapper.populateWithMenuItems
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

data class IslandMenuGUIConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val guiMenuItems: List<MenuItem>
)

class IslandMenu : BaseMenu(true, MenuConfig(
    GUIConfig.instance.islandMenuGUI.guiTitle,
    GUIConfig.instance.islandMenuGUI.guiBackgroundItem,
    GUIConfig.instance.islandMenuGUI.guiRows,
    GUIConfig.instance.islandMenuGUI.guiMenuItems
)) {
    override fun fillContents(player: Player, contents: InventoryContents) {
        // Menu item GUI, nothing to be done.
    }
}