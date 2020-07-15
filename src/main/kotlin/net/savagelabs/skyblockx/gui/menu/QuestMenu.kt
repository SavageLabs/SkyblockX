package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.gui.PagedMenu
import net.savagelabs.skyblockx.gui.PagedMenuConfig
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.persist.data.SerializableItem
import net.savagelabs.skyblockx.quest.Quest
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.DecimalFormat


class QuestMenu(val iPlayer: IPlayer, val island: Island) : PagedMenu(
	PagedMenuConfig(
		Quests.instance.islandQuestGUITitle,
		Quests.instance.islandQuestGUIBackgroundItem,
		Quests.instance.islandQuestGUIRows,
		SlotIterator.Type.HORIZONTAL,
		Quests.instance.islandQuestGUIStartCoordinate,
		Quests.instance.islandQuestGUIItemsPerPage,
		Quests.instance.questsNextPageItem,
		Quests.instance.questPreviousPageItem,
		Quests.instance.questMenuItems
	)
) {
	override fun getPageItems(): List<ClickableItem> {
		return Quests.instance.islandQuests.map { quest ->
			ClickableItem.of(buildItem(island, quest.guiDisplayItem, quest)) { e ->
				val player = e.whoClicked as Player
				if (quest.oneTime && island.isOneTimeQuestAlreadyCompleted(quest.id)) {
					player.sendMessage(color(Message.instance.questIsOneTimeAndAlreadyCompleted))
					return@of
				}
				island.currentQuest = quest.id
				buildMenu(
					QuestMenu(
						iPlayer,
						island
					)
				).open(iPlayer.getPlayer())
				player.sendMessage(color(Message.instance.questActivationTrigger.replace("{quest}", quest.id)))
				quest.executeActivationTrigger(iPlayer)
			}
		}
	}

	private fun buildItem(
		island: Island,
		serializableItem: SerializableItem,
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