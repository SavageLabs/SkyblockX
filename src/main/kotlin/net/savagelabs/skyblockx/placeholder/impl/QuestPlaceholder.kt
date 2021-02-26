package net.savagelabs.skyblockx.placeholder.impl

import net.savagelabs.skyblockx.placeholder.Placeholder
import net.savagelabs.skyblockx.quest.QuestContext

/**
 * This class is the [QuestContext] implementation of [Placeholder].
 */
internal class QuestPlaceholder : Placeholder<QuestContext> {
    /**
     * [Char] the start character for quest placeholders.
     */
    override val startCharacter: Char = '{'

    /**
     * [Char] the end character for quest placeholders.
     */
    override val endCharacter: Char = '}'

    /**
     * Process the available quest placeholders.
     */
    override fun process(value: QuestContext, identifier: String, vararg extra: Any): String {
        // necessity
        val player = value.contextIPlayer
        val quest = value.quest

        // return the corresponding value
        return when (identifier) {
            "player" -> player.name
            "uuid" -> player.uuid.toString()
            "quest-name" -> quest.name
            "quest-amount-till-complete" -> quest.amountTillComplete.toString()
            else -> "$startCharacter$identifier$endCharacter"
        }
    }
}