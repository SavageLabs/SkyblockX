package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.XMaterial
import net.prosavage.baseplugin.serializer.Serializer
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem

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
    var islandCreateGUIRows = 9
    var islandCreateGUIBackgroundItem = SerializableItem(XMaterial.BLACK_STAINED_GLASS, "&9", listOf(""), 1)
    var islandCreateGUIIslandTypes = mapOf(
        "island.structure" to SerializableItem(
            XMaterial.GRASS_BLOCK,
            "&aBasic Island",
            listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
            1
        ),
        "other.structure" to SerializableItem(
            XMaterial.BEDROCK,
            "&aVoid Island",
            listOf(
                "&aThis is a harder island.",
                "&a - Start off with bedrock & a chest.",
                "&a - Chest has extra dirt and sand, along with a sapling."
            ),
            1
        )
    )


    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Config::class.java, "config")
    }
}