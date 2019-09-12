package net.savagellc.savageskyblock.world

import net.savagellc.savageskyblock.persist.Config
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.entity.Player


// Using a object cuz I dont want it to autocomplete it everything
object WorldManager {

    fun createVoidWorld(name: String = Config.skyblockWorldName) {
        val worldCreator = WorldCreator(name)
        worldCreator.generator(VoidWorldGenerator())
        Bukkit.createWorld(worldCreator)
    }


}