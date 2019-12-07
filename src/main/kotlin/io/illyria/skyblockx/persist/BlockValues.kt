package io.illyria.skyblockx.persist

import net.prosavage.baseplugin.XMaterial
import net.prosavage.baseplugin.serializer.Serializer

object BlockValues {

    @Transient
    private val instance = this

    var blockValues = mapOf(
        XMaterial.GRASS_BLOCK to 3.0,
        XMaterial.DIRT to 1.0,
        XMaterial.DIAMOND_BLOCK to 100.0,
        XMaterial.GOLD_BLOCK to 50.0,
        XMaterial.IRON_BLOCK to 25.0,
        XMaterial.LAPIS_BLOCK to 20.0,
        XMaterial.COAL_BLOCK to 10.0,
        XMaterial.SPAWNER to 1000.0
    )



    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, BlockValues::class.java, "blockvalues")
    }
}