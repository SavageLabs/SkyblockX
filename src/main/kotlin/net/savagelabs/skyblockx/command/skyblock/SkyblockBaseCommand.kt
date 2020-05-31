package net.savagelabs.skyblockx.command.skyblock

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.command.skyblock.cmd.CmdSEPasteStructure
import net.savagelabs.skyblockx.command.skyblock.cmd.CmdSEPosition
import net.savagelabs.skyblockx.command.skyblock.cmd.CmdSESaveStructure
import net.savagelabs.skyblockx.command.skyblock.cmd.*
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class SkyblockBaseCommand : SCommand(), CommandExecutor, TabCompleter {

    init {
        this.commandRequirements = CommandRequirementsBuilder().build()

        subCommands.add(CmdSEPosition())
        subCommands.add(CmdSESaveStructure())
        subCommands.add(CmdSEPasteStructure())
        subCommands.add(CmdSbDelete())
        subCommands.add(CmdSbKick())
        subCommands.add(CmdSbOwner())
        subCommands.add(CmdSbReload())
        subCommands.add(CmdSbHelp())
        subCommands.add(CmdSbCalc())
        subCommands.add(CmdSbBypass())
        subCommands.add(CmdSbChest())
        prefix = "/sbx"

        initializeSubCommandData()
        SkyblockX.skyblockBaseCommand = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        execute(
            CommandInfo(
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

    override fun perform(info: CommandInfo) {
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

