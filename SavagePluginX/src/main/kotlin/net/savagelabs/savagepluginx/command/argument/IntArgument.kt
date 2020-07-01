package net.savagelabs.savagepluginx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import org.bukkit.entity.Player

class IntArgument : ArgumentType {
    override fun getPossibleValues(player: Player): List<String> {
        return listOf("1")
    }

}