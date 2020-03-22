package net.savagelabs.skyblockx.persist

import io.illyria.skyblockx.quest.Quest
import io.illyria.skyblockx.quest.QuestActions
import io.illyria.skyblockx.quest.QuestGoal
import net.prosavage.baseplugin.XMaterial
import net.prosavage.baseplugin.serializer.Serializer
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

object Quests {
    @Transient
    private val instance = this

    var islandQuestGUITitle = "&bIsland Quests"

    var islandQuestGUIBackgroundItem = SerializableItem(XMaterial.BLACK_STAINED_GLASS_PANE, "&9", listOf(), 1)

    var islandQuestGUIRows: Int = 3

    var questOrder = listOf(
        "Quest-1",
        "Quest-2",
        "Quest-3",
        "Quest-4",
        "Quest-5",
        "Quest-6",
        "Quest-7",
        "Quest-8",
        "Quest-9",
        "Quest-10",
        "Quest-11",
        "Quest-12",
        "Quest-13",
        "Quest-14",
        "Quest-15",
        "Quest-16",
        "Quest-17",
        "Quest-18",
        "Quest-19"
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
            0,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.OAK_LOG.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)",
                    "command(give {player} oak_sapling 1:::give {player} apple 1)"
                )
            )
        ),
        Quest(
            "Quest-2",
            "Craft a wooden pickaxe",
            SerializableItem(
                XMaterial.WOODEN_PICKAXE,
                "&bCraft a Wooden Pickaxe",
                listOf("&7Put three planks and 2 sticks in a crafting table.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            1,
            QuestGoal.CRAFT,
            XMaterial.WOODEN_PICKAXE.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-3",
            "Make a ore generator",
            SerializableItem(
                XMaterial.LAVA_BUCKET,
                "&8Create an Ore Generator",
                listOf("&7Mine &b3 &7cobblestone from Ore Generator", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            2,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.COBBLESTONE.name,
            2,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-4",
            "Craft a Stone Pickaxe",
            SerializableItem(
                XMaterial.STONE_PICKAXE,
                "&8Craft a stone pickaxe.",
                listOf("&7Craft a Stone Pickaxe", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            3,
            QuestGoal.CRAFT,
            XMaterial.STONE_PICKAXE.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-5",
            "Mine 3 Iron Ore",
            SerializableItem(
                XMaterial.IRON_ORE,
                "&8Mine 3 Iron Ore from the Ore Generator.",
                listOf("&7Mine &b3 Iron Ore.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            4,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.IRON_ORE.name,
            3,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-6",
            "Craft an Furnace",
            SerializableItem(
                XMaterial.FURNACE,
                "&8Craft a Furnace",
                listOf("&7Place 8 cobblestone blocks in the crafting table.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            5,
            QuestGoal.CRAFT,
            XMaterial.FURNACE.name,
            8,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)",
                    "command(&7give {player} coal 1)"
                )
            )
        ),
        Quest(
            "Quest-7",
            "Smelt Iron Ingot",
            SerializableItem(
                XMaterial.IRON_INGOT,
                "&8Smelt Iron Ingot",
                listOf("&7Smelt 3 Iron Ingots in a furnace.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            6,
            QuestGoal.SMELT,
            XMaterial.IRON_INGOT.name,
            3,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-8",
            "Craft an &bIron Pickaxe",
            SerializableItem(
                XMaterial.IRON_PICKAXE,
                "&8Craft an Iron Pickaxe",
                listOf("&7Place three Iron Ingots and 2 Sticks into a crafting table.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            7,
            QuestGoal.CRAFT,
            XMaterial.IRON_PICKAXE.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-9",
            "Plant a Sapling",
            SerializableItem(
                XMaterial.OAK_SAPLING,
                "&8Plant a sapling.",
                listOf("&7Plant the Oak Sapling in your Inventory.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            8,
            QuestGoal.PLACE_BLOCKS,
            XMaterial.OAK_SAPLING.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-10",
            "&8Get some sand.",
            SerializableItem(
                XMaterial.SAND,
                "&8Get some sand.",
                listOf("&7Dig into your island and find the sand.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            9,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.SAND.name,
            3,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-11",
            "&8Plant some Cactus.",
            SerializableItem(
                XMaterial.CACTUS,
                "&8Plant some cactus.",
                listOf("&7Plant some cactus on sand.", "&7You can find cactus in the island chest", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            10,
            QuestGoal.PLACE_BLOCKS,
            XMaterial.CACTUS.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-12",
            "&8Plant some sugarcane.",
            SerializableItem(
                XMaterial.SUGAR_CANE,
                "&8Plant some sugarcane.",
                listOf("&7Plant some sugarcane on sand.", "&7You can find some sugarcane in the island chest.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            11,
            QuestGoal.PLACE_BLOCKS,
            XMaterial.CACTUS.name,
            10,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-13",
            "&8Plant some melons.",
            SerializableItem(
                XMaterial.MELON_SLICE,
                "&8Plant some melons.",
                listOf("&7Plant some melons on tilled dirt.", "&7You can find some melon seeds in the island chest.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            12,
            QuestGoal.PLACE_BLOCKS,
            XMaterial.MELON_SEEDS.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-14",
            "&8Plant 20 Cacti.",
            SerializableItem(
                XMaterial.CACTUS,
                "&8Make a cactus farm.",
                listOf("&7Plant 20 cacti.", "&7You can buy more sand from /is shop.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            13,
            QuestGoal.PLACE_BLOCKS,
            XMaterial.CACTUS.name,
            20,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-15",
            "&8Plant 30 wheat seeds.",
            SerializableItem(
                XMaterial.WHEAT_SEEDS,
                "&8Make a wheat farm.",
                listOf("&7Plant some wheat on dirt.", "&7You can buy more dirt from /is shop.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            14,
            QuestGoal.PLACE_BLOCKS,
            XMaterial.WHEAT_SEEDS.name,
            30,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-16",
            "&8Harvest some wheat.",
            SerializableItem(
                XMaterial.WHEAT,
                "&8Harvest some wheat.",
                listOf("&7Harvest some fully grown wheat.", "&7You can find some pumpkin in the island chest.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            15,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.WHEAT.name,
            1,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-17",
            "&8Kill 100 Zombies.",
            SerializableItem(
                XMaterial.ZOMBIE_HEAD,
                "&8Kill Zombies.",
                listOf("&7Kill 100 Zombies.", "&7They will spawn in the dark.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            16,
            QuestGoal.KILL_MOBS,
            EntityType.ZOMBIE.name,
            100,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-18",
            "&8Kill 100 Skeleton.",
            SerializableItem(
                XMaterial.SKELETON_SKULL,
                "&8Kill Skeletons.",
                listOf("&7Kill 100 Skeletons.", "&7They will spawn in the dark.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            17,
            QuestGoal.KILL_MOBS,
            EntityType.SKELETON.name,
            100,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "title(&7{quest-name}:::&b&lQuest Completed)"
                )
            )
        ),
        Quest(
            "Quest-19",
            "&8Kill 100 Creepers.",
            SerializableItem(
                XMaterial.CREEPER_HEAD,
                "&8Kill Creepers.",
                listOf("&7Kill 100 Creepers.", "&7They will spawn in the dark.", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            16,
            QuestGoal.KILL_MOBS,
            EntityType.CREEPER.name,
            100,
            true,
            QuestActions(
                listOf(
                    "message(&7You have started the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
                    "actionbar(&7Your quest progress is 0/0/{quest-amount-till-complete})"
                )
            ),
            QuestActions(
                listOf(
                    "message(&7You have finished the {quest-name}:::&7Your current progress is 0/{quest-amount-till-complete})",
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