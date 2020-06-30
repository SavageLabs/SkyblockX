package net.savagelabs.skyblockx.persist

import net.savagelabs.savagepluginx.persist.container.ConfigContainer
import net.savagelabs.skyblockx.core.color
import org.bukkit.ChatColor

class Message : ConfigContainer {

    override val name = "message"

    companion object {
        lateinit var instance: Message
    }

    var messagePrefix = "&7[&b!&7] "

    var islandCreationMessage = "&7Island created with size %1\$s."

    var commandRequirementsNotAPlayer = "&cThis command requires the executor to be a player."
    var commandRequirementsNotAnIslandMember =
        "&cThis command requires the executor to be a island member, create one using \"/is create\"."
    var commandRequirementsNotAnIslandLeader = "&cThis command requires the executor to be an island leader."
    var commandRequirementsPlayerDoesNotHavePermission = "&cThis command requires the permission %1\$s"

    var commandHelpGeneratorPageInvalid = "&cThe page %1\$s does not exist."
    var commandHelpGeneratorFormat = "&b/%1\$s %2\$s &8> &7 %3\$s"
    var commandHelpGeneratorBackgroundColor = ChatColor.GRAY
    var commandHelpGeneratorNotRequired = "&cNo&r"
    var commandHelpGeneratorRequires = "&aYes&r"
    var commandHelpGeneratorIslandRequired = "&7Island member requirement: %1\$s"
    var commandHelpGeneratorClickMeToPaste = "&7Click me to autocomplete."
    var commandHelpGeneratorPageNavBack = "&b<<<"
    var commandHelpGeneratorPageNavNext = "&b>>>"


    var commandParsingArgIsNotInt = "&cThis argument is not an integer, please make it one."
    var commandParsingPlayerDoesNotExist = "&cThis player does not exist."
    var commandParsingArgIsNotBoolean = "&cThis argument is not a boolean, please make it 'true' or 'false'"
    var commandParsingPlayerIsYou = "&cYou cannot reference yourself."


    var genericCommandsTooFewArgs = "&cThis command requires more arguments."
    var genericCommandsTooManyArgs = "&cThis command requires less arguments."
    var genericActionRequiresPermission = "&cThis action requires the permission %1\$s"
    var genericCannotReferenceYourSelf = "&cYou cannot reference yourself."
    var genericPlayerNotAnIslandMember = "&cThis player is not an island member."


    var commandBaseHelp = "&7The base command for skyblock."
    var commandSkyblockBaseHelp = "&7The base admin command for skyblock."
    var commandBaseHelpMessage = "&aPlease execute /is help."
    var commandSkyblockBaseHelpMessage = "&aPlease execute /sb help"

    var commandCreateCLIHeader = "&7&m-------&r &bIsland Types &7&m-------"
    var commandCreateCLIFormat = "&7%1\$s. &b%2\$s"
    var commandCreateCLIFormatTooltip = "&7Click to paste &b/is create %1\$s&7 into your chatbar."
    
    var commandCreateHelp = "&7Creates a skyblock island."
    var commandCreateAlreadyHaveAnIsland = "&7You already have an island, use /is delete to delete your island."
    var commandCreateSuccess = "&7Your island was successfully created."
    var commandCreateCooldown = "&7Island Creation is on cooldown: &b%1\$s &7seconds left."

    var commandWorthHelp = "&7View island worth and level."
    var commandWorthValue = "&7Your island value is &b$%1\$s&7."
    var commandWorthLevel = "&7Your island level based off of your worth is Lv. &b%1\$s&7."

    var commandChestHelp = "&7Open the island's virtual chest."
    var commandChestOpening = "&7Opening the island chest."

    var commandGoHelp = "&7Takes you to an island."
    var commandGoTeleporting = "&7Taking you to your island..."

    var commandGoSetHelp = "&7Set the go point for the island."
    var commandGoSetYouMustBeOnYourIsland = "&7You must be on your island to execute this command."
    var commandGoSetSuccess = "&7You have successfully updated your &b/is go&7 location."

    var commandDeleteHelp = "&7Deletes an island."
    var commandDeleteDeletedIsland = "&7Your island has been deleted."

    var commandSEPostionHelp = "&7Positions for skyblock edit."
    var commandSEPosition = "&7Please right click a block to set it to position %1\$s."
    var commandSEPositionInvalidIndex = "&7The specified position index is invalid, it must be either 1 or 2."

    var skyblockEditPositionSet = "&7You have set position %1\$s to %2\$s"
    var skyblockEditErrorPositionsNotInSameWorld = "&7Position 1 and position 2 are no in the same world."
    var skyblockEditStructureSaved = "&7You have saved a structure to a file called %1\$s"

    var commandSESaveStructureHelp = "&7Saves a structure to a specified file."
    var commandSESaveStructurePositionsNotSet =
        "You have not set both positions use the command \"/is pos\" to set the positions."

    var commandSEPasteStructurePasted = "&7Structure pasted successfully."

