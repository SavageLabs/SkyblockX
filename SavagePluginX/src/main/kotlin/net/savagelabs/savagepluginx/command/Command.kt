package net.savagelabs.savagepluginx.command

import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.savagepluginx.persist.BaseConfig
import net.savagelabs.savagepluginx.strings.color
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

abstract class Command<T: CommandInfo, R: CommandRequirements<T>> {

    val aliases = LinkedList<String>()
    val requiredArgs = LinkedList<Argument>()
    val optionalArgs = LinkedList<Argument>()
    lateinit var commandRequirements: R
    var prefix = ""
    var bypassArgumentCount = false
    var showInHelp = true
    var isBaseCommand = false

    val subCommands = LinkedList<Command<T, R>>()

    fun execute(info: T) {
        if (info.args.isNotEmpty()) {
            for (command in subCommands) {
                if (command.aliases.contains(info.args.first().toLowerCase())) {
                    // Remove the first arg so when the CommandInfo is passed to subcommand,
                    // first arg is relative.
                    info.args.removeAt(0)
                    command.execute(info)
                    return
                }
            }
        }

        if (!checkRequirements(info)) {
            return
        }

        if (!this.isBaseCommand) {
            if (!checkInput(info)) {
                return
            }
        }
        perform(info)
    }

    abstract fun perform(info: T)


    fun initializeSubCommandData() {
        for (subCommand in subCommands) {
            subCommand.prefix = prefix.replace("/", "")
            subCommand.initializeSubCommandData()
        }
    }


    private fun checkRequirements(info: T): Boolean {
        return commandRequirements.checkRequirements(info)
    }

    private fun checkInput(info: T): Boolean {
        if (info.args.size < requiredArgs.size) {
            info.message(BaseConfig.instance.genericCommandTooFewArgs)
            handleCommandFormat(info)
            return false
        }

        if (!bypassArgumentCount && info.args.size > requiredArgs.size + optionalArgs.size) {
            info.message(BaseConfig.instance.genericCommandTooManyArgs)
            handleCommandFormat(info)
            return false
        }
        return true
    }

    private fun handleCommandFormat(info: T) {
        if (info.isPlayer()) {
            sendCommandFormat(info)
        } else {
            sendCommandFormat(info, false)
        }
    }

    /**
     * Removes based off of first alias.
     */
    fun removeSubCommand(command: Command<T, R>) {
        subCommands.remove(this.subCommands.find { subCommand -> subCommand.aliases.first == command.aliases.first })
    }

    var parentHelpString = ""

    fun addSubCommand(command: Command<T, R>) {
        subCommands.add(command)
        command.prefix = this.prefix
        if (this.isBaseCommand) return
        command.parentHelpString += aliases[0] + " "
    }

    fun generateHelp(page: Int, commandSender: CommandSender, args: ArrayList<String>) {
        val pageStartEntry = BaseConfig.instance.helpGeneratorPageEntries * (page - 1)
        if (page <= 0 || pageStartEntry >= subCommands.size) {
            commandSender.sendMessage(
                color(
                    BaseConfig.instance.commandEnginePrefix + String.format(
                        BaseConfig.instance.commandHelpGeneratorPageInvalid,
                        page
                    )
                )
            )
            return
        }

        for (i in pageStartEntry until (pageStartEntry + BaseConfig.instance.helpGeneratorPageEntries)) {
            if (subCommands.size - 1 < i) {
                continue
            }
            val command = subCommands[i]
            if (!command.showInHelp) continue
            val base = (if (aliases.size > 0) parentHelpString + aliases[0] + " " else "") + command.aliases[0]
            val tooltip = BaseConfig.instance.commandHelpGeneratorClickMeToPaste
            if (commandSender is Player) {
                JSONMessage.create(
                    color(
                        String.format(
                            BaseConfig.instance.commandHelpGeneratorFormat,
                            prefix,
                            base,
                            command.getHelpInfo()
                        )
                    )
                )
                    .color(BaseConfig.instance.commandHelpGeneratorBackgroundColor)
                    .tooltip(color(tooltip)).suggestCommand("/$prefix $base").send(commandSender)
            } else commandSender.sendMessage(
                color(
                    String.format(
                        BaseConfig.instance.commandHelpGeneratorFormat,
                        prefix,
                        base,
                        command.getHelpInfo()
                    )
                )
            )


        }
        if (commandSender is Player) {
            val pageNav = JSONMessage.create("       ")
            if (page > 1) pageNav.then(color(BaseConfig.instance.commandHelpGeneratorPageNavBack)).tooltip("Go to Page ${page - 1}").runCommand(
                "/$prefix help ${page - 1}"
            ).then("       ")
            if (page < subCommands.size) pageNav.then(color(BaseConfig.instance.commandHelpGeneratorPageNavNext)).tooltip("Go to Page ${page + 1}").runCommand(
                "/$prefix help ${page + 1}"
            )
            pageNav.send(commandSender)
        }

    }

