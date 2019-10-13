package io.illyria.skyblockx.world

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import java.util.*

class VoidWorldGenerator : ChunkGenerator() {

    override fun getDefaultPopulators(world: World): List<BlockPopulator> {
        return Arrays.asList(*arrayOfNulls(0))
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return true
    }

    fun generate(world: World, rand: Random, chunkx: Int, chunkz: Int): ByteArray {
        return ByteArray(32768)
    }

    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        return createChunkData(world)
    }


    override fun getFixedSpawnLocation(world: World, random: Random): Location {
        return Location(world, 0.toDouble(), 128.toDouble(), 0.toDouble())
    }

}