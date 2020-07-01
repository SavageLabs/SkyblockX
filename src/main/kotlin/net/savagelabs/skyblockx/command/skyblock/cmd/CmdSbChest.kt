package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message

class CmdSbChest : Command<SCommandInfo, SCommandRequirements>() {


    init {
        aliases.add("chest")
        aliases.add("viewchest")

        requiredArgs.add(Argument("owner-tag", 0, PlayerArgument()))

        commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.ADMIN_OPENCHEST).build()
    }

    override fun perform(info: SCommandInfo) {
        val iPlayerByName = getIPlayerByName(info.args[0])
        if (!iPlayerByName?.hasIsland()!!) {
            info.message(Message.instance.commandSkyblockOpenChestNotAnIslandMember)
            return
        }
        val inventory = iPlayerByName.getIsland()!!.inventory
        iPlayerByName.getPlayer().openInventory(inventory!!)
        info.message(Message.instance.commandSkyblockOpenChestOpening)

    }

    override fun getHelpInfo(): String {
        return Message.instance.commandSkyblockOpenChestNotAnIslandMember
    }

}