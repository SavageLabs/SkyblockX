package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.isIslandNameTaken
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message

class CmdRename : SCommand() {

    init {
        aliases.add("rename")
        aliases.add("retag")

        requiredArgs.add(Argument("new-name", 0, StringArgument()))

        commandRequirements = CommandRequirementsBuilder()
            .withPermission(Permission.RENAME)
            .asLeader(true)
            .asIslandMember(true)
            .build()
    }


    override fun perform(info: CommandInfo) {
        val newName = info.args[0]
        if (Config.instance.islandNameEnforceLength && (newName.length < Config.instance.islandNameMinLength || newName.length > Config.instance.islandNameMaxLength)) {
            info.message(Message.instance.commandCreateLength, Config.instance.islandNameMinLength.toString(), Config.instance.islandNameMaxLength.toString())
            return
        }

        if (Config.instance.islandNameEnforceAlphaNumeric && !newName.chars().allMatch(Character::isLetterOrDigit)) {
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