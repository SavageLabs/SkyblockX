package net.savagellc.savageskyblock.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.core.createIsland
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.core.hasPermission
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.entity.Player

class IslandCreateGUI :
    BaseGUI(Config.islandCreateGUITitle, Config.islandCreateGUIBackgroundItem, Config.islandCreateGUIRows),
    SkyblockGui {

    override fun buildGui() {
        // Builds the base GUI
        super.buildGui()

        val guiItems = ArrayList<GuiItem>()
        for (item in 0 until (Config.islandCreateGUIRows * 9)) {
            guiItems.add(GuiItem(super.backgroundItem.buildItem()) { e -> e.isCancelled = true })
        }
        for (island in Config.islandCreateGUIIslandTypes) {
            guiItems[island.guiIndex] = (GuiItem(island.item.buildItem()) { e ->
                run {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    if (!hasPermission(player, island.requirementPermission)) {
                        player.sendMessage(color(Message.islandCreateGUIYouDontHavePermission))
                    }
                    createIsland(player, island.structureFile.replace(".structure", ""))
                    player.sendMessage(Message.commandCreateSuccess)
                }
            })
            super.pane.populateWithGuiItems(guiItems)
            super.gui.addPane(super.pane)
            gui.addPane(super.pane)
            gui.update()
        }
    }

}
