package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.entity.Player

class IslandMenuGUI : BaseGUI(Config.instance.islandMenuGUITitle, Config.instance.islandMenuGUIBackgroundItem, Config.instance.islandMenuGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        for (item in Config.instance.islandMenuGUIItems) {
            guiItems[item.slot] = (GuiItem(item.guiItem.buildItem()) { e ->
                run {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    executeCommands(item.commandsToExecute, player)
                }
            })
        }
        pane.populateWithGuiItems(guiItems)
    }

    private fun executeCommands(commands: List<String>, player: Player) {
        for (command in commands) {
            player.performCommand(command.replace("{player}", player.name))
        }
    }

}

class MenuItem(
    val guiItem: net.savagelabs.skyblockx.persist.data.SerializableItem,
    val commandsToExecute: List<String>,
    val slot: Int
)