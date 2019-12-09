package io.illyria.skyblockx.persist

import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import net.prosavage.baseplugin.serializer.Serializer

object Data {

    @Transient
    private val instance = this

    var nextIslandID = 0

    var IPlayers = HashMap<String, IPlayer>()

    var islands = HashMap<Int, Island>()


    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Data::class.java, "data")
    }


}
