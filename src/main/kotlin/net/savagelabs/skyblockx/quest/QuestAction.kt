package net.savagelabs.skyblockx.quest

import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.skyblockx.placeholder.impl.QuestPlaceholder
import net.savagelabs.skyblockx.util.colored
import net.savagelabs.skyblockx.util.withPlaceholders
import org.bukkit.Bukkit

/**
 * This object contains helper functions for quest actions.
 */
object QuestActionHelper {
    /**
     * [Regex] this regex is used to fetch the type of an action.
     */
    private val ACTION_TYPE_REGEX: Regex = Regex("(.+)\\(.+\\)")

    /**
     * [Regex] this regex is used to fetch the value of an action.
     */
    private val PARAMS_BASE_REGEX: Regex = Regex("\\((.+)\\)")

    /**
     * Execute all correspondent actions in a list.
     *
     * @param actions [List] collection of actions to be executed.
     * @param context [QuestContext] the context to maintain.
     */
    fun execute(actions: List<String>, context: QuestContext) = actions.forEach { action ->
        // fetch the type and value
        val type  = typeOf(action)
        val value = valueOf(context, action)

        // if the type or value is absent, then we'll ignore this action
        if (type.isEmpty() || value.isEmpty()) {
            return@forEach
        }

        // type and value are present so let's continue
        val player = context.contextIPlayer
        when (type.toLowerCase()) {
            "message" -> player.message(value.colored)
            "title" -> {
                val split = value.split("::")
                player.getPlayer()?.sendTitle(split.getOrNull(0)?.colored ?: "", split.getOrNull(1)?.colored ?: "")
            }
            "command" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value)
            "actionbar" -> JSONMessage.actionbar(value.colored, player.getPlayer())
        }
    }

    /**
     * Fetch the type of a quest action.
     *
     * @param action [String] the action to fetch the type from.
     * @return [String] the type collected from [action] if present, otherwise empty.
     */
    private fun typeOf(action: String): String {
        // necessity
        val matcher = ACTION_TYPE_REGEX.find(action)

        // return the found type
        return matcher?.groupValues?.getOrNull(0) ?: return ""
    }

    /**
     * Fetch the value of a quest action.
     *
     * @param context [QuestContext] the context related to this quest action; for placeholder purposes.
     * @param action  [String] the action to fetch the value from.
     * @return [String] the value collected from [action] if present, otherwise empty.
     */
    private fun valueOf(context: QuestContext, action: String): String {
        // necessity
        val matcher = PARAMS_BASE_REGEX.find(action)
        val value   = matcher?.groupValues?.getOrNull(0) ?: return ""

        // return the worked value
        return value.withPlaceholders(QuestPlaceholder::class.java, context)
    }
}