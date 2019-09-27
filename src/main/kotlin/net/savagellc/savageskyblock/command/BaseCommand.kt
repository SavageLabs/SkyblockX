package net.savagellc.savageskyblock.command

import net.savagellc.savageskyblock.Globals
import net.savagellc.savageskyblock.command.cmd.*
import net.savagellc.savageskyblock.command.cmd.home.CmdHome
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*


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
        subCommands.add(CmdTeleport())
        subCommands.add(CmdHome())
        subCommands.add(CmdHelp())


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
        info.commandSender.sendMessage(Message.commandBaseHelpMessage)
        generateHelp(1, info.player!!)
    }


}