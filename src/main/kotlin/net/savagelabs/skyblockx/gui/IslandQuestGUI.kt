package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.Quest
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

class IslandQuestGUI :
    BaseGUI(Quests.instance.islandQuestGUITitle, Quests.instance.islandQuestGUIBackgroundItem, Quests.instance.islandQuestGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        for (quest in Quests.instance.islandQuests) {
            guiItems[quest.guiDisplayIndex] =
                GuiItem(buildItem(context.getIsland()!!, quest.guiDisplayItem, quest)) { e ->
                    run {
                        e.isCancelled = true
                        val player = e.whoClicked as Player
                        if (quest.oneTime && context.getIsland()!!.isOneTimeQuestAlreadyCompleted(quest.id)) {
                            player.sendMessage(color(Message.instance.questIsOneTimeAndAlreadyCompleted))
                            return@run
                        }
                        context.getIsland()!!.currentQuest = quest.id
                        val islandQuestGUI = IslandQuestGUI()
                        islandQuestGUI.showGui(player)
                        player.sendMessage(color(Message.instance.questActivationTrigger.replace("{quest}", quest.id)))
                        quest.executeActivationTrigger(context)
                    }
                }
        }
        pane.populateWithGuiItems(guiItems)
    }

    private fun buildItem(
        island: Island,
        serializableItem: net.savagelabs.skyblockx.persist.data.SerializableItem,
        quest: Quest
    ): ItemStack {
        val lore = ArrayList<String>()
        for (line in serializableItem.lore) {
            lore.add(
                line
                    .replace(
                        "{currentAmount}",
                        DecimalFormat.getInstance().format(island.getQuestCompletedAmount(quest.id))
                    )
                    .replace("{finalAmount}", DecimalFormat.getInstance().format(quest.amountTillComplete))
                    .replace("{progress}", getProgressPlaceholder(island, quest))
            )
        }

        return ItemBuilder(serializableItem.material.parseItem()!!)
            .name(serializableItem.name)
            .amount(serializableItem.amt).lore(lore)
            .glowing(island.currentQuest == quest.id)
            .build()
    }

    private fun getProgressPlaceholder(island: Island, quest: Quest): String {
        if (quest.id == island.currentQuest) return color(Message.instance.questInProgressPlaceholder)
        return if (island.isOneTimeQuestAlreadyCompleted(quest.id)) color(Message.instance.questCompletedPlaceholder) else color(
            Message.instance.questNotStarted
        )
    }

}
