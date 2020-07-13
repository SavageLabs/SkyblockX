package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.StringArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class CmdWho : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("who")
        aliases.add("info")

        optionalArgs.add(Argument("island-name", 0, StringArgument()))

        commandRequirements = SCommandRequirementsBuilder().build()
    }

    override fun perform(info: SCommandInfo) {
        if (info.args.isEmpty()) {
            if (info.commandSender !is Player) {
                sendCommandFormat(info, false)
                return
            }

            val island = info.island
            if (island == null) {
                sendCommandFormat(info)
                return
            }

            sendIslandInfo(info, island)
            return
        }

        val island = info.getArgAsIsland(0) ?: return
        sendIslandInfo(info, island)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandWhoHelp
    }


    private fun sendIslandInfo(info: SCommandInfo, island: Island) {
        val memberList = island.members.toMutableList()
        memberList.add(island.ownerUUID)
        memberList.sortBy { it }
        val onlineMembers = memberList.map { uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)) }.filter { player -> player.isOnline }
        val offlineMembers = memberList.map { uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)) }.filter { player -> !player.isOnline }
        for (line in Message.instance.commandWhoFormat) {
            info.message(line
                    .replace("{name}", island.islandName)
                    .replace("{ownerName}", island.ownerTag)
                    .replace("{value}", island.getValue().toString())
                    .replace("{onlineMembers}", onlineMembers.map { player -> player.name }.joinToString(", "))
                    .replace("{offlineMembers}", offlineMembers.map { player -> player.name }.joinToString(", ")),
            false)
        }
    }

}