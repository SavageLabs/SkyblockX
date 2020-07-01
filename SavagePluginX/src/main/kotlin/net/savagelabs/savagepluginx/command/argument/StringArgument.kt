package net.savagelabs.savagepluginx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import org.bukkit.entity.Player

class StringArgument : ArgumentType {
    override fun getPossibleValues(player: Player): List<String> {
        return listOf("something")
    }
}