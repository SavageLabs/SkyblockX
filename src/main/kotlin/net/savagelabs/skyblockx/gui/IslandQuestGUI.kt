package net.savagelabs.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.persist.Quests
import io.illyria.skyblockx.quest.Quest
import net.prosavage.baseplugin.ItemBuilder
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

class IslandQuestGUI :
    BaseGUI(Quests.islandQuestGUITitle, Quests.islandQuestGUIBackgroundItem, Quests.islandQuestGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = buildFullBackgroundItemlist()
        for (quest in Quests.islandQuests) {
            guiItems[quest.guiDisplayIndex] =
                GuiItem(buildItem(context.getIsland()!!, quest.guiDisplayItem, quest)) { e ->
                    run {
                        e.isCancelled = true
                        val player = e.whoClicked as Player
                        if (quest.oneTime && context.getIsland()!!.isOneTimeQuestAlreadyCompleted(quest.id)) {
                            player.sendMessage(color(Message.questIsOneTimeAndAlreadyCompleted))
                            return@run
                        }
                        context.getIsland()!!.currentQuest = quest.id
                        val islandQuestGUI = IslandQuestGUI()
                        islandQuestGUI.showGui(player)
                        player.sendMessage(color(Message.questActivationTrigger.replace("{quest}", quest.id)))
                        quest.executeActivationTrigger(context)
                    }
                }
        }
        pane.populateWithGuiItems(guiItems)
    }

    private fun buildItem(island: Island, serializableItem: SerializableItem, quest: Quest): ItemStack {
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

        return ItemBuilder(serializableItem.material.parseItem())
            .name(serializableItem.name)
            .amount(serializableItem.amt).lore(lore)
            .glowing(island.currentQuest == quest.id)
            .build()
    }

    private fun getProgressPlaceholder(island: Island, quest: Quest): String {
        if (quest.id == island.currentQuest) return color(Message.questInProgressPlaceholder)
        return if (island.isOneTimeQuestAlreadyCompleted(quest.id)) color(Message.questCompletedPlaceholder) else color(
            Message.questNotStarted
        )
    }

}
