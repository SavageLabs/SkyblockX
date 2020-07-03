package net.savagelabs.skyblockx.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.gui.menu.CoopInviteMenu
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player
import java.util.*

open class PagedMenuConfig(
    guiTitle: String,
    guiBackgroundItem: SerializableItem,
    guiRows: Int,
    val itemPageDirection: SlotIterator.Type,
    val itemPageStartCoordinate: GUICoordinate,
    val itemsPerPage: Int,
    val nextPageItem: GUIItem,
    val previousPageItem: GUIItem,
    val guiMenuItems: List<MenuItem>
) : MenuConfig(
    guiTitle,
    guiBackgroundItem,
    guiRows,
    guiMenuItems
)


abstract class PagedMenu(val pagedMenuConfig: PagedMenuConfig, val fillBackgroundItems: Boolean = true) : BaseMenu(
    fillBackgroundItems, MenuConfig(pagedMenuConfig.title, pagedMenuConfig.backgroundItem, pagedMenuConfig.rows, pagedMenuConfig.guiMenuItems)
) {

    abstract fun getPageItems(): List<ClickableItem>

    override fun fillContents(player: Player, contents: InventoryContents) {
        val pagination = contents.pagination()

        pagination.setItems(*getPageItems().toTypedArray())

        pagination.setItemsPerPage(pagedMenuConfig.itemsPerPage)

        pagination.addToIterator(contents.newIterator(
            pagedMenuConfig.itemPageDirection,
            pagedMenuConfig.itemPageStartCoordinate.row,
            pagedMenuConfig.itemPageStartCoordinate.column
        ))
        val nextPageItem = pagedMenuConfig.nextPageItem
        contents.set(nextPageItem.guiCoordinate.row, nextPageItem.guiCoordinate.column, ClickableItem.of(nextPageItem.item.buildItem()) {
            this.build().open(player, pagination.previous().page)
        })

        val previousPageItem = pagedMenuConfig.previousPageItem
        contents.set(previousPageItem.guiCoordinate.row, previousPageItem.guiCoordinate.column, ClickableItem.of(previousPageItem.item.buildItem()) {
            this.build().open(player, pagination.previous().page)
        })
    }



}