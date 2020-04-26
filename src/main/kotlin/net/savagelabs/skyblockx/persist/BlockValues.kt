package net.savagelabs.skyblockx.persist

import com.cryptomorin.xseries.XMaterial
import net.prosavage.baseplugin.serializer.Serializer
import org.bukkit.entity.EntityType

object BlockValues {

    @Transient
    private val instance = this

    var blockValues = mutableMapOf(
        XMaterial.GRASS_BLOCK to 3.0,
        XMaterial.DIRT to 1.0,
        XMaterial.DIAMOND_BLOCK to 100.0,
        XMaterial.GOLD_BLOCK to 50.0,
        XMaterial.IRON_BLOCK to 25.0,
        XMaterial.LAPIS_BLOCK to 20.0,
        XMaterial.COAL_BLOCK to 10.0,
        XMaterial.SPAWNER to 1000.0
    )

    var spawnerValues = mutableMapOf(
        EntityType.CREEPER to 100000.0,
        EntityType.ZOMBIE to 10000.0,
        EntityType.SKELETON to 50000.0
    )

    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        XMaterial.values().forEach { xmat -> blockValues.putIfAbsent(xmat, 0.0) }
        EntityType.values().forEach { entityType -> spawnerValues.putIfAbsent(entityType, 0.0) }
        Serializer().load(instance, BlockValues::class.java, "blockvalues")
    }
}