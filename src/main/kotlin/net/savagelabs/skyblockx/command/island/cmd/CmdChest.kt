package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdChest : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("chest")
        aliases.add("inventory")
        aliases.add("inv")


        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.CHEST).asIslandMember(true).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        var inventory = info.island?.inventory
        info.player?.openInventory(inventory!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandChestHelp
    }
}