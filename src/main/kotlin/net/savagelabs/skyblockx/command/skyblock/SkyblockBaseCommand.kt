package net.savagelabs.skyblockx.command.skyblock

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.command.skyblock.cmd.*
import io.illyria.skyblockx.persist.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class SkyblockBaseCommand : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand(), CommandExecutor, TabCompleter {

    init {
        this.commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().build()

        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbDelete())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbKick())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbOwner())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbReload())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbHelp())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbCalc())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbBypass())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.skyblock.cmd.CmdSbChest())
        prefix = "/sbx"

        initializeSubCommandData()
        _root_ide_package_.net.savagelabs.skyblockx.Globals.skyblockBaseCommand = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        execute(
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo(
                sender,
                ArrayList(args.toList()),
                label
            )
        )
        return true
    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockBaseHelp
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        info.message(Message.commandSkyblockBaseHelpMessage)
        generateHelp(1, info.commandSender)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return handleTabComplete(sender, command, alias, args)
    }


}

