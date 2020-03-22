package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdChest : SCommand() {

    init {
        aliases.add("chest")
        aliases.add("inventory")
        aliases.add("inv")


        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.CHEST).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        var inventory = info.island?.inventory
        info.player?.openInventory(inventory!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandChestHelp
    }
}