package io.illyria.skyblockx

import io.illyria.skyblockx.command.island.BaseCommand
import io.illyria.skyblockx.persist.data.Items
import net.prosavage.baseplugin.WorldBorderUtil
import net.prosavage.baseplugin.XMaterial

object Globals {
    lateinit var skyblockX: SkyblockX
    lateinit var baseCommand: BaseCommand
    lateinit var worldBorderUtil: WorldBorderUtil
    lateinit var generatorAlgorithm: Map<Int, Items<XMaterial>>

}