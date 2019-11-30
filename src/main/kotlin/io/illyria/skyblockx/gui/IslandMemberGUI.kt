package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.createIsland
import io.illyria.skyblockx.core.hasPermission
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import me.clip.placeholderapi.PlaceholderAPI
import net.prosavage.baseplugin.XMaterial
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*


class IslandMemberGUI :
    BaseGUI(Config.islandCreateGUITitle, Config.islandCreateGUIBackgroundItem, Config.islandCreateGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = ArrayList<GuiItem>()
        for (item in 0 until (super.guiRows * 9)) {
            guiItems.add(GuiItem(super.backgroundItem.buildItem()) { e -> e.isCancelled = true })
        }
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
            pane.populateWithGuiItems(guiItems)
        }
    }

    private fun getSkullOfPlayer(name: String, headFormat: HeadFormat): ItemStack? {
        val itemStack = ItemStack(XMaterial.PLAYER_HEAD.parseItem()!!)
        val skullMeta = itemStack.itemMeta as SkullMeta
        skullMeta.owner = name
        skullMeta.setDisplayName(color(headFormat.name.replace("{player}", name)))
        val lore: MutableList<String> = ArrayList()
        for (line in headFormat.lore) {
            lore.add(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(name), color(line)))
        }
        skullMeta.lore = lore
        itemStack.itemMeta = skullMeta
        return itemStack
    }

}

class HeadFormat(val name: String, val lore: List<String>)