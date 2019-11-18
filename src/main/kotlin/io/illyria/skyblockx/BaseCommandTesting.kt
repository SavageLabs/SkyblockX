package io.illyria.skyblockx

import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.world.WorldManager
import io.illyria.skyblockx.world.spiral
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BaseCommandTesting : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("command root")
        if (!sender.isOp) {
            sender.sendMessage("you need to be OP to use this testing command.")
            return true
        }
        when (args[0]) {
            "world" -> {
                if (Bukkit.getWorld(Config.skyblockWorldName) != null) {
                    sender.sendMessage("world already exists lol")
                    return true
                }
                sender.sendMessage(Config.skyblockWorldName)
                WorldManager.createVoidWorld()
                sender.sendMessage("created world")
                sender as Player
                sender.teleport(Bukkit.getWorld(Config.skyblockWorldName)!!.spawnLocation)
            }
            "tp" -> {
                sender as Player
                sender.teleport(Bukkit.getWorld(Config.skyblockWorldName)!!.spawnLocation)
            }
            "reload" -> {
                Globals.skyblockX.loadDataFiles()
                Globals.skyblockX.setupOreGeneratorAlgorithm()
            }
            "tp-back" -> {
                sender as Player
                sender.teleport(Bukkit.getWorld("world")!!.spawnLocation)
            }
            "island" -> {
                sender as Player
                val material = Material.valueOf(args[1].toUpperCase())
                val index = args[2].toInt()
                Island(1, spiral(index), "", "I want to fucking kill my self").fillIsland(material)
            }
            "pos1" -> {

            }
            else -> sender.sendMessage("Arg does not exist")
        }
        return true
    }
}