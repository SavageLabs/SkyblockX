package io.illyria.skyblockx.command

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.cmd.*
import io.illyria.skyblockx.command.cmd.home.CmdHome
import io.illyria.skyblockx.command.cmd.home.CmdHomeGo
import io.illyria.skyblockx.command.cmd.member.CmdMember
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import net.prosavage.baseplugin.WorldBorderUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class BaseCommand : SCommand(), CommandExecutor, TabCompleter {

    init {
        this.commandRequirements = CommandRequirementsBuilder().build()
        subCommands.add(CmdCreate())
        subCommands.add(CmdGo())
        subCommands.add(CmdDelete())
        subCommands.add(CmdSEPosition())
        subCommands.add(CmdSESaveStructure())
        subCommands.add(CmdSEPasteStructure())
        subCommands.add(CmdBypass())
        subCommands.add(CmdCoop())
        subCommands.add(CmdRemove())
        subCommands.add(CmdTeleport())
        subCommands.add(CmdHome())
        subCommands.add(CmdHelp())
        subCommands.add(CmdQuest())
        subCommands.add(CmdMember())
        subCommands.add(CmdMenu())
        subCommands.add(CmdJoin())
        subCommands.add(CmdBorder())

        Globals.baseCommand = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        execute(CommandInfo(sender, ArrayList(args.toList()), label))
        return true
    }

    override fun getHelpInfo(): String {
        return Message.commandBaseHelp
    }

    override fun perform(info: CommandInfo) {
        // If console
        if (info.player == null) {
            info.message(Message.commandBaseHelpMessage)
            generateHelp(1, info.player!!)
            return
        }

        if (info.iPlayer!!.hasIsland()) {
            // Execute command just to make a shorthand version for /is menu.
            this.subCommands.find { command -> command is CmdMenu }?.perform(info)
        } else {
            // Create an island gui since they dont have an island.
            this.subCommands.find { command -> command is CmdCreate }?.perform(info)
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        // Spigot has an empty arg instead of making the array so we gotta check if it's empty :).
        if (args.size == 1) {
            val tabComplete = ArrayList<String>()
            if (args[0].isEmpty()) {
                // Just loop all commands and give the alias.
                subCommands.forEach { subCommand -> tabComplete.add(subCommand.aliases[0]) }
            } else {
                for (subCommand in subCommands) {
                    for (subCommandAlias in subCommand.aliases) {
                        if (subCommandAlias.toLowerCase().startsWith(args[0].toLowerCase())) {
                            tabComplete.add(subCommandAlias)
                        }
                    }
                }
            }
            return tabComplete
        }

        // Now we gotta check if the command has args and show those...
        if (args.size >= 2) {
            // This is needed so we can get the right relative argument for the command when tabbing.
            var relativeArgIndex = 2
            var subCommandIndex = 0


            var commandToTab: SCommand = this

            // Predicate for filtering our command.
            // If command is not found return empty list.
            while (commandToTab.subCommands.isNotEmpty()) {
                val findCommand = commandToTab.subCommands.find { subCommand ->
                    subCommand.aliases[0].equals(
                        args[subCommandIndex],
                        true
                    )
                }
                subCommandIndex++
                if (findCommand != null) commandToTab = findCommand else break
                relativeArgIndex++
            }


            // This is the actual arg we need to complete.
            val argToComplete = args.size + 1 - relativeArgIndex
            if (commandToTab.requiredArgs.size >= argToComplete) {
                // Quick add all so we can find from all args.
                val list = mutableListOf<Argument>()
                list.addAll(commandToTab.requiredArgs)
                list.addAll(commandToTab.optionalArgs)
                val possibleValues = mutableListOf<String>()
                val arg = list.find { argument -> argument.argumentOrder == argToComplete }
                if (arg != null) {
                    possibleValues.addAll(arg.argumentType.getPossibleValues(if (sender is Player) getIPlayer(sender) else null).toList())
                }
                // If we have more subCommands show those
                if (commandToTab.subCommands.isNotEmpty()) {
                    commandToTab.subCommands.forEach { subCommand -> possibleValues.add(subCommand.aliases[0]) }
                }
                return possibleValues
            }

        }

        return emptyList()
    }

}

