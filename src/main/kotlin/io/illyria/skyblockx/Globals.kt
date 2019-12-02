package io.illyria.skyblockx

import io.illyria.skyblockx.command.island.IslandBaseCommand
import io.illyria.skyblockx.command.skyblock.SkyblockBaseCommand
import io.illyria.skyblockx.persist.data.Items
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.XMaterial

object Globals {
    lateinit var skyblockX: SkyblockX
    lateinit var islandBaseCommand: IslandBaseCommand
    lateinit var skyblockBaseCommand: SkyblockBaseCommand
    lateinit var worldBorderUtil: WorldBorderUtil
    lateinit var generatorAlgorithm: Map<Int, Items<XMaterial>>

}