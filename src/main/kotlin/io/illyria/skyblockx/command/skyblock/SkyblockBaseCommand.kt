package io.illyria.skyblockx.command.skyblock

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.command.skyblock.cmd.CmdSbDelete
import io.illyria.skyblockx.command.skyblock.cmd.CmdSbKick
import io.illyria.skyblockx.persist.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class SkyblockBaseCommand : SCommand(), CommandExecutor, TabCompleter {

    init {
        this.commandRequirements = CommandRequirementsBuilder().build()
        subCommands.add(CmdSbDelete())
        subCommands.add(CmdSbKick())


        Globals.skyblockBaseCommand = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        execute(CommandInfo(sender, ArrayList(args.toList()), label))
        return true
    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockBaseHelp
    }

    override fun perform(info: CommandInfo) {
        info.message(Message.commandSkyblockBaseHelpMessage)
        generateHelp(1, info.player!!)
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

