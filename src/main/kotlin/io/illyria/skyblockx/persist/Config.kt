package io.illyria.skyblockx.persist

import io.illyria.skyblockx.persist.data.IslandCreateInfo
import io.illyria.skyblockx.quest.Quest
import io.illyria.skyblockx.quest.QuestGoal
import net.prosavage.baseplugin.XMaterial
import net.prosavage.baseplugin.serializer.Serializer
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.entity.EntityType

object Config {

    @Transient
    private val instance = this

    @Transient
    val skyblockPermissionPrefix = "skyblockx"

    var islandMaxSizeInBlocks = 100

    var islandPaddingSizeInBlocks = 20

    var skyblockWorldName = "skyblockx"

    var preventFallingDeaths = true

    var defaultMaxCoopPlayers = 3

    var defaultMaxIslandHomes = 3

    var helpGeneratorPageEntries = 4

    // TODO: Actually use GUI, also create island structure requirements.
    var islandCreateGUITitle = "Choose an island!"
    var islandCreateGUIRows = 1
    var islandCreateGUIBackgroundItem = SerializableItem(XMaterial.BLACK_STAINED_GLASS_PANE, "&9", listOf(""), 1)
    var islandCreateGUIIslandTypes = listOf(
        IslandCreateInfo(
            "normal",
            "savageskyblock.islands.default", 2, SerializableItem(
                XMaterial.GRASS_BLOCK,
                "&aBasic Island",
                listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
                1
            ), "island.structure"
        ),
        IslandCreateInfo(
            "bedrock",
            "savageskyblock.islands.bedrock", 6, SerializableItem(
                XMaterial.BEDROCK,
                "&aBedrock Island",
                listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
                1
            ), "island.structure"
        )
    )

    var islandQuestGUITitle = "&bIsland Quests"

    var islandQuestGUIBackgroundItem = SerializableItem(XMaterial.BLACK_STAINED_GLASS_PANE, "&9", listOf(), 1)

    var islandQuestGUIRows = 1


    var islandQuests = listOf(
        Quest(
            "Mine-Cobblestone-1",
            SerializableItem(
                XMaterial.COBBLESTONE,
                "&8Mine-Cobblestone (Tier 1)",
                listOf("&7Mine &b10 &7cobblestone", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            0,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.COBBLESTONE.name,
            10,
            listOf("broadcast {player} completed the Tier 1 Cobblestone Mining Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Mine-Cobblestone-2",
            SerializableItem(
                XMaterial.COBBLESTONE,
                "&8Mine-Cobblestone (Tier 2)",
                listOf("&7Mine &b100,000 &7cobblestone", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            1,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.COBBLESTONE.name,
            100000,
            listOf("broadcast {player} completed the Tier 2 Cobblestone Mining Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Mine-Cobblestone-3",
            SerializableItem(
                XMaterial.COBBLESTONE,
                "&8Mine-Cobblestone (Tier 3)",
                listOf("&7Mine &b1,000,000 &7cobblestone", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            2,
            QuestGoal.BREAK_BLOCKS,
            XMaterial.COBBLESTONE.name,
            1000000,
            listOf("broadcast {player} completed the Tier 3 Cobblestone Mining Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Kill-Creeper-1",
            SerializableItem(
                XMaterial.CREEPER_SPAWN_EGG,
                "&8Kill Creepers (Tier 1)",
                listOf("&7Kill &b10 &7creepers", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            3,
            QuestGoal.KILL_MOBS,
            EntityType.CREEPER.name,
            10,
            listOf("broadcast {player} completed the Tier 1 Creeper Killing Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Kill-Creeper-2",
            SerializableItem(
                XMaterial.CREEPER_SPAWN_EGG,
                "&8Kill Creepers (Tier 2)",
                listOf("&7Kill &b100 &7creepers", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            4,
            QuestGoal.KILL_MOBS,
            EntityType.CREEPER.name,
            100,
            listOf("broadcast {player} completed the Tier 2 Creeper Killing Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Kill-Creeper-3",
            SerializableItem(
                XMaterial.CREEPER_SPAWN_EGG,
                "&8Kill Creepers (Tier 3)",
                listOf("&7Kill &b1,000 &7creepers", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            5,
            QuestGoal.KILL_MOBS,
            EntityType.CREEPER.name,
            1000,
            listOf("broadcast {player} completed the Tier 3 Creeper Killing Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Craft-Diamond-Pickaxe-1",
            SerializableItem(
                XMaterial.DIAMOND_PICKAXE,
                "&8Craft Diamond Pickaxe (Tier 1)",
                listOf("&7Craft &b1 &7Diamond Pickaxe", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            6,
            QuestGoal.CRAFT,
            XMaterial.DIAMOND_PICKAXE.name,
            1,
            listOf("broadcast {player} completed the Tier 1 Diamond Pickaxe Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Craft-Diamond-Sword-1",
            SerializableItem(
                XMaterial.DIAMOND_SWORD,
                "&8Craft Diamond Sword (Tier 1)",
                listOf("&7Craft &b1 &7Diamond Sword", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            7,
            QuestGoal.CRAFT,
            XMaterial.DIAMOND_SWORD.name,
            1,
            listOf("broadcast {player} completed the Tier 1 Diamond Sword Quest.", "give {player} diamond 1")
        ),
        Quest(
            "Craft-Diamond-Shovel-1",
            SerializableItem(
                XMaterial.DIAMOND_SHOVEL,
                "&8Craft Diamond Shovel (Tier 1)",
                listOf("&7Craft &b1 &7Diamond Shovel", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            8,
            QuestGoal.CRAFT,
            XMaterial.DIAMOND_SHOVEL.name,
            1,
            listOf("broadcast {player} completed the Tier 1 Diamond Shovel Quest.", "give {player} diamond 1")
        )
    )


    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Config::class.java, "config")
    }
}