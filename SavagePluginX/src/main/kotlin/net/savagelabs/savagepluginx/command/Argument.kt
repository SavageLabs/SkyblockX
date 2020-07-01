package net.savagelabs.savagepluginx.command

import org.bukkit.entity.Player

class Argument(
    val name: String,
    val argumentOrder: Int,
    val argumentType: ArgumentType
)

interface ArgumentType {
    fun getPossibleValues(player: Player): List<String>
}
