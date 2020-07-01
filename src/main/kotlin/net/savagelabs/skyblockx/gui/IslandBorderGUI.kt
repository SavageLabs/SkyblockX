package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.updateWorldBorder
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.worldborder.WorldBorderUtil
import org.bukkit.entity.Player

class IslandBorderGUI :
    BaseGUI(
        Config.instance.islandBorderGUITitle,
        Config.instance.islandBorderGUIBackgroundItem,
        Config.instance.islandBorderGUIRows
    ) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        Config.instance.islandBorderGUIItems.forEach { color: WorldBorderUtil.Color, item: IslandBorderItem ->
            guiItems[item.slot] = GuiItem(item.displayItem.buildItem()) { e ->
                run {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    getIPlayer(player).borderColor = color
                    updateWorldBorder(player, player.location, 0L)
                }
            }
        }
        pane.populateWithGuiItems(guiItems)

    }

}

class IslandBorderItem(val slot: Int, val displayItem: net.savagelabs.skyblockx.persist.data.SerializableItem)