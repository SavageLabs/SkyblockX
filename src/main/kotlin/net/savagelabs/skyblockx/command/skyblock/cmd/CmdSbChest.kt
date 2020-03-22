package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.persist.Message

class CmdSbChest : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("chest")
        aliases.add("viewchest")

        requiredArgs.add(Argument("owner-tag", 0, PlayerArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.ADMIN_OPENCHEST).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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