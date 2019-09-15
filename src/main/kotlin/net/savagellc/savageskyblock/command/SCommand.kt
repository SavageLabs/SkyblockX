package net.savagellc.savageskyblock.command

import me.rayzr522.jsonmessage.JSONMessage
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.persist.Message
import java.util.*

abstract class SCommand {

    val aliases = LinkedList<String>()
    val requiredArgs = LinkedList<String>()
    val optionalArgs = LinkedList<String>()
    lateinit var commandRequirements: CommandRequirements

    val subCommands = LinkedList<SCommand>()


    abstract fun perform(commandInfo: CommandInfo)

    fun execute(commandInfo: CommandInfo) {
        if (commandInfo.args.size > 0) {
            for (command in subCommands) {
                if (command.aliases.contains(commandInfo.args[0].toLowerCase())) {
                    // Remove the first arg so when the commandInfo is passed to subcommand, first arg is relative.
                    commandInfo.args.removeAt(0)
                    command.execute(commandInfo)
                    return
                }
            }
        }

        if (!checkRequirements(commandInfo)) {
            return
        }

        if (this !is BaseCommand) {
            if (!checkInput(commandInfo)) {
                return
            }
        }


        perform(commandInfo)
    }


    private fun checkRequirements(commandInfo: CommandInfo): Boolean {
        return commandRequirements.computeRequirements(commandInfo)
    }

    private fun checkInput(commandInfo: CommandInfo): Boolean {
        if (commandInfo.args.size < requiredArgs.size) {
            commandInfo.message(Message.genericCommandsTooFewArgs)
            handleCommandFormat(commandInfo)
            return false
        }

        if (commandInfo.args.size > requiredArgs.size + optionalArgs.size) {
            commandInfo.message(Message.genericCommandsTooManyArgs)
            handleCommandFormat(commandInfo)
            return false
        }
        return true
    }

    private fun handleCommandFormat(commandInfo: CommandInfo) {
        if (commandInfo.isPlayer()) {
            sendCommandFormat(commandInfo)
        } else {
            sendCommandFormat(commandInfo, false)
        }
    }

    private fun sendCommandFormat(commandInfo: CommandInfo, useJSON: Boolean = true) {
        if (useJSON) {
            var commandFormatJSON = JSONMessage.create(color("&7&o((Hoverable))&r")).then(" /is ").then(this.aliases[0]).then(" ")
            for (requiredArg in requiredArgs) {
                commandFormatJSON = commandFormatJSON.then("<$requiredArg>").tooltip("This argument is required.").then(" ")
            }
            for (optionalArg in optionalArgs) {
                commandFormatJSON = commandFormatJSON.then("($optionalArg)").tooltip("The argument is optional").then(" ")
            }

            commandFormatJSON.send(commandInfo.player)
        } else {
            var commandFormat = "/is "
            for (requiredArg in requiredArgs) {
                commandFormat += "<$requiredArg> "
            }
            for (optionalArg in optionalArgs) {
                commandFormat += "($optionalArg) "
            }
            commandInfo.message(commandFormat)
        }



    }

    abstract fun getHelpInfo(): String
}
