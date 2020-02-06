package io.illyria.skyblockx.command.skyblock.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.persist.Message

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