package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Data
import io.illyria.skyblockx.persist.Message

class CmdSbReload : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("reload")

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.RELOAD).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        Data.save()
        _root_ide_package_.net.savagelabs.skyblockx.Globals.skyblockX.loadDataFiles()
        _root_ide_package_.net.savagelabs.skyblockx.Globals.skyblockX.setupOreGeneratorAlgorithm()
        info.message(Message.commandReloadSuccess)


    }


    override fun getHelpInfo(): String {
        return Message.commandReloadHelp
    }
}