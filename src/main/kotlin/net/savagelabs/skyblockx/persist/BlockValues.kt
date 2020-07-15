package net.savagelabs.skyblockx.persist

import com.cryptomorin.xseries.XMaterial
import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.savagepluginx.persist.container.ConfigContainer
import org.bukkit.entity.EntityType

class BlockValues : ConfigContainer {

	@JsonIgnore
	override val name = "blockvalues"


	companion object {
		lateinit var instance: BlockValues
	}

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
		EntityType.PIG to 10000.0,
		EntityType.ZOMBIE to 15000.0,
		EntityType.CREEPER to 20000.0,
		EntityType.IRON_GOLEM to 100000.0,
		EntityType.SPIDER to 15000.0
	)


//    fun load() {
//        XMaterial.values().toList().forEach{ xmat -> instance.putIfAbsent(xmat, 0.0) }
//        EntityType.values().forEach {
//            spawnerValues.putIfAbsent(it, 1000.0)
//        }
//        Serializer().load(instance, BlockValues::class.java, "blockvalues")
//
//    }

}