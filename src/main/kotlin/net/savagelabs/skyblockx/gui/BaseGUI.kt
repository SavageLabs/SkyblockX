package net.savagelabs.skyblockx.gui

import com.cryptomorin.xseries.XMaterial
import com.github.stefvanschie.inventoryframework.Gui
import com.github.stefvanschie.inventoryframework.GuiItem
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.gui.wrapper.populateWithMenuItems
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseGUI(
    val title: String,
    val backgroundItem: SerializableItem,
    val guiRows: Int
) {

    @Transient
    val gui = Gui(SkyblockX.skyblockX, guiRows, color(title))

    @Transient
    val pane = PaginatedPane(0, 0, 9, guiRows)

    abstract fun populatePane(context: IPlayer)

    fun buildFullBackgroundItemlist(): MutableList<GuiItem> {
        val guiItems = ArrayList<GuiItem>()
        for (item in 0 until (this.guiRows * 9)) {
            guiItems.add(GuiItem(this.backgroundItem.buildItem()) { e -> e.isCancelled = true })
        }

        return guiItems
    }

    fun showGui(humanEntity: HumanEntity) {
        populatePane(getIPlayer(humanEntity as Player))
        gui.addPane(pane)
        gui.update()
        gui.show(humanEntity)
    }

}