    var commandBypassHelp = "&7Bypass island checks."
    var commandBypassToggle = "&7You are now %1\$s bypass mode."

    var commandTopHelp = "&7Gets island top info."
    var commandTopInvalidPage = "&7Invalid page"
    var commandTopIndexTooHigh = "&7This page does not exist."
    var commandTopNotCalculated =
        "&7Island values have not been calculated yet. Run &b/sbx calc&7 to force calculation."

    var commandCoopHelp = "&7Set another player to co-op mode."
    var commandCoopInvokerSuccess = "&7You have added %1\$s to your island as co-op"
    var commandCoopMessageRecipient = "&7You have been added as a co-op player to %1\$s's island."
    var commandCoopAuthorized =
        "&b%1\$s&7 has been authorized as a co-op player; Once &b%2\$s &7logs out, &b%1\$s's&7 co-op status will be removed."
    var commandCoopLoggedOut = "&7Your co-op access has expired because the player that authorized you logged out."
    var commandCoopCannotHaveMoreCoopPlayers =
        "You cannot have more co-op players, run &c/is coop&r to view current co-op players, and remove them using &c/is remove <player>."

    var commandHomeListHeader = "&7&m-------&r &bHomes &7&m-------"
    var commandHomeListFormat = "&7&b%1\$s. &7%2\$s"
    var commandHomeListRemoveTooltip = "&7Click to go!"
    var commandHomeList = "&7List island homes."
    var commandHomeSetHelp = "&7Set island homes."
    var commandHomeRemoveHelp = "&7Remove homes."
    var commandHomeGoHelp = "&7Go to your homes."

    var commandHomeHelp = "&7Opens a list with home commands."
    var commandHomeGoSuccess = "&7You have been teleported to &b%1\$s."
    var commandHomeHomeSet = "&7You have set this location to your &b%1\$s&7 home."
    var commandHomeSetNotInIsland = "&7This location is not inside of the island's boundaries."
    var commandHomeCannotHaveMoreHomes = "&7You cannot have more homes set."
    var commandHomeDoesNotExist = "&7This home does not exist."
    var commandHomeRemoveSuccess = "&7The home &b%1\$s&7 has been removed."

    var commandCalcMessage = "&7Calculated Island."
    var commandCalcCooldown = "&7You are trying to calculate the island value too often, Cooldown: &b%1\$s seconds left."
    var commandCalcHelp = "&7Calculate your own island's value."

    var commandBorderHelp = "&7Change your border color."

    var commandHelpHelp = "&7View information about other commands."
    var commandSkyblockHelpHelp = "&7View other admin command info."

    var commandRemoveHelp = "&7Removes island/co-op member."
    var commandRemoveInvokerSuccess = "&7You have removed %1\$s from your island."
    var commandRemoveInvokerCoopRemoved = "&7%1\$s's co-op status has been removed."
    var commandRemoveInvokerPlayerNotOnIsland = "&7The specified player's location is currently not on your island."
    var commandRemovedCoopStatus = "&7Your co-op status has been removed due to you being removed from the island."

    var commandVisitHelp = "&7Teleport to islands of other players."
    var commandVisitPossibleLocationsHeader = "&7You can teleport to the following locations..."
    var commandVisitPossibleLocationsFormat = "&7%1\$s. &b%2\$s's island."
    var commandVisitThisIslandIsNotValid =
        "&7The player %1\$s does not own an island. Type &b/is tp &r&7to list possible locations."
    var commandVisitTeleporting = "&7Teleporting you to %1\$s's island."
    var commandVisitNoPermission = "&7The specified island does not have you as co-op, an island member, or allow visitors."

    var commandJoinHelp = "&7Join an island."
    var commandJoinNotInvited = "&7You have not been invited to %1\$s's island."
    var commandJoinSuccess = "&7You have joined %1\$s's island."
    var commandJoinHaveIsland = "&7You already have an island."

    var commandRenameIslandNameIsTaken = "&7This island name is already in use."
    var commandRenameSuccess = "&7You have renamed your island to &6%1\$s."
    var commandRenameHelp = "rename your island."

    var commandQuestHelp = "&7View Quest GUI."

    var commandReloadHelp = "&7Reloads the skyblock config files."
    var commandReloadSuccess = "&7Reloaded configs and saved Data.instance."

    var commandMenuHelp = "&7Opens the Island Menu GUI."

    var islandCreateGUIYouDontHavePermissionToUseIsland =
        "You do not have the permission &b%1\$s&7 required to use this island."

    var commandMemberInviteHelp = "invite a member to your island"
    var commandMemberInviteSuccess = "&7You have invited %1\$s to your island."
    var commandMemberInviteMessage = "&7%1\$s has invited you to their island, click to accept!"
    var commandMemberInviteLimit = "&7You cannot invite more members to your island, the limit is %1\$s."

    var commandMemberKickHelp = "&7Remove member from island."
    var commandMemberKickLimit = "&7There are no members to remove!"
    var commandMemberKickNotFound = "&7Your island does not have this member, names are &cCase Sensitive!"
    var commandMemberKicked = "&7You have successfully removed %1\$s from your island."

    var commandMemberPromoteHelp = "&7Promote a member to leader."
    var commandMemberNoMembers = "&7Your island does not have any members to promote."
    var commandMemberPromoteNotFound = "&7Your island does not have this member, names are &cCase Sensitive!"
    var commandMemberPromotedSuccess = "&b%1\$s&7 was promoted to leader."
    var commandMemberPromoteYouHaveBeenPromoted = "&7You have been promoted to &bisland leader&7."

    var commandMemberListHelp = "&7List all the members."
    var commandMemberListHeader = "&7&m-------&r &bMembers &7&m-------"
    var commandMemberListFormat = "&7&b%1\$s. &7%2\$s"
    var commandMemberListRemoveTooltip = "&7Click to remove!"

    var commandMemberHelp = "&7Member commands."
    var commandMemberAlreadyPartOfIsland = "&7This player is already in your island"

    var commandLeaveHelp = "&7Leave your island."
    var commandLeaveSuccess = "&7You have successfully left your island."
    var commandLeaveMemberLeftIsland = "&7%1\$s has left your island."
    var commandLeaveDeniedLeader =
        "&7You cannot leave an island as a leader, promote someone else to leader, or delete it."

    var commandResetHelp = "&7Reset your island."

    var commandValueHelp = "&7Get the monetary value of a block you're holding."
    var commandValueInfo = "&7The value of this block is &b%1\$s"

    var commandSkyblockOpenChestHelp = "Open an island chest."
    var commandSkyblockOpenChestNotAnIslandMember = "&7This player is &bnot&7 an island member."
    var commandSkyblockOpenChestOpening = "&7Opening target island's chest..."

    var commandSkyblockRemoveHelp = "&7Delete a player's island."
    var commandSkyblockRemoveSuccess = "&7The island was successfully deleted."
    var commandSkyblockRemoveNotAnIslandOwner = "&7This player is &bnot&7 an island owner."

    var commandSkyblockKickHelp = "&7Kick a player from their island."
    var commandSkyblockKickMemberKicked = "&b%1\$s was kicked from their island."
    var commandSkyblockKickMemberKickedOwner = "&b%1\$s is now the owner of the island."
    var commandSkyblockKickIslandDeleted = "&7The island has been deleted since the island has no members left to inherit the island."

    var commandSkyblockNewOwnerHelp = "&7Set a new owner for an island."
    var commandSkyblockNewOwnerSuccess = "&7New owner successfully set."

    var commandSkyblockCalcHelp = "&7Calculates every island's price."
    var commandSkyblockCalcDone = "&7Calculated %1\$s islands."
    var commandSkyblockCalcStart = "&7Starting Calculation..."

    var commandAllowVisitorsHelp = "&7Allow visitors to your island using /is teleport <owner-name>."
    var commandAllowVisitorsStatus = "&7Your visitor allowed status is now set to &b%1\$s&7."

    var commandBiomeHelp = "&7Change your island's biome."
    var commandBiomeSuccess = "&7You have changed your island's biome to %1\$s, you need to re-log to see changes."
    var commandBiomeInvalidBiome = "&7The specified biome is invalid."


    var listenerVoidDeathPrevented = "&7You fell into the void, teleporting you back."
    var listenerDeathTeleport = "&7You've been teleported to your island."
    var listenerBlockPlacementDenied = "&7You can only place blocks inside of your island."
    var listenerActionDeniedCreateAnIslandFirst = "&7This action has been denied, please create an island first."
    var listenerObsidianBucketLava = "&7The obsidian has been turned back to lava, be careful!"
    var listenerPlayerCannotInteract = "&7You cannot interact with this block."


    var questActivationTrigger = "&7You have activated the quest &b{quest}"
    var questIsOneTimeAndAlreadyCompleted = "&7This quest is a onetime quest and is already completed."
    var questOrderNoNextQuestWasFound = "&7No next quest was found."
    var nextQuestMessage = "&7Your next quest is &b%1\$s"
    var questInProgressPlaceholder = "&a&lIN PROGRESS"
    var questCompletedPlaceholder = "&b&lCOMPLETED"
    var questNotStarted = "&c&lNOT STARTED"
    var questProgressCompletedChar = "&b="
    var questProgressInCompleteChar = "&7="
    var questProgressBarMessage = "&b&l{quest-name} {progress-bar} &b{percentage}%"

    var islandNetherTeleported = "&7You have been teleported to your nether island."

    var commandCreateNonAlphaNumeric = "&7This island tag is not &balphanumeric&7."
    var commandCreateLength = "&7island tag must be between &b%1\$s&7 and &b%2\$s&7 characters long."

    var islandCreatedTitle = "&9SkyblockX"
    var islandCreatedSubtitle = "&7By: ProSavage"


    override fun toString(): String {
        return color(super.toString())
    }


}


