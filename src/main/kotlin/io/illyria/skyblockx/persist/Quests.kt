package io.illyria.skyblockx.persist

import io.illyria.skyblockx.quest.Quest
import io.illyria.skyblockx.quest.QuestActions
import io.illyria.skyblockx.quest.QuestGoal
import net.prosavage.baseplugin.XMaterial
import net.prosavage.baseplugin.serializer.Serializer
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem

object Quests {
    @Transient
    private val instance = this

    var islandQuestGUITitle = "&bIsland Quests"

    var islandQuestGUIBackgroundItem = SerializableItem(XMaterial.BLACK_STAINED_GLASS_PANE, "&9", listOf(), 1)

    var islandQuestGUIRows: Int = 3

    var questOrder = listOf(
        "Quest-1",
        "Quest-2",
        "Quest-3"
    )

    var useQuestOrder = true
    var sendNextQuestInOrderMessages = true

    var islandQuests = listOf(
        Quest(
            "Quest-1",
            "Get wood from your island's tree",
            SerializableItem(
                XMaterial.OAK_WOOD,
                "&bGet wood from island's tree",
                listOf(
                    "&7Break the wood from your island's tree trunk",
                    "&7Completion: &b{currentAmount}&7/&b{finalAmount}"
                ),
                1
            ),
            14,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.OAK_LOG.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is {quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is {quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress {quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)",
                    "command(give {player} oak_sapling 1:::give {player} apple 1)"
                )
            )
        ),
        Quest(
            "Quest-2",
            "Craft wooden planks",
            SerializableItem(
                XMaterial.OAK_PLANKS,
                "&bCraft some wooden planks",
                listOf("&7Put a log in a crafting table", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            15,
            QuestGoal.CRAFT,
            XMaterial.OAK_PLANKS.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress {quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is {quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress {quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-3",
            "Mine-Cobblestone-1",
            SerializableItem(
                XMaterial.LAVA_BUCKET,
                "&8Create a Cobblestone generator and mine a block from it.",
                listOf("&7Mine &b1 &7cobblestone", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            0,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.COBBLESTONE.name,
            10,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is {quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is {quest-amount-completed}/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is {quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-4",
            "Mine 10 Cobblestone",
            SerializableItem(
                XMaterial.COBBLESTONE,
                "&8Create a Cobblestone generator and mine a block from it.",
                listOf("&7Mine &b1 &7cobblestone", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            0,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.COBBLESTONE.name,
            10,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is {quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is {quest-amount-completed}/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is {quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        )
    )

    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Quests::class.java, "quests")
    }
}