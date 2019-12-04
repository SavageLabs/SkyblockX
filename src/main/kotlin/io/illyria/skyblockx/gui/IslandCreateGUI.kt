package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.createIsland
import io.illyria.skyblockx.core.hasPermission
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import org.bukkit.entity.Player

class IslandCreateGUI :
    BaseGUI(Config.islandCreateGUITitle, Config.islandCreateGUIBackgroundItem, Config.islandCreateGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        for (island in Config.islandCreateGUIIslandTypes) {
            guiItems[island.guiIndex] = (GuiItem(island.item.buildItem()) { e ->
                run {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    if (!hasPermission(player, island.requirementPermission)) {
                        player.sendMessage(color(Message.messagePrefix + Message.islandCreateGUIYouDontHavePermission))
                    }
                    createIsland(player, island.structureFile.replace(".structure", ""))
                    player.sendMessage(color(Message.messagePrefix + Message.commandCreateSuccess))
                }
            })
        }
        pane.populateWithGuiItems(guiItems)
    }

}
