package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.Gui
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIPlayer
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player

abstract class BaseGUI(val title: String, val backgroundItem: SerializableItem, val guiRows: Int) {

    @Transient
    val gui = Gui(io.illyria.skyblockx.Globals.skyblockX, guiRows, color(title))
    @Transient
    val pane = PaginatedPane(0, 0, 9, guiRows)

    abstract fun populatePane(context: IPlayer)

    fun showGui(humanEntity: HumanEntity) {
        populatePane(getIPlayer(humanEntity as Player))
        gui.addPane(pane)
        gui.update()
        gui.show(humanEntity)
    }

}