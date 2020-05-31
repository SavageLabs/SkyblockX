package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message

class CmdSbReload : SCommand() {

    init {
        aliases.add("reload")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.RELOAD).build()
    }


    override fun perform(info: CommandInfo) {
        Data.save()
        SkyblockX.skyblockX.loadDataFiles()
        SkyblockX.skyblockX.setupOreGeneratorAlgorithm()
        info.message(Message.commandReloadSuccess)


    }


    override fun getHelpInfo(): String {
        return Message.commandReloadHelp
    }
}