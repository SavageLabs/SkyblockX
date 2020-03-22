package net.savagelabs.skyblockx.persist

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
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
