package net.savagelabs.skyblockx

import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.command.skyblock.SkyblockBaseCommand
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.IslandTopInfo
import io.illyria.skyblockx.persist.data.Items
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.XMaterial

object Globals {
    lateinit var skyblockX: _root_ide_package_.net.savagelabs.skyblockx.SkyblockX
    lateinit var islandBaseCommand: _root_ide_package_.net.savagelabs.skyblockx.command.island.IslandBaseCommand
    lateinit var skyblockBaseCommand: _root_ide_package_.net.savagelabs.skyblockx.command.skyblock.SkyblockBaseCommand
    lateinit var worldBorderUtil: WorldBorderUtil
    lateinit var generatorAlgorithm: Map<Int, Items<XMaterial>>
    var islandValues: IslandTopInfo? = null

}