package net.savagellc.savageskyblock.gui

import com.github.stefvanschie.inventoryframework.Gui
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import net.savagellc.savageskyblock.Globals
import net.savagellc.savageskyblock.core.color
import org.bukkit.entity.HumanEntity

open class BaseGUI(val title: String, val backgroundItem: SerializableItem, val guiRows: Int) {

    @Transient
    val gui = Gui(Globals.savageSkyblock, guiRows, color(title))
    @Transient
    val pane = PaginatedPane(0, 0, 9, guiRows)


    fun buildGUI() {
        gui.addPane(pane)
        gui.update()
    }

    fun showGUI(humanEntity: HumanEntity) {
        gui.show(humanEntity)
    }

}