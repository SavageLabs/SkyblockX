package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.StringArgument
import net.savagelabs.savagepluginx.strings.isAlphaNumeric
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.isIslandNameTaken
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message

class CmdRename : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("rename")
        aliases.add("retag")

        requiredArgs.add(Argument("new-name", 0, StringArgument()))

        commandRequirements = SCommandRequirementsBuilder()
            .withPermission(Permission.RENAME)
            .asLeader(true)
            .asIslandMember(true)
            .build()
    }


    override fun perform(info: SCommandInfo) {
        val newName = info.args[0]
        if (Config.instance.islandNameEnforceLength && (newName.length < Config.instance.islandNameMinLength || newName.length > Config.instance.islandNameMaxLength)) {
            info.message(Message.instance.commandCreateLength, Config.instance.islandNameMinLength.toString(), Config.instance.islandNameMaxLength.toString())
            return
        }

        if (Config.instance.islandNameEnforceAlphaNumeric && !newName.isAlphaNumeric()) {
            info.message(Message.instance.commandCreateNonAlphaNumeric)
            return
        }

        if (isIslandNameTaken(newName)) {
            info.message(Message.instance.commandRenameIslandNameIsTaken)
            return
        }

        info.island?.islandName = newName
        info.message(Message.instance.commandRenameSuccess, newName)
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandRenameHelp
    }
}