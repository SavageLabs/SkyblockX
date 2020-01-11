package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
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
//        if (inventory == null) {
//            info.island?.inventory = Bukkit.createInventory(null, (Config.chestRows[1] ?: 3) * 9)
//            inventory = info.island?.inventory
//        }
        info.player?.openInventory(inventory!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandChestHelp
    }
}