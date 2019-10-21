package io.illyria.skyblockx.command

import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.persist.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*


class BaseCommand : SCommand(), CommandExecutor, TabCompleter {

    init {
        this.commandRequirements = io.illyria.skyblockx.command.CommandRequirementsBuilder().build()
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdCreate())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdGo())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdDelete())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdSEPosition())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdSESaveStructure())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdSEPasteStructure())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdBypass())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdCoop())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdRemove())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdTeleport())
        subCommands.add(io.illyria.skyblockx.command.cmd.home.CmdHome())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdHelp())
        subCommands.add(io.illyria.skyblockx.command.cmd.CmdQuest())

        io.illyria.skyblockx.Globals.baseCommand = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        execute(io.illyria.skyblockx.command.CommandInfo(sender, ArrayList(args.toList()), label))
        return true
    }

    override fun getHelpInfo(): String {
        return Message.commandBaseHelp
    }

    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        info.message(Message.commandBaseHelpMessage)
        generateHelp(1, info.player!!)
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
                val list = mutableListOf<SCommand.Argument>()
                list.addAll(commandToTab.requiredArgs)
                list.addAll(commandToTab.optionalArgs)
                val arg = list.find { argument -> argument.argumentOrder == argToComplete } ?: return emptyList()
                val possibleValues =
                    arg.argumentType.getPossibleValues(if (sender is Player) getIPlayer(sender) else null)
                        .toMutableList()
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

