package net.savagelabs.skyblockx.persist

import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.savagepluginx.persist.container.FlatDataContainer

class Data : FlatDataContainer {

    @JsonIgnore
    override val name = "data"


    companion object {
        lateinit var instance: Data
    }

    var nextIslandID = 0

    var IPlayers = HashMap<String, IPlayer>()
    var islands = HashMap<Int, Island>()





}
