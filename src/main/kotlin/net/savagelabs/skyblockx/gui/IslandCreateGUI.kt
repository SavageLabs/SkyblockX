package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.createIsland
import net.savagelabs.skyblockx.core.hasPermission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
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
                        player.sendMessage(
                            color(
                                String.format(
                                    Message.messagePrefix + Message.islandCreateGUIYouDontHavePermissionToUseIsland,
                                    island.requirementPermission
                                )
                            )
                        )
                        return@run
                    }
                    val createdIsland = createIsland(player, island.structureFile.replace(".structure", ""))
                    createdIsland.netherFilePath = island.netherFile
                    player.sendMessage(color(Message.messagePrefix + Message.commandCreateSuccess))
                    context.lastIslandResetTime = System.currentTimeMillis() / 1000
                }
            })
        }
        pane.populateWithGuiItems(guiItems)
    }

}
