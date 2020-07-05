package net.savagelabs.skyblockx.gui.wrapper

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

class MenuItem(
    val guiItem: SerializableItem,
    val commandsToExecute: List<String>,
    val guiCoordinate: GUICoordinate
)


fun InventoryContents.populateWithMenuItems(player: Player, menuItems: List<MenuItem>) {
    menuItems.forEach { item ->
        set(item.guiCoordinate.row, item.guiCoordinate.column, ClickableItem.of(item.guiItem.buildItem()) {
            it.isCancelled = true
            item.commandsToExecute.forEach { command ->
                player.performCommand(command) }
        })
    }
}