package net.savagellc.savageskyblock.gui

import com.github.stefvanschie.inventoryframework.GuiItem
import net.prosavage.baseplugin.ItemBuilder
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import net.savagellc.savageskyblock.core.*
import net.savagellc.savageskyblock.goal.Quest
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat

class IslandQuestGUI :
    BaseGUI(Config.islandQuestGUITitle, Config.islandQuestGUIBackgroundItem, Config.islandQuestGUIRows) {

    fun makeGUI(iPlayer: IPlayer) {
        // Builds the base GUI
        super.buildGui()

        val guiItems = ArrayList<GuiItem>()
        for (item in 0 until (Config.islandCreateGUIRows * 9)) {
            guiItems.add(GuiItem(super.backgroundItem.buildItem()) { e -> e.isCancelled = true })
        }
        for (quest in Config.islandQuests) {
            guiItems[quest.guiDisplayIndex] = (GuiItem(buildItem(iPlayer.getIsland()!!, quest.guiDisplayItem, quest)) { e ->
                run {
                    e.isCancelled = true
                    val player = e.whoClicked as Player
                    iPlayer.getIsland()!!.currentQuest = quest.name
                    val islandQuestGUI = IslandQuestGUI()
                    islandQuestGUI.makeGUI(iPlayer)
                    islandQuestGUI.showGui(player)
                    player.sendMessage("Quest activation trigger. -> Change this")
                }
            })
            super.pane.populateWithGuiItems(guiItems)
            super.gui.addPane(super.pane)
            gui.addPane(super.pane)
            gui.update()
        }
    }

    fun buildItem(island: Island, serializableItem: SerializableItem, quest: Quest): ItemStack {
        val lore = ArrayList<String>()
        for (line in serializableItem.lore) {
            lore.add(line
                .replace("{currentAmount}", DecimalFormat.getInstance().format(island.getQuestCompletedAmountForMission(quest.name)))
                .replace("{finalAmount}", DecimalFormat.getInstance().format(quest.amountTillComplete)))
        }
        return ItemBuilder(serializableItem.material.parseItem()).amount(serializableItem.amt).lore(lore).glowing(island.currentQuest == quest.name).build()
    }


}
