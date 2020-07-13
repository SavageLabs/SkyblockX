package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.persist.Message

class CmdPaypal : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("paypal")

        commandRequirements = SCommandRequirementsBuilder().asPlayer(true).asIslandMember(true).withIslandPermission(IslandPermission.ISLAND_PAYPAL).build()
    }

    override fun perform(info: SCommandInfo) {
        if (info.island!!.paypal == null) {
            info.message(Message.instance.commandPaypalNotSet)
            return
        }

        info.message(Message.instance.commandPaypalSuccess, info.island!!.paypal!!)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandPaypalHelp
    }


}