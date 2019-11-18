package io.illyria.skyblockx.persist.data

import java.util.concurrent.ThreadLocalRandom

class Items<T>(val weightedItems: ArrayList<WeightedItem<T>>) {
    @Transient
    val neutralList = ArrayList<T>()


    init {
        setup()
    }

    fun setup() {
        neutralList.clear()
        for (weightedItem in weightedItems) {
            for (count in 1..weightedItem.weight) neutralList.add(weightedItem.value)
        }
    }

    fun choose(): T {
        return neutralList[ThreadLocalRandom.current().nextInt(0, neutralList.size - 1)]
    }


}

class WeightedItem<T>(val value: T, val weight: Int)
