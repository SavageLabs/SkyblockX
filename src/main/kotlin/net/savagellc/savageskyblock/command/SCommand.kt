package net.savagellc.savageskyblock.command

import me.rayzr522.jsonmessage.JSONMessage
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.entity.Player
import java.util.*

abstract class SCommand {

    val aliases = LinkedList<String>()
    val requiredArgs = LinkedList<String>()
    val optionalArgs = LinkedList<String>()
    lateinit var commandRequirements: CommandRequirements

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

        if (this !is BaseCommand) {
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
            player.sendMessage(color(String.format(Message.commandHelpGeneratorPageInvalid, page)))
            return
        }



        for (i in pageStartEntry..pageStartEntry + Config.helpGeneratorPageEntries) {
            if (subCommands.size <= i) {
                continue
            }
            val command = subCommands[i]
            val base = (if (aliases.size > 0) aliases[0] + " " else "") + command.aliases[0]
            val tooltip = String.format(
                Message.commandHelpGeneratorIslandRequired,
                (if (command.commandRequirements.asIslandMember) Message.commandHelpGeneratorRequires else Message.commandHelpGeneratorNotRequired)
            ) + "\n" + String.format(Message.commandHelpGeneratorClickMeToPaste, "/is $base")

            JSONMessage.create(color(String.format(Message.commandHelpGeneratorFormat, base, command.getHelpInfo())))
                .tooltip(color(tooltip)).suggestCommand("/is $base").send(player)
        }
    }

    private fun sendCommandFormat(info: CommandInfo, useJSON: Boolean = true) {
        if (useJSON) {
            var commandFormatJSON =
                JSONMessage.create(color("&7&o((Hoverable))&r")).then(" /is ").then(this.aliases[0]).then(" ")
            for (requiredArg in requiredArgs) {
                commandFormatJSON =
                    commandFormatJSON.then("<$requiredArg>").tooltip("This argument is required.").then(" ")
            }
            for (optionalArg in optionalArgs) {
                commandFormatJSON =
                    commandFormatJSON.then("($optionalArg)").tooltip("The argument is optional").then(" ")
            }

            commandFormatJSON.send(info.player)
        } else {
            var commandFormat = "/is "
            for (requiredArg in requiredArgs) {
                commandFormat += "<$requiredArg> "
            }
            for (optionalArg in optionalArgs) {
                commandFormat += "($optionalArg) "
            }
            info.message(commandFormat)
        }


    }

    abstract fun getHelpInfo(): String
}