    fun sendCommandFormat(info: T, useJSON: Boolean = true) {
        val list = mutableListOf<Argument>()
        list.addAll(requiredArgs)
        list.addAll(optionalArgs)
        requiredArgs.sortBy { arg -> arg.argumentOrder }

        if (useJSON) {
            var commandFormatJSON =
                JSONMessage.create(color(BaseConfig.instance.commandEngineFormatHoverable)).then(String.format(BaseConfig.instance.commandEngineFormatPrefix, prefix, parentHelpString + this.aliases[0])).then(" ")

            for (arg in list) {
                commandFormatJSON = if (optionalArgs.contains(arg)) {
                    commandFormatJSON.then(String.format(BaseConfig.instance.commandEngineFormatOptionalArg, arg.name)).tooltip(BaseConfig.instance.commandEngineFormatOptionalTooltip).then(" ")
                } else {
                    commandFormatJSON.then(String.format(BaseConfig.instance.commandEngineFormatRequiredArg, arg.name)).tooltip(BaseConfig.instance.commandEngineFormatRequiredTooltip).then(" ")
                }

            }
            commandFormatJSON.send(info.player)
            return
        }

        // This is for the rest usually for console.
        var commandFormat = "/$prefix ${this.aliases.first} "
        for (arg in list) {
            commandFormat += if (optionalArgs.contains(arg)) {
                "(${arg.name}) "
            } else {
                "<${arg.name}> "

            }
        }

        info.message(commandFormat)


    }



        fun handleTabComplete(
            sender: CommandSender,
            command: org.bukkit.command.Command,
            alias: String,
            args: Array<String>
        ): List<String> {
        // This is for basic subcommand tabbing. /souls <subcommand>

        // Spigot has an empty arg instead of making the array so we gotta check if it's empty :).
        if (args.size == 1) {
            val tabComplete = ArrayList<String>()
            if (args[0].isEmpty()) {
                // Just loop all commands and give the alias.
                subCommands.forEach { subCommand -> tabComplete.add(subCommand.aliases[0]) }
            } else {
                for (subCommand in subCommands) {
                    for (subCommandAlias in subCommand.aliases) {
                        if (subCommandAlias.toLowerCase().startsWith(args[0], true)) {
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


            var commandToTab: Command<T, R> = this

            // Predicate for filtering our command.
            // If command is not found return empty list.
            while (commandToTab.subCommands.isNotEmpty()) {
                var findCommand: Command<T, R>?
                findCommand = commandToTab.subCommands.find { subCommand -> subCommand.aliases.contains(args[subCommandIndex].toLowerCase()) }
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
                    val start = args[args.size - 1]
                    var possibleValuesArg = if (sender !is Player) emptyList() else
                     arg.argumentType.getPossibleValues(sender)
                    if (!start.isNullOrEmpty()) possibleValuesArg = possibleValuesArg.filter { possibleValue -> possibleValue.startsWith(start, true) }
                    possibleValues.addAll(possibleValuesArg.toList())
                }
                // If we have more subCommands show those
                if (commandToTab.subCommands.isNotEmpty()) {
                    for (subCommand in commandToTab.subCommands) {
                        for (subCommandAlias in subCommand.aliases) {
                            if (subCommandAlias.toLowerCase().startsWith(args[argToComplete + 1], true)) {
                                possibleValues.add(subCommandAlias)
                            }
                        }
                    }
                }
                return possibleValues
            }

        }

        return emptyList()
    }

    abstract fun getHelpInfo(): String
}