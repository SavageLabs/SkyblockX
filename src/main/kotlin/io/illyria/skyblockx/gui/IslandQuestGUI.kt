package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.quest.Quest
import net.prosavage.baseplugin.ItemBuilder
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

class IslandQuestGUI :
    BaseGUI(Config.islandQuestGUITitle, Config.islandQuestGUIBackgroundItem, Config.islandQuestGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = ArrayList<GuiItem>()
        for (item in 0 until (Config.islandCreateGUIRows * 9)) {
            guiItems.add(GuiItem(super.backgroundItem.buildItem()) { e -> e.isCancelled = true })
        }

        for (quest in Config.islandQuests) {
            Bukkit.broadcastMessage(quest.name)
            guiItems[quest.guiDisplayIndex] =
                (GuiItem(buildItem(context.getIsland()!!, quest.guiDisplayItem, quest)) { e ->
                    run {
                        e.isCancelled = true
                        val player = e.whoClicked as Player
                        context.getIsland()!!.currentQuest = quest.name
                        val islandQuestGUI = IslandQuestGUI()
                        islandQuestGUI.showGui(player)
                        player.sendMessage("Quest activation trigger. -> Change this")
                    }
                })
            pane.populateWithGuiItems(guiItems)
        }
    }

    private fun buildItem(island: Island, serializableItem: SerializableItem, quest: Quest): ItemStack {
        val lore = ArrayList<String>()
        for (line in serializableItem.lore) {
            lore.add(
                line
                    .replace(
                        "{currentAmount}",
                        DecimalFormat.getInstance().format(island.getQuestCompletedAmount(quest.name))
                    )
                    .replace("{finalAmount}", DecimalFormat.getInstance().format(quest.amountTillComplete))
            )
        }

        return ItemBuilder(serializableItem.material.parseItem()).amount(serializableItem.amt).lore(lore)
            .glowing(island.currentQuest == quest.name).build()
    }


}
