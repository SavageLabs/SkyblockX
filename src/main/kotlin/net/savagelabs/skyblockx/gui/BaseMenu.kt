package net.savagelabs.skyblockx.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.gui.wrapper.populateWithMenuItems
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player
import java.util.*

open class MenuConfig(val title: String, val backgroundItem: SerializableItem, val rows: Int, val menuItems: List<MenuItem>)

abstract class BaseMenu(val fillBackground: Boolean = true, val menuConfig: MenuConfig) : InventoryProvider {

    abstract fun fillContents(player: Player, contents: InventoryContents)

    override fun init(player: Player, contents: InventoryContents) {
        if (fillBackground) {
            contents.fill(ClickableItem.empty(menuConfig.backgroundItem.buildItem()))
        }
        contents.populateWithMenuItems(player, menuConfig.menuItems)
        fillContents(player, contents)
    }

    fun build(): SmartInventory {
        return SmartInventory.builder()
            .manager(SkyblockX.inventoryManager)
            .id(UUID.randomUUID().toString())
            .provider(this)
            .size(menuConfig.rows, 9)
            .title(color(menuConfig.title))
            .build()
    }
}