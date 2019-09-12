package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.serializer.Serializer
import net.savagellc.savageskyblock.Globals

object Config {

    @Transient
    private val instance = this

    @Transient
    val skyblockPermissionPrefix = "savageskyblock"

    var islandMaxSizeInBlocks = 100

    var islandPaddingSizeInBlocks = 20




    var skyblockWorldName = "savageskyblock"

    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Config::class.java, "config")
    }
}