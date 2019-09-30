package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.XMaterial
import net.prosavage.baseplugin.serializer.Serializer
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import net.savagellc.savageskyblock.goal.Quest
import net.savagellc.savageskyblock.goal.QuestGoal
import net.savagellc.savageskyblock.persist.data.IslandCreateInfo

object Config {

    @Transient
    private val instance = this

    @Transient
    val skyblockPermissionPrefix = "savageskyblock"

    var islandMaxSizeInBlocks = 100

    var islandPaddingSizeInBlocks = 20

    var skyblockWorldName = "savageskyblock"

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
            "savageskyblock.islands.default", 2, SerializableItem(
                XMaterial.GRASS_BLOCK,
                "&aBasic Island",
                listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
                1
            ), "island.structure"
        ),
        IslandCreateInfo(
            "savageskyblock.islands.bedrock", 6, SerializableItem(
                XMaterial.BEDROCK,
                "&aBedrock Island",
                listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
                1
            ), "island.structure"
        )
    )

    val islandQuestGUITitle = "&bIsland Quests"

    val islandQuestGUIBackgroundItem = SerializableItem(XMaterial.BLACK_STAINED_GLASS_PANE, "&9", listOf(), 1)

    val islandQuestGUIRows = 1

    val islandQuests = listOf(
        Quest(
            "Mine-Cobblestone-1",
            SerializableItem(
                XMaterial.COBBLESTONE,
                "&8Mine-Cobblestone (Tier 1)",
                listOf("&7Mine &b10 &7cobblestone", "&7Completion: &b{currentAmount}&7/&b{finalAmount}"),
                1
            ),
            0,
            QuestGoal.MINE_BLOCKS,
            XMaterial.COBBLESTONE.name,
            0,
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
            QuestGoal.MINE_BLOCKS,
            XMaterial.COBBLESTONE.name,
            0,
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
            QuestGoal.MINE_BLOCKS,
            XMaterial.COBBLESTONE.name,
            0,
            1000000,
            listOf("broadcast {player} completed the Tier 3 Cobblestone Mining Quest.", "give {player} diamond 1")
        )
    )


    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Config::class.java, "config")
    }
}