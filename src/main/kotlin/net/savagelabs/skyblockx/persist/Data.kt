package net.savagelabs.skyblockx.persist

import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.savagepluginx.persist.container.FlatDataContainer
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import java.util.*
import kotlin.collections.HashMap

class Data : FlatDataContainer {

    @JsonIgnore
    override val name = "data"


    companion object {
        lateinit var instance: Data
    }

    var nextIslandID = 0

    var IPlayers = HashMap<UUID, IPlayer>()
    var islands = HashMap<Int, Island>()


}
