package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.persist.engine.FlatDataManager
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message

class CmdSbReload : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("reload")

        commandRequirements = SCommandRequirementsBuilder()
            .withPermission(Permission.RELOAD)
            .build()
    }


    override fun perform(info: SCommandInfo) {
        FlatDataManager.save(Data.instance)
        SkyblockX.skyblockX.loadDataFiles()
        SkyblockX.skyblockX.setupOreGeneratorAlgorithm()
        info.message(Message.instance.commandReloadSuccess)


    }


    override fun getHelpInfo(): String {
        return Message.instance.commandReloadHelp
    }
}