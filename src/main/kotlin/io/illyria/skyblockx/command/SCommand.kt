package io.illyria.skyblockx.command

import io.illyria.skyblockx.command.island.IslandBaseCommand
import io.illyria.skyblockx.command.skyblock.SkyblockBaseCommand
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

abstract class SCommand {

    val aliases = LinkedList<String>()
    val requiredArgs = LinkedList<Argument>()
    val optionalArgs = LinkedList<Argument>()
    lateinit var commandRequirements: CommandRequirements
    var prefix = ""

    val subCommands = LinkedList<SCommand>()


    abstract fun perform(info: CommandInfo)

    fun execute(info: CommandInfo) {
        if (info.args.size > 0) {
            for (command in subCommands) {
                if (command.aliases.contains(info.args[0].toLowerCase())) {
                    // Remove the first arg so when the CommandInfo is passed to subcommand, first arg is relative.
                    info.args.removeAt(0)
                    command.execute(info)
                    return
                }
            }
        }

        if (!checkRequirements(info)) {
            return
        }

        if (this !is SkyblockBaseCommand && this !is IslandBaseCommand) {
            if (!checkInput(info)) {
                return
            }
        }
        perform(info)
    }


    private fun checkRequirements(info: CommandInfo): Boolean {
        return commandRequirements.computeRequirements(info)
    }

    private fun checkInput(info: CommandInfo): Boolean {
        if (info.args.size < requiredArgs.size) {
            info.message(Message.genericCommandsTooFewArgs)
            handleCommandFormat(info)
            return false
        }

        if (info.args.size > requiredArgs.size + optionalArgs.size) {
            info.message(Message.genericCommandsTooManyArgs)
            handleCommandFormat(info)
            return false
        }
        return true
    }

    private fun handleCommandFormat(info: CommandInfo) {
        if (info.isPlayer()) {
            sendCommandFormat(info)
        } else {
            sendCommandFormat(info, false)
        }
    }

    fun generateHelp(page: Int, player: Player) {
        val pageStartEntry = Config.helpGeneratorPageEntries * (page - 1)
        if (page <= 0 || pageStartEntry >= subCommands.size) {
            player.sendMessage(
                color(
                    Message.messagePrefix + String.format(
                        Message.commandHelpGeneratorPageInvalid,
                        page
                    )
                )
            )
            return
        }

        for (i in pageStartEntry until (pageStartEntry + Config.helpGeneratorPageEntries)) {
            if (subCommands.size - 1 < i) {
                continue
            }
            val command = subCommands[i]
            val base = (if (aliases.size > 0) aliases[0] + " " else "") + command.aliases[0]
            val tooltip = String.format(
                Message.commandHelpGeneratorIslandRequired,
                (if (command.commandRequirements.asIslandMember) Message.commandHelpGeneratorRequires else Message.commandHelpGeneratorNotRequired)
            ) + "\n" + Message.commandHelpGeneratorClickMeToPaste

            JSONMessage.create(color(String.format(Message.commandHelpGeneratorFormat, prefix, base, command.getHelpInfo())))
                .color(Message.commandHelpGeneratorBackgroundColor)
                .tooltip(color(tooltip)).suggestCommand("/$prefix $base").send(player)
        }
        val pageNav = JSONMessage.create("       ")
        if (page > 1) pageNav.then(color(Message.commandHelpGeneratorPageNavBack)).tooltip("Go to Page ${page - 1}").runCommand(
            "/$prefix help ${page - 1}"
        ).then("       ")
        if (page < subCommands.size) pageNav.then(color(Message.commandHelpGeneratorPageNavNext)).tooltip("Go to Page ${page + 1}").runCommand(
            "/$prefix help ${page + 1}"
        )
        pageNav.send(player)
    }

    private fun sendCommandFormat(info: CommandInfo, useJSON: Boolean = true) {
        val list = mutableListOf<Argument>()
        list.addAll(requiredArgs)
        list.addAll(optionalArgs)
        requiredArgs.sortBy { arg -> arg.argumentOrder }
        if (useJSON) {
            var commandFormatJSON =
                JSONMessage.create(color("&7&o((Hoverable))&r")).then(" /is ").then(this.aliases[0]).then(" ")
            for (arg in list) {
                commandFormatJSON = if (optionalArgs.contains(arg)) {
                    commandFormatJSON.then("(${arg.name})").tooltip("The argument is optional").then(" ")
                } else {
                    commandFormatJSON.then("<${arg.name}>").tooltip("This argument is required.").then(" ")
                }

            }
            commandFormatJSON.send(info.player)
            return
        }

        // This is for the rest usually for console.
        var commandFormat = "/is "
        for (arg in requiredArgs) {
            commandFormat += if (optionalArgs.contains(arg)) {
                "(${arg.name}) "
            } else {
                "<${arg.name}> "

            }
        }

        info.message(commandFormat)


    }

    class Argument(
        val name: String,
        val argumentOrder: Int,
        val argumentType: ArgumentType
    )

    abstract class ArgumentType {
        abstract fun getPossibleValues(iPlayer: IPlayer?): List<String>
    }

    class HomeArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return if (iPlayer != null && iPlayer.hasIsland()) iPlayer.getIsland()!!.getAllHomes().keys.toList() else emptyList()
        }
    }

    class PlayerArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return Bukkit.getOnlinePlayers().map { player -> player.name }
        }
    }

    class StringArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return emptyList()
        }
    }

    class IntArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return listOf(1.toString())
        }
    }

    class PosArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return if (iPlayer != null && iPlayer.pos1 == null) listOf(1.toString()) else listOf(2.toString())
        }
    }

    class BooleanArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return listOf("true", "false")
        }
    }

    class MemberArgument : ArgumentType() {
        override fun getPossibleValues(iPlayer: IPlayer?): List<String> {
            return if (iPlayer != null && iPlayer.hasIsland()) iPlayer.getIsland()!!.getAllMembers().toList() else emptyList()
        }
    }

    fun handleTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
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

    abstract fun getHelpInfo(): String
}
