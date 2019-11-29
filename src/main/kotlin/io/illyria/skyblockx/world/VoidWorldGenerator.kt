package io.illyria.skyblockx.world

import io.illyria.skyblockx.persist.Config
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import java.util.*

class VoidWorldGenerator : ChunkGenerator() {

    override fun getDefaultPopulators(world: World): List<BlockPopulator> {
        return emptyList()
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return true
    }

    fun generate(world: World, rand: Random, chunkx: Int, chunkz: Int): ByteArray {
        return ByteArray(world.getMaxHeight() / 16)
    }

    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        val createChunkData = createChunkData(world)
        for (cx in 0..15) {
            for (cz in 0..15) {
                biome.setBiome(cx, cz, Config.skyblockWorldBiome)
            }
        }
        return createChunkData
    }



    override fun getFixedSpawnLocation(world: World, random: Random): Location {
        return Location(world, 0.toDouble(), 128.toDouble(), 0.toDouble())
    }

}