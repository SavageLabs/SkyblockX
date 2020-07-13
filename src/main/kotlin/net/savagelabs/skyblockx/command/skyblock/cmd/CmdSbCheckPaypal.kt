package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.StringArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdSbCheckPaypal : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("checkpaypal")

        requiredArgs.add(Argument("island-name", 0, StringArgument()))

        commandRequirements = SCommandRequirementsBuilder().asPlayer(true).withPermission(Permission.ADMIN_CHECKPAYPAL).build()
    }

    override fun perform(info: SCommandInfo) {
        val island = info.getArgAsIsland(0) ?: return

        if (island.paypal == null) {
            info.message(Message.instance.commandSkyblockCheckPaypalNotSet)
            return
        }

        info.message(Message.instance.commandSkyblockCheckPaypalSuccess.replace("{islandName}", island.islandName).replace("{paypal}", island.paypal!!))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandSkyblockCheckPaypalHelp
    }
}