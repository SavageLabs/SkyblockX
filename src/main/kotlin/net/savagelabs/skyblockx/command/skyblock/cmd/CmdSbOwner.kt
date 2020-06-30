package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message
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
            info.message(Message.instance.commandSkyblockRemoveNotAnIslandOwner)
            return
        }

        island.assignNewOwner(newOwner)
        island.getOwnerIPlayer()?.unassignIsland()


        info.message(Message.instance.commandSkyblockNewOwnerSuccess)

    }

    override fun getHelpInfo(): String {
        return Message.instance.commandSkyblockNewOwnerHelp
    }

}