package net.savagelabs.skyblockx.command.skyblock

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.skyblock.cmd.*
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*


class SkyblockBaseCommand : Command<SCommandInfo, SCommandRequirements>(), CommandExecutor, TabCompleter {


    companion object {
        lateinit var instance: SkyblockBaseCommand
    }

    init {
        this.commandRequirements = SCommandRequirementsBuilder().build()

        subCommands.add(CmdSEPosition())
        subCommands.add(CmdSESaveStructure())
        subCommands.add(CmdSEPasteStructure())
        subCommands.add(CmdSbDelete())
        subCommands.add(CmdSbKick())
        subCommands.add(CmdSbOwner())
        subCommands.add(CmdSbReload())
        subCommands.add(CmdSbHelp())
        subCommands.add(CmdSbCalc())
        subCommands.add(CmdSbBypass())
        subCommands.add(CmdSbChest())
        subCommands.add(CmdSbCheckPaypal())
        prefix = "/sbx"
        isBaseCommand = true

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
        return Message.instance.commandSkyblockBaseHelp
    }

    override fun perform(info: SCommandInfo) {
        info.message(Message.instance.commandSkyblockBaseHelpMessage)
        generateHelp(1, info.commandSender, info.args)
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

