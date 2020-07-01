package net.savagelabs.skyblockx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import org.bukkit.entity.Player

class PosArgument : ArgumentType {
    override fun getPossibleValues(player: Player): List<String> {
        return listOf("1", "2")
    }

}