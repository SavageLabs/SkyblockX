package net.savagellc.savageskyblock.command

import net.savagellc.savageskyblock.command.cmd.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

import net.savagellc.savageskyblock.persist.Message

import java.util.ArrayList



class BaseCommand : SCommand(), CommandExecutor {




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
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        execute(CommandInfo(sender, ArrayList(args.toList()), label))
        return true
    }

    override fun getHelpInfo(): String {
        return Message.commandBaseHelp
    }

    override fun perform(info: CommandInfo) {
        info.commandSender.sendMessage(Message.commandBaseHelpMessage)
    }


}