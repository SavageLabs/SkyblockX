package net.savagelabs.skyblockx.command.island

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.island.cmd.*
import net.savagelabs.skyblockx.command.island.cmd.home.CmdHome
import net.savagelabs.skyblockx.command.island.cmd.member.*
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class IslandBaseCommand : Command<SCommandInfo, SCommandRequirements>(), CommandExecutor, TabCompleter {


    companion object {
        lateinit var instance: IslandBaseCommand
    }


    init {
        this.commandRequirements = SCommandRequirementsBuilder().build()
        subCommands.add(CmdCreate())
        subCommands.add(CmdGo())
        subCommands.add(CmdDelete())
        subCommands.add(CmdCoop())
        subCommands.add(CmdRemove())
        subCommands.add(CmdVisit())
        subCommands.add(CmdHome())
        subCommands.add(CmdHelp())
        subCommands.add(CmdQuest())
        subCommands.add(CmdMember())
        subCommands.add(CmdMenu())
        subCommands.add(CmdJoin())
        subCommands.add(CmdBorder())
        subCommands.add(CmdKick())
        subCommands.add(CmdInvite())
        subCommands.add(CmdLeave())
        subCommands.add(CmdPromote())
        subCommands.add(CmdReset())
        subCommands.add(CmdAllowVisitors())
        subCommands.add(CmdTop())
        subCommands.add(CmdCalc())
        subCommands.add(CmdValue())
        subCommands.add(CmdChest())
        subCommands.add(CmdBiome())
        subCommands.add(CmdSetGo())
        subCommands.add(CmdWorth())
        subCommands.add(CmdRename())
        prefix = "/is"

        initializeSubCommandData()
        instance = this
    }

    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        execute(
            SCommandInfo(
                sender,
                ArrayList(args.toList()),
                label
            )
        )
        return true
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandBaseHelp
    }

    override fun perform(info: SCommandInfo) {
        // If console
        if (info.player == null) {
            info.message(Message.instance.commandBaseHelpMessage)
            generateHelp(1, info.commandSender, info.args)
            return
        }

        if (info.args.isEmpty()) {
            if (Config.instance.openIslandMenuOnBaseCommand && info.iPlayer!!.hasIsland()) {
                // Execute command just to make a shorthand version for /is menu.
                this.subCommands.find { command -> command is CmdMenu }?.perform(info)
            } else {
                // Create an island gui since they dont have an 
                this.subCommands.find { command -> command is CmdCreate }?.perform(info)
            }
        } else {
            info.message(Message.instance.commandBaseHelpMessage)
            generateHelp(1, info.player!!, info.args)
        }
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return handleTabComplete(sender, command, alias, args)
    }


}

