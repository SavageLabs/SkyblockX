package net.savagelabs.skyblockx.placeholder.impl

import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.placeholder.Placeholder

/**
 * This class is the [Island.ChestShop] implementation of [Placeholder].
 */
internal class ChestShopPlaceholder : Placeholder<Island.ChestShop> {
    /**
     * [Char] the start character for chest shop placeholders.
     */
    override val startCharacter: Char = '{'

    /**
     * [Char] the end character for chest shop placeholders.
     */
    override val endCharacter: Char = '}'

    /**
     * Process the available chest shop placeholders.
     */
    override fun process(value: Island.ChestShop, identifier: String, vararg extra: Any): String = when (identifier) {
        "player" -> value.islandPlayerOfOwner?.name ?: "Unknown"
        "material" -> value.material.toString()
        "price" -> value.price.toString()
        "amount" -> value.amount.toString()
        else -> "$startCharacter$identifier$endCharacter"
    }
}