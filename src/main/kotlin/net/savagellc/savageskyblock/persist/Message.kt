package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.serializer.Serializer
import net.savagellc.savageskyblock.core.color

object Message {

    @Transient
    private val instance = this

    var commandRequirementsNotAPlayer = "&cThis command requires the executor to be a player."
    var commandRequirementsNotAnIslandMember =
        "&cThis command requires the executor to be a island member, create one using \"/is create\"."
    var commandRequirementsPlayerDoesNotHavePermission = "&cThis command requires the permission %1\$s"

    var commandHelpGeneratorPageInvalid = "&cThe page %1\$s is invalid."
    var commandHelpGeneratorFormat = "&b/is %1\$s &8> &7 %2\$s"
    var commandHelpGeneratorNotRequired = "&c✗&r"
    var commandHelpGeneratorRequires = "&a✔&r"
    var commandHelpGeneratorIslandRequired = "&7Island member requirement: %1\$s"
    var commandHelpGeneratorClickMeToPaste = "&7Click me to paste \"%1\$s\" in chat bar."


    var commandParsingArgIsNotInt = "&cThis argument is not an integer, please make it one."
    var commandParsingPlayerDoesNotExist = "&cThis player does not exist."
    var commandParsingPlayerIsYou = "&cYou cannot reference yourself."


    var genericCommandsTooFewArgs = "&cThis command requires more arguments."
    var genericCommandsTooManyArgs = "&cThis command requires less arguments."
    var genericActionRequiresPermission = "&cThis action requires the permission %1\$s"


    var commandBaseHelp = "&7The base command for skyblock."
    var commandBaseHelpMessage = "&aPlease execute /is help."

    var commandCreateHelp = "&7Creates a skyblock island."
    var commandCreateAlreadyHaveAnIsland = "&7You already have an island, use /is delete to delete your island."

    var commandGoHelp = "&7Takes you to an island"
    var commandGoTeleporting = "&7Taking you to your island..."

    var commandDeleteHelp = "&7Deletes an island."
    var commandDeleteDeletedIsland = "&7Your island has been deleted."

    var commandSEPostionHelp = "&7positions for skyblock edit."
    var commandSEPositionInvalidIndex = "&7The specified position index is invalid, it must be either 1 or 2."
    var commandSEPosition = "&7Please right click a block to set it to position %1\$s"

    var commandSESaveStructureHelp = "&7This command saves a structure to a file name specified"
    var commandSESaveStructurePositionsNotSet =
        "You have not set both positions use the command \"/is pos\" to set the positions"

    var commandSEPasteStructurePasted = "&7Structure pasted successfully"

    var commandBypassToggle = "&7You are now %1\$s bypass mode"
    var commandBypassHelp = "&7bypass island checks (For Server Administrators)"


    var commandCoopInvokerSuccess = "&7You have added %1\$s to your island as co-op"
    var commandCoopMessageRecipient = "&7You have been added as a co-op player to %1\$s's island."
    var commandCoopCannotHaveMoreCoopPlayers =
        "You cannot have more co-op players, run &c/is coop&r to view current co-op players, and remove them using &c/is remove <player>"
    var commandCoopHelp = "&7set another player to co-op mode."

    var commandHomeGoSuccess = "&7You have been teleported to &b%1\$s"
    var commandHomeHomeSet = "&7You have set this location to your &b%1\$s&7 home."
    var commandHomeSetNotInIsland = "&7This location is not inside of the island's boundaries."
    var commandHomeCannotHaveMoreHomes = "&7You cannot have more homes set."
    var commandHomeListHeader = "&7&m-------&r &bHomes &7&m-------"
    var commandHomeListFormat = "&7&b%1\$s. &7%2\$s"
    var commandHomeListRemoveTooltip = "&7Click to remove!"
    var commandHomeHelp = "&7go to a use island homes."
    var commandHomeSetHelp = "&7set island homes."
    var commandHomeDoesNotExist = "&7This home does not exist."
    var commandHomeRemoveHelp = "&7remove homes."
    var commandHomeRemoveSuccess = "&7The home &b%1\$s&7 has been removed."
    var commandHomeGoHelp = "&7go to your homes."
    var commandHomeList = "&7list island homes."

    var commandHelpHelp = "&7view information about other commands."

    var commandRemoveInvokerSuccess = "&7You have removed %1\$s from your island."
    var commandRemoveInvokerCoopRemoved = "&7%1\$s's co-op status has been removed."
    var commandRemoveInvokerPlayerNotOnIsland = "&7The specified player's location is currently not on your island."
    var commandRemovedCoopStatus = "&7Your co-op status has been removed due to you being removed from the island."
    var commandRemoveHelp =
        "&7This command removes someone from your island, also removes co-op status if they have it."

    var commandTpHelp = "&7teleport to islands of other players."
    var commandTpPossibleLocationsHeader = "&7You can teleport to the following locations..."
    var commandTpPossibleLocationsFormat = "&7%1\$s. %2\$s's island."
    var commandTpThisIslandIsNotValid =
        "&7The player %1\$s does not own an island. Type &c/is tp &rto list possible locations."
    var commandTpTeleporting = "&7Teleporting you to %1\$s's island."
    var commandTpNoPermission = "&7The specified island does not have you as co-op or as a island member, "

    var islandCreateGUIYouDontHavePermission = "You do not have permission to use this island."

    var listenerVoidDeathPrevented = "&7You fell into the void, teleporting you back."
    var listenerBlockPlacementDenied = "&7You can only place blocks inside of your island."
    var listenerActionDeniedCreateAnIslandFirst = "&7This action has been denied, please create an island first."
    var listenerObsidianBucketLava = "&7The obsidian has been turned back to lava, be careful!"
    var listenerPlayerDamageCancelled = "&7Damage cancelled due to you not being on your island."

    var skyblockEditPositionSet = "&7You have set position %1\$s to %2\$s"
    var skyblockEditStructureSaved = "&7You have saved a structure to a file called %1\$s"
    var skyblockEditErrorPositionsNotInSameWorld = "&7Position 1 and position 2 are no in the same world."

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


