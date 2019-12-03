package io.illyria.skyblockx.command.skyblock.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdSbOwner : SCommand() {


    init {
        aliases.add("newowner")
        aliases.add("owner")

        requiredArgs.add(Argument("island-member", 0, PlayerArgument()))

        requiredArgs.add(Argument("new-owner", 1, PlayerArgument()))

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.ADMIN_NEWOWNER).build()
    }

    override fun perform(info: CommandInfo) {
        println(info)
        val newOwner = info.getArgAsIPlayer(1, cannotReferenceYourSelf = false) ?: return
        val iPlayerByName = getIPlayerByName(info.args[0])
        val island = iPlayerByName?.getIsland()
        if (island == null || island.getOwnerIPlayer() != iPlayerByName) {
            info.message(Message.commandSkyblockRemoveNotAnIslandOwner)
            return
        }

        island.assignNewOwner(newOwner)
        island.getOwnerIPlayer()?.unassignIsland()


        info.message(Message.commandSkyblockNewOwnerSuccess)

    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockNewOwnerHelp
    }

}