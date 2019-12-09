package io.illyria.skyblockx.command.skyblock.cmd

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.persist.Message


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