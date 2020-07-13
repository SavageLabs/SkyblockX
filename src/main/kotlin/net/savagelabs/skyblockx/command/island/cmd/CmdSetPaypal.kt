package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.StringArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.persist.Message

class CmdSetPaypal : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("setpaypal")

        requiredArgs.add(Argument("paypal", 0, StringArgument()))

        commandRequirements = SCommandRequirementsBuilder().asPlayer(true).asIslandMember(true).withIslandPermission(IslandPermission.ISLAND_SETPAYPAL).build()
    }

    override fun perform(info: SCommandInfo) {
        val paypal = info.args[0]

        info.island!!.paypal = paypal
        info.message(Message.instance.commandSetPaypalSuccess, paypal)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandSetPaypalHelp
    }

}