package net.savagelabs.skyblockx.gui

import com.cryptomorin.xseries.XMaterial
import com.github.stefvanschie.inventoryframework.GuiItem
import me.clip.placeholderapi.PlaceholderAPI
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.isPlaceholderAPIPresent
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*


class IslandMemberGUI :
    BaseGUI(Config.instance.islandMemberGUITitle, Config.instance.islandMemberGUIBackgroundItem, Config.instance.islandMemberGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        var slotListIndexesUsed = 0
        for (memberName in context.getIsland()!!.getIslandMembers()) {
            if (slotListIndexesUsed >= Config.instance.islandMemberGUIHeadSlots.size) {
                SkyblockX.skyblockX.logger.info("Skipping for $memberName due to not having a configured slot for the ${slotListIndexesUsed + 1}th member.")
                continue
            }
            guiItems[Config.instance.islandMemberGUIHeadSlots[slotListIndexesUsed]] =
                GuiItem(getSkullOfPlayer(memberName.name, Config.instance.islandMemberGUIItemMeta)!!) { e ->
                    run {
                        e.isCancelled = true
                        IslandMemberActionGUI(memberName.name).showGui(context.getPlayer())
                    }
                }
            slotListIndexesUsed++
        }

        if (slotListIndexesUsed < Config.instance.islandMemberGUIHeadSlots.size) {
            for (slot in slotListIndexesUsed until Config.instance.islandMemberGUIHeadSlots.size) {
                guiItems[Config.instance.islandMemberGUIHeadSlots[slot]] =
                    GuiItem(
                        ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()!!).name(Config.instance.islandMemberGUINoMemberName)
                            .lore(Config.instance.islandMemberGUINoMemberLore).build()
                    ) { e ->
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