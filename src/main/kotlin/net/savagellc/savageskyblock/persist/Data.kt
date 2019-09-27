package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.serializer.Serializer
import net.savagellc.savageskyblock.core.IPlayer
import net.savagellc.savageskyblock.core.Island

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
