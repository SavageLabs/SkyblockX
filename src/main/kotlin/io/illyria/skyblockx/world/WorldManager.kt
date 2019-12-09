package io.illyria.skyblockx.world

import io.illyria.skyblockx.persist.Config
import org.bukkit.Bukkit
import org.bukkit.WorldCreator


// Using a object cuz I dont want it to autocomplete it everything
object WorldManager {

    fun createVoidWorld(name: String = Config.skyblockWorldName) {
        val worldCreator = WorldCreator(name)
        worldCreator.generator(VoidWorldGenerator())
        Bukkit.createWorld(worldCreator)
    }


}