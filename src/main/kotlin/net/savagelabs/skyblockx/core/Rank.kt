package net.savagelabs.skyblockx.core

import net.savagelabs.skyblockx.persist.Config

enum class Rank(val weight: Int) {

    RECRUIT(0),
    MEMBER(1),
    MOD(2),
    OWNER(3);

    val data: Data
        get() {
            return Config.instance.islandRanks[this]!!
        }

    fun next() = getByWeight(weight + 1)

    fun previous() = getByWeight(weight - 1)

    data class Data(val identifier: String)

}

fun generateDefaultRankMap(): HashMap<Rank, Rank.Data> {
    val map = hashMapOf<Rank, Rank.Data>()

    for(i in Rank.values().indices) {
        val rank = Rank.values()[i]

        map[rank] = Rank.Data(rank.name.split("_").joinToString(" ").toLowerCase().capitalize())
    }

    return map
}

fun getByWeight(id: Int) = when(id) {
    0 -> Rank.RECRUIT
    1 -> Rank.MEMBER
    2 -> Rank.MOD
    3 -> Rank.OWNER
    else -> null
}
