package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.isPlaceholderAPIPresent
import net.savagelabs.skyblockx.persist.Config
import me.clip.placeholderapi.PlaceholderAPI
import net.prosavage.baseplugin.ItemBuilder
import net.prosavage.baseplugin.XMaterial
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*


class IslandMemberGUI :
    BaseGUI(Config.islandMemberGUITitle, Config.islandMemberGUIBackgroundItem, Config.islandMemberGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        var slotListIndexesUsed = 0
        for (memberName in context.getIsland()!!.getIslandMembers()) {
            if (slotListIndexesUsed >= Config.islandMemberGUIHeadSlots.size) {
                Globals.skyblockX.logger.info("Skipping for $memberName due to not having a configured slot for the ${slotListIndexesUsed + 1}th member.")
                continue
            }
            guiItems[Config.islandMemberGUIHeadSlots[slotListIndexesUsed]] =
                GuiItem(getSkullOfPlayer(memberName.name, Config.islandMemberGUIItemMeta)!!) { e ->
                    run {
                        e.isCancelled = true
                        IslandMemberActionGUI(memberName.name).showGui(context.getPlayer())
                    }
                }
            slotListIndexesUsed++
        }

        if (slotListIndexesUsed < Config.islandMemberGUIHeadSlots.size) {
            for (slot in slotListIndexesUsed until Config.islandMemberGUIHeadSlots.size) {
                guiItems[Config.islandMemberGUIHeadSlots[slot]] =
                    GuiItem(ItemBuilder(Material.PLAYER_HEAD).name(Config.islandMemberGUINoMemberName).lore(Config.islandMemberGUINoMemberLore).build()) { e ->
                        e.isCancelled = true
                    }
            }
        }
        pane.populateWithGuiItems(guiItems)
    }

    private fun getSkullOfPlayer(name: String, headFormat: HeadFormat): ItemStack? {
        val itemStack = ItemStack(XMaterial.PLAYER_HEAD.parseItem()!!)
        val offlinePlayer = Bukkit.getOfflinePlayer(name)
        val skullMeta = itemStack.itemMeta as SkullMeta
        skullMeta.owner = name
        skullMeta.setDisplayName(
            color(
                if (isPlaceholderAPIPresent()) PlaceholderAPI.setPlaceholders(
                    offlinePlayer,
                    headFormat.name.replace("{player}", name)
                ) else headFormat.name.replace("{player}", name)
            )
        )
        val lore: MutableList<String> = ArrayList()
        for (line in headFormat.lore) {
            lore.add(
                if (isPlaceholderAPIPresent()) PlaceholderAPI.setPlaceholders(
                    offlinePlayer,
                    color(line)
                ) else color(line)
            )
        }
        skullMeta.lore = lore
        itemStack.itemMeta = skullMeta
        return itemStack
    }

}

class HeadFormat(val name: String, val lore: List<String>)