package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.command.argument.RankArgument
import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.menu.PermsManageMenu
import net.savagelabs.skyblockx.gui.menu.PermsMenu
import net.savagelabs.skyblockx.persist.Message

class CmdPerms : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("perms")
        aliases.add("permissions")

        optionalArgs.add(Argument("rank", 0, RankArgument()))

        commandRequirements = SCommandRequirementsBuilder().asPlayer(true).asIslandMember(true).withIslandPermission(IslandPermission.PERMS_VIEW).build()
    }

    override fun perform(info: SCommandInfo) {
        if (info.args.isEmpty()) {
            buildMenu(PermsMenu()).open(info.player)
            return
        }

        val rank = info.getArgAsRank(0) ?: return
        buildMenu(PermsManageMenu(rank)).open(info.player)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandPermsHelp
    }

}