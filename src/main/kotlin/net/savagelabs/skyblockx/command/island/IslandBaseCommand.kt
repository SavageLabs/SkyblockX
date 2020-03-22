package net.savagelabs.skyblockx.command.island

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.command.island.cmd.*
import net.savagelabs.skyblockx.command.island.cmd.home.CmdHome
import io.illyria.skyblockx.command.island.cmd.member.*
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class IslandBaseCommand : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand(), CommandExecutor, TabCompleter {

    init {
        this.commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().build()
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdCreate())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdGo())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdDelete())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdSEPosition())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdSESaveStructure())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdSEPasteStructure())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdCoop())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdRemove())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdVisit())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.home.CmdHome())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdHelp())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdQuest())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMember())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdMenu())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdJoin())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdBorder())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdKick())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdInvite())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdLeave())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdPromote())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdReset())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdAllowVisitors())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdTop())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdCalc())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdValue())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdChest())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdBiome())
        prefix = "/is"

        initializeSubCommandData()
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand = this
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
        return Message.commandBaseHelp
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        // If console
        if (info.player == null) {
            info.message(Message.commandBaseHelpMessage)
            generateHelp(1, info.commandSender)
            return
        }

        if (info.args.isEmpty()) {
            if (Config.openIslandMenuOnBaseCommand && info.iPlayer!!.hasIsland()) {
                // Execute command just to make a shorthand version for /is menu.
                this.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdMenu }?.perform(info)
            } else {
                // Create an island gui since they dont have an island.
                this.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdCreate }?.perform(info)
            }
        } else {
            info.message(Message.commandBaseHelpMessage)
            generateHelp(1, info.player!!)
        }
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

