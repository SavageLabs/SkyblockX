package io.illyria.skyblockx.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.quest.Quest
import net.prosavage.baseplugin.ItemBuilder
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

class IslandQuestGUI :
    BaseGUI(Config.islandQuestGUITitle, Config.islandQuestGUIBackgroundItem, Config.islandQuestGUIRows) {

    override fun populatePane(context: IPlayer) {
        val guiItems = ArrayList<GuiItem>()
        for (item in 0 until (super.guiRows * 9)) {
            guiItems.add(GuiItem(super.backgroundItem.buildItem()) { e -> e.isCancelled = true })
        }
        for (quest in Config.islandQuests) {
            guiItems[quest.guiDisplayIndex] =
                (GuiItem(buildItem(context.getIsland()!!, quest.guiDisplayItem, quest)) { e ->
                    run {
                        e.isCancelled = true
                        val player = e.whoClicked as Player
                        context.getIsland()!!.currentQuest = quest.id
                        val islandQuestGUI = IslandQuestGUI()
                        islandQuestGUI.showGui(player)
                        player.sendMessage(color(Message.questActivationTrigger
                            .replace("{quest}", quest.id)))
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
                        DecimalFormat.getInstance().format(island.getQuestCompletedAmount(quest.id))
                    )
                    .replace("{finalAmount}", DecimalFormat.getInstance().format(quest.amountTillComplete))
            )
        }

        return ItemBuilder(serializableItem.material.parseItem())
            .name(serializableItem.name)
            .amount(serializableItem.amt).lore(lore)
            .glowing(island.currentQuest == quest.id)
            .build()
    }


}
