package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.persist.Config
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.Player

class IslandMenuGUI : BaseGUI(Config.islandMenuGUITitle, Config.islandMenuGUIBackgroundItem, Config.islandMenuGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        for (item in Config.islandMenuGUIItems) {
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

class MenuItem(val guiItem: SerializableItem, val commandsToExecute: List<String>, val slot: Int)