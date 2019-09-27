package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.serializer.Serializer
import net.savagellc.savageskyblock.core.color
import org.bukkit.ChatColor

object Message {

    @Transient
    private val instance = this

    var commandRequirementsNotAPlayer = "&cThis command requires the executor to be a player."
    var commandRequirementsNotAnIslandMember = "&cThis command requires the executor to be a island member, create one using \"/is create\"."
    var commandRequirementsPlayerDoesNotHavePermission = "&cThis command requires the permission %1\$s"

    var commandHelpGeneratorPageInvalid = "&cThe page %1\$s is invalid."
    var commandHelpGeneratorFormat = "&b/is %1\$s &8> &7 %2\$s"
    var commandHelpGeneratorNotRequired = "&c✗&r"
    var commandHelpGeneratorRequires = "&a✔&r"
    var commandHelpGeneratorIslandRequired = "Island member requirement: %1\$s"
    var commandHelpGeneratorClickMeToPaste = "&7Click me to paste \"%1\$s\" in chat bar."


    var commandParsingArgIsNotInt = "&cThis argument is not an integer, please make it one."
    var commandParsingPlayerDoesNotExist = "&cThis player does not exist."
    var commandParsingPlayerIsYou = "&cYou cannot reference yourself."


    var genericCommandsTooFewArgs = "&cThis command requires more arguments."
    var genericCommandsTooManyArgs = "&cThis command requires less arguments."
    var genericActionRequiresPermission = "&cThis action requires the permission %1\$s"


    var commandBaseHelp = "The base command for skyblock."
    var commandBaseHelpMessage = "&aPlease execute /is help."

    var commandCreateHelp = "Creates a skyblock island."
    var commandCreateAlreadyHaveAnIsland = "You already have an island, use /is delete to delete your island."

    var commandGoHelp = "Takes you to an island"
    var commandGoTeleporting = "Taking you to your island..."

    var commandDeleteHelp = "Deletes an island."
    var commandDeleteDeletedIsland = "Your island has been deleted."

    var commandSEPostionHelp = "This command lets you set positions for skyblock edit."
    var commandSEPositionInvalidIndex = "The specified position index is invalid, it must be either 1 or 2."
    var commandSEPosition = "Please right click a block to set it to position %1\$s"

    var commandSESaveStructureHelp = "This command saves a structure to a file name specified"
    var commandSESaveStructurePositionsNotSet = "You have not set both positions use the command \"/is pos\" to set the positions"

    var commandSEPasteStructurePasted = "Structure pasted successfully"

    var commandBypassToggle = "You are now %1\$s bypass mode"
    var commandBypassHelp = "This command allows you to bypass island checks (For Server Administrators)"


    var commandCoopInvokerSuccess = "You have added %1\$s to your island as co-op"
    var commandCoopMessageRecipient = "You have been added as a co-op player to %1\$s's island."
    var commandCoopCannotHaveMoreCoopPlayers = "You cannot have more co-op players, run &c/is coop&r to view current co-op players, and remove them using &c/is remove <player>"
    var commandCoopHelp = "This command allows you to set another player to co-op mode."



    var commandHomeHomeSet = "You have set this location to your %1\$s home."
    var commandHomeCannotHaveMoreHomes = "You cannot have more homes set."
    var commandHomeHelp = "This command allows you to go to a use island homes."
    var commandHomeSetHelp = "This command allows you to set island homes."
    var commandHomeList = "This command allows you to list island homes."

    var commandHelpHelp = "This command allows you to view information about other commands."

    var commandRemoveInvokerSuccess = "You have removed %1\$s from your island."
    var commandRemoveInvokerCoopRemoved = "%1\$s's co-op status has been removed."
    var commandRemoveInvokerPlayerNotOnIsland = "The specified player's location is currently not on your island."
    var commandRemovedCoopStatus = "Your co-op status has been removed due to you being removed from the island."
    var commandRemoveHelp = "This command removes someone from your island, also removes co-op status if they have it."

    var commandTpHelp = "This command allows you to teleport to islands of other players."
    var commandTpPossibleLocationsHeader = "You can teleport to the following locations..."
    var commandTpPossibleLocationsFormat = "%1\$s. %2\$s's island."
    var commandTpThisIslandIsNotValid = "The player %1\$s does not own an island. Type &c/is tp &rto list possible locations."
    var commandTpTeleporting = "Teleporting you to %1\$s's island."
    var commandTpNoPermission = "The specified island does not have you as co-op or as a island member, "

    var listenerVoidDeathPrevented = "You fell into the void, teleporting you back."
    var listenerBlockPlacementDenied = "You can only place blocks inside of your island."
    var listenerActionDeniedCreateAnIslandFirst = "This action has been denied, please create an island first."
    var listenerObsidianBucketLava = "The obsidian has been turned back to lava, be careful!"
    var listenerPlayerDamageCancelled = "Damage cancelled due to you not being on your island."

    var skyblockEditPositionSet = "You have set position %1\$s to %2\$s"
    var skyblockEditStructureSaved = "You have saved a structure to a file called %1\$s"
    var skyblockEditErrorPositionsNotInSameWorld = "Position 1 and position 2 are no in the same world."

    override fun toString(): String {
        return color(super.toString())
    }

    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Message::class.java, "message")
    }



}


