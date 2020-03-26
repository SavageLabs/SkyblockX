package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.Gui
import com.github.stefvanschie.inventoryframework.GuiItem
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import net.savagelabs.skyblockx.Globals.skyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player

abstract class BaseGUI(
    val title: String,
    val backgroundItem: net.savagelabs.skyblockx.persist.data.SerializableItem,
    val guiRows: Int
) {

    @Transient
    val gui = Gui(skyblockX, guiRows, color(title))

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