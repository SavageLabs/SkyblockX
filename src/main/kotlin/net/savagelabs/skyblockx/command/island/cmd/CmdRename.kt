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

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.RENAME).asLeader(true).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        val newName = info.args[0]
        if (Config.islandNameEnforceLength && (newName.length < Config.islandNameMinLength || newName.length > Config.islandNameMaxLength)) {
            info.message(Message.commandCreateLength, Config.islandNameMinLength.toString(), Config.islandNameMaxLength.toString())
            return
        }

        if (Config.islandNameEnforceAlphaNumeric && !newName.chars().allMatch(Character::isLetterOrDigit)) {
            info.message(Message.commandCreateNonAlphaNumeric)
            return
        }

        if (isIslandNameTaken(newName)) {
            info.message(Message.commandRenameIslandNameIsTaken)
            return
        }

        info.message(Message.commandRenameSuccess, newName)
    }


    override fun getHelpInfo(): String {
        return Message.commandRenameHelp
    }
}