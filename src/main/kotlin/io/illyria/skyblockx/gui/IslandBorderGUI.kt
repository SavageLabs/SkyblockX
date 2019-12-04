package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.core.updateWorldBorder
import io.illyria.skyblockx.persist.Config
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.Player

class IslandBorderGUI :
    BaseGUI(Config.islandBorderGUITitle, Config.islandBorderGUIBackgroundItem, Config.islandBorderGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        Config.islandBorderGUIItems.forEach { color: WorldBorderUtil.Color, item: IslandBorderItem ->
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

class IslandBorderItem(val slot: Int, val displayItem: SerializableItem)