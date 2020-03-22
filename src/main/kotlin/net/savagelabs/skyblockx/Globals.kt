package net.savagelabs.skyblockx

import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.command.skyblock.SkyblockBaseCommand
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.IslandTopInfo
import net.savagelabs.skyblockx.persist.data.Items
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.XMaterial

object Globals {
    lateinit var skyblockX: SkyblockX
    lateinit var islandBaseCommand: IslandBaseCommand
    lateinit var skyblockBaseCommand: SkyblockBaseCommand
    lateinit var worldBorderUtil: WorldBorderUtil
    lateinit var generatorAlgorithm: Map<Int, Items<XMaterial>>
    var islandValues: IslandTopInfo? = null

}