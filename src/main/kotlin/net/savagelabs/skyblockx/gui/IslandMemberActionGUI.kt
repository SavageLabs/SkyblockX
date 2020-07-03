package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import me.clip.placeholderapi.PlaceholderAPI
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.isPlaceholderAPIPresent
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.GUIConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IslandMemberActionGUI(val name: String) :
    BaseGUI(
        GUIConfig.instance.islandMenuGUI.guiTitle,
        GUIConfig.instance.islandMenuGUI.guiBackgroundItem,
        GUIConfig.instance.islandMenuGUI.guiRows
    ) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()

        for (item in GUIConfig.instance.islandMenuGUI.menuItems) {
//            guiItems[item.slot] = GuiItem(buildMenuItem(item)) { e ->
//                e.isCancelled = true
//                executeCommands(item.commandsToExecute, context.getPlayer()!!)
//            }
        }
        pane.populateWithGuiItems(guiItems)
    }

    private fun executeCommands(commands: List<String>, player: Player) {
        for (command in commands) {
            player.performCommand(command.replace("{player}", name))
        }
    }

    private fun buildMenuItem(item: MenuItem): ItemStack {
        val offlinePlayer = Bukkit.getOfflinePlayer(name)
        val lore = item.guiItem.lore
        for (line in lore) {
            line.replace("{player}", name)
        }
        item.guiItem.lore = if (isPlaceholderAPIPresent()) PlaceholderAPI.setPlaceholders(offlinePlayer, lore) else lore
        item.guiItem.name = if (isPlaceholderAPIPresent()) PlaceholderAPI.setPlaceholders(
            offlinePlayer,
            item.guiItem.name.replace("{player}", name)
        ) else item.guiItem.name.replace("{player}", name)
        return item.guiItem.buildItem()
    }


}

