package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message


class CmdSbDelete : SCommand() {


    init {
        aliases.add("delete")
        aliases.add("remove")

        requiredArgs.add(Argument("owner-tag", 0, PlayerArgument()))

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.ADMIN_DELETEISLAND).build()
    }

    override fun perform(info: CommandInfo) {
        val iPlayerByName = getIPlayerByName(info.args[0])
        if (iPlayerByName?.getIsland() == null) {
            info.message(Message.commandSkyblockRemoveNotAnIslandOwner)
            return
        }
        iPlayerByName.getIsland()?.delete()
        info.message(Message.commandSkyblockRemoveSuccess)

    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockRemoveHelp
    }

}