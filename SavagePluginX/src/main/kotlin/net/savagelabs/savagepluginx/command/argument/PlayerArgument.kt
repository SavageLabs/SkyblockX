package net.savagelabs.savagepluginx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import net.savagelabs.savagepluginx.command.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerArgument : ArgumentType {
    override fun getPossibleValues(player: Player): List<String> {
        return Bukkit.getOnlinePlayers().map { plyr -> plyr.name }
    }
}