package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.persist.Message


class CmdSbDelete : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("delete")
        aliases.add("remove")

        requiredArgs.add(Argument("owner-tag", 0, PlayerArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.ADMIN_DELETEISLAND).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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