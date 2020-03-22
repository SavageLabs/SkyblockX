package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message

class CmdSbChest : SCommand() {


    init {
        aliases.add("chest")
        aliases.add("viewchest")

        requiredArgs.add(Argument("owner-tag", 0, PlayerArgument()))

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.ADMIN_OPENCHEST).build()
    }

    override fun perform(info: CommandInfo) {
        val iPlayerByName = getIPlayerByName(info.args[0])
        if (!iPlayerByName?.hasIsland()!!) {
            info.message(Message.commandSkyblockOpenChestNotAnIslandMember)
            return
        }
        val inventory = iPlayerByName.getIsland()!!.inventory
        iPlayerByName.getPlayer().openInventory(inventory)
        info.message(Message.commandSkyblockOpenChestOpening)

    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockOpenChestNotAnIslandMember
    }

}