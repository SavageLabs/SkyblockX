package net.savagelabs.skyblockx.persist

import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.savagepluginx.persist.container.ConfigContainer
import net.savagelabs.skyblockx.core.color

class Message : ConfigContainer {

	@JsonIgnore
	override val name = "message"

	companion object {
		lateinit var instance: Message
	}

	var messagePrefix = "&7[&a!&7] "

	var islandCreationMessage = "&7Island created with size %1\$s."

	var commandRequirementsNotAnIslandMember =
			"&cThis command requires the executor to be a island member, create one using \"/is create\"."
	var commandRequirementsNotAnIslandLeader = "&cThis command requires the executor to be an island leader."
	var commandRequirementsPlayerDoesNotHavePermission = "&cThis command requires the permission %1\$s"

	var commandParsingPlayerDoesNotExist = "&cThis player does not exist."
	var commandParsingPlayerIsYou = "&cYou cannot reference yourself."

	var genericActionRequiresPermission = "&cThis action requires the permission %1\$s"
	var genericCannotReferenceYourSelf = "&cYou cannot reference yourself."
	var genericPlayerNotAnIslandMember = "&cThis player is not an island member."
	var genericPlayerPaid = "&7You have paid %1\$s."
	var genericPlayerDidntPay = "&7You dont have %1\$s."


	var commandBaseHelp = "&7The base command for skyblock."
	var commandBaseHelpMessage = "&aPlease execute /is help."
	var commandSkyblockBaseHelp = "&7The base admin command for skyblock."
	var commandSkyblockBaseHelpMessage = "&aPlease execute /sbx help."


	var commandCreateHelp = "&7Creates a skyblock island."
	var commandCreateAlreadyHaveAnIsland = "&7You already have an island, use /is delete to delete your island."
	var commandCreateSuccess = "&7Your island was successfully created."
	var commandCreateCooldown = "&7Island Creation is on cooldown: &e%1\$s &7seconds left."

	var commandCreateCLIHeader = "&7&m-------&r &aIsland Types &7&m-------"
	var commandCreateCLIFormat = "&7%1\$s. &a%2\$s"
	var commandCreateCLIFormatTooltip = "&7Click to paste &a/is create %1\$s&7 into your chatbar."

	var islandCreateGUIYouDontHavePermissionToUseIsland =
			"&cYou do not have the permission &a%1\$s&7 required to use this island."

	var commandGoHelp = "&7Takes you to an island."
	var commandGoTeleporting = "&7Taking you to your island..."

	var commandDeleteHelp = "&7Deletes an island."
	var commandDeleteDeletedIsland = "&7Your island has been deleted."

	var commandCoopHelp = "&7Set another player to co-op mode."
	var commandCoopInvokerSuccess = "&7You have added %1\$s to your island as co-op."
	var commandCoopMessageRecipient = "&7You have been added as a co-op player to %1\$s's island."
	var commandCoopAuthorized =
			"&a%1\$s&7 has been authorized as a co-op player; Once &a%2\$s &7logs out, &a%1\$s's&7 co-op status will be removed."
	var commandCoopLoggedOut = "&7Your co-op access has expired because the player that authorized you logged out."
	var commandCoopCannotHaveMoreCoopPlayers =
			"You cannot have more co-op players, run &c/is coop&r to view current co-op players, and remove them using &c/is remove <player>."

	var commandRemoveHelp = "&7Removes island/co-op member."
	var commandRemoveAlreadyInIsland = "&7This member is part of your island."
	var commandRemoveInvokerSuccess = "&7You have removed &a%1\$s &7from your island."
	var commandRemoveInvokerCoopRemoved = "&7%1\$s's co-op status has been removed."
	var commandRemoveInvokerPlayerNotOnIsland = "&7The specified player's location is currently not on your island."
	var commandRemovedCoopStatus = "&7Your co-op status has been removed due to you being removed from the island."

	var commandVisitHelp = "&7Teleport to islands of other players."
	var commandVisitPossibleLocationsHeader = "&7You can teleport to the following locations..."
	var commandVisitPossibleLocationsFormat = "&7%1\$s. &a%2\$s's island."
	var commandVisitThisIslandIsNotValid =
			"&7The player %1\$s does not own an island. Type &a/is tp &r&7to list possible locations."
	var commandVisitTeleporting = "&7Teleporting you to %1\$s's island."
	var commandVisitNoPermission =
			"&7The specified island does not have you as co-op, an island member, or allow visitors."

	var commandHomeHelp = "&7Opens a list with home commands."
	var commandHomeGoSuccess = "&7You have been teleported to &a%1\$s."
	var commandHomeHomeSet = "&7You have set this location to your &a%1\$s&7 home."
	var commandHomeSetNotInIsland = "&cThis location is not inside of the island's boundaries."
	var commandHomeCannotHaveMoreHomes = "&cYou cannot have more homes set."
	var commandHomeDoesNotExist = "&cThis home does not exist."
	var commandHomeRemoveSuccess = "&7The home &a%1\$s&7 has been removed."
	var commandHomeListHeader = "&7&m-------&r &aHomes &7&m-------"
	var commandHomeListFormat = "&7&a%1\$s. &7%2\$s"
	var commandHomeListRemoveTooltip = "&7Click to go!"
	var commandHomeList = "&7List island homes."
	var commandHomeSetHelp = "&7Set island homes."
	var commandHomeRemoveHelp = "&7Remove homes."
	var commandHomeGoHelp = "&7Go to your homes."

	var commandHelpHelp = "&7View information about other commands."

	var commandQuestHelp = "&7View Quest GUI."

	var commandMemberHelp = "&7Member commands."
	var commandMemberAlreadyPartOfIsland = "&7This player is already in your island."

	var commandMemberListHelp = "&7List all the members."
	var commandMemberListHeader = "&7&m-------&r &aMembers &7&m-------"
	var commandMemberListFormat = "&7&a%1\$s. &7%2\$s"
	var commandMemberListRemoveTooltip = "&7Click to remove!"

	var commandMenuHelp = "&7Opens the Island Menu GUI."

	var commandJoinHelp = "&7Join an island."
	var commandJoinNotInvited = "&7You have not been invited to %1\$s's island."
	var commandJoinSuccess = "&7You have joined %1\$s's island."
	var commandJoinHaveIsland = "&cYou already have an island."

	var commandBorderHelp = "&7Change your border color."

	var commandMemberKickHelp = "&7Remove member from island."
	var commandMemberKickLimit = "&cThere are no members to remove!"
	var commandMemberKickNotFound = "&7Your island does not have this member, names are &cCase Sensitive&7!"
	var commandMemberKicked = "&7You have successfully removed %1\$s from your island."

	var commandMemberInviteHelp = "&7Invite a member to your island"
	var commandMemberInviteSuccess = "&7You have invited &a%1\$s &7to your island."
	var commandMemberInviteMessage = "&a%1\$s &7has invited you to their island, click to accept!"
	var commandMemberInviteLimit = "&7You cannot invite more members to your island, the limit is %1\$s."

	var commandLeaveHelp = "&7Leave your island."
	var commandLeaveSuccess = "&7You have successfully left your island."
	var commandLeaveMemberLeftIsland = "&7%1\$s has left your island."
	var commandLeaveDeniedLeader =
			"&7You cannot leave an island as a leader, promote someone else to leader, or delete it."

	var commandMemberPromoteHelp = "&7Promote a member to leader."
	var commandMemberNoMembers = "&cYour island does not have any members to promote."
	var commandMemberPromoteNotFound = "&7Your island does not have this member, names are &cCase Sensitive&7!"
	var commandMemberPromotedSuccess = "&a%1\$s&7 was promoted to leader."
	var commandMemberPromoteYouHaveBeenPromoted = "&7You have been promoted to &aisland leader&7."

	var commandResetHelp = "&7Reset your island."

	var commandAllowVisitorsHelp = "&7Allow visitors to your island using /is teleport <owner-name>."
	var commandAllowVisitorsStatus = "&7Your visitor allowed status is now set to &a%1\$s&7."

	var commandTopHelp = "&7Gets island top info."
	var commandTopInvalidPage = "&7Invalid page"
	var commandTopIndexTooHigh = "&7This page does not exist."
	var commandTopNotCalculated =
			"&7Island values have not been calculated yet. Run &a/sbx calc&7 to force calculation."

	var commandCalcHelp = "&7Calculate your own island's value."
	var commandCalcStart = "&7Starting Island Calc."
	var commandCalcMessage = "&7Calculated Island. (%1\$s)"
	var commandCalcCooldown =
			"&7You are trying to calculate the island value too often! Cooldown: &a%1\$s seconds left."

	var commandValueHelp = "&7Get the monetary value of a block you're holding."
	var commandValueInfo = "&7The value of this block is &a%1\$s"

	var commandChestHelp = "&7Open the island's virtual chest."
	var commandChestOpening = "&7Opening the island chest."

	var commandBiomeHelp = "&7Change your island's biome."
	var commandBiomeSuccess = "&7You have changed your island's biome to &a%1\$s&7, you need to re-log to see changes."
	var commandBiomeInvalidBiome = "&7The specified biome is invalid."

	var commandGoSetHelp = "&7Set the go point for the island."
	var commandGoSetYouMustBeOnYourIsland = "&7You must be on your island to execute this command."
	var commandGoSetSuccess = "&7You have successfully updated your &a/is go&7 location."

	var commandWorthHelp = "&7View island worth and level."
	var commandWorthValue = "&7Your island value is &a$%1\$s&7."
	var commandWorthLevel = "&7Your island level based off of your worth is Lv. &a%1\$s&7."

	var commandRenameHelp = "Rename your island."
	var commandRenameIslandNameIsTaken = "&7This island name is already in use."
	var commandRenameSuccess = "&7You have renamed your island to &6%1\$s."

	var commandUpgradesHelp = "&7View the upgrades menu."
	var commandUpgradesOpening = "&7Opening upgrade menu..."

	var commandChatHelp = "&7Switch between global and island chat."
	var commandChatOn = "on"
	var commandChatOff = "off"
	var commandChatChange = "&7You have turned island chat &a%1\$s&7."


	var commandSEPostionHelp = "&7Positions for skyblock edit."
	var commandSEPosition = "&7Please right click a block to set it to position %1\$s."
	var commandSEPositionInvalidIndex = "&7The specified position index is invalid, it must be either 1 or 2."
	var skyblockEditPositionSet = "&7You have set position &a%1\$s &7to &a%2\$s"
	var skyblockEditErrorPositionsNotInSameWorld = "&7Position 1 and position 2 are no in the same world."
	var skyblockEditStructureSaved = "&7You have saved a structure to a file called &a%1\$s&7."

	var commandSESaveStructureHelp = "&7Saves a structure to a specified file."
	var commandSESaveStructurePositionsNotSet =
			"You have not set both positions use the command \"/is pos\" to set the positions."

	var commandSEPasteStructurePasted = "&7Structure pasted successfully."

	var commandSkyblockRemoveHelp = "&7Delete a player's island."
	var commandSkyblockRemoveSuccess = "&7The island was successfully deleted."
	var commandSkyblockRemoveNotAnIslandOwner = "&7This player is &cnot&7 an island owner."

	var commandSkyblockKickHelp = "&7Kick a player from their island."
	var commandSkyblockKickMemberKicked = "&a%1\$s was kicked from their island."
	var commandSkyblockKickMemberKickedOwner = "&a%1\$s is now the owner of the island."
	var commandSkyblockKickIslandDeleted =
			"&7The island has been deleted since the island has no members left to inherit the island."

	var commandSkyblockNewOwnerHelp = "&7Set a new owner for an island."
	var commandSkyblockNewOwnerSuccess = "&7New owner successfully set."

	var commandReloadHelp = "&7Reloads the skyblock config files."
	var commandReloadSuccess = "&7Reloaded configs and saved Data.instance."

	var commandSkyblockHelpHelp = "&7View other admin command info."

	var commandSkyblockCalcHelp = "&7Calculates every island's price."
	var commandSkyblockCalcStart = "&7Starting Calculation..."

	var commandBypassHelp = "&7Bypass island checks."
	var commandBypassToggle = "&7You are now %1\$s bypass mode."

	var commandSkyblockOpenChestNotAnIslandMember = "&7This player is &cnot&7 an island member."
	var commandSkyblockOpenChestOpening = "&7Opening target island's chest..."

	var listenerVoidDeathPrevented = "&eYou fell into the void, teleporting you back."
	var listenerDeathTeleport = "&eYou've been teleported to your island."
	var listenerBlockPlacementDenied = "&cYou can only place blocks inside of your island."
	var listenerActionDeniedCreateAnIslandFirst = "&cThis action has been denied, please create an island first."
	var listenerObsidianBucketLava = "&6The obsidian has been turned back to lava, be careful!"
	var listenerPlayerCannotInteract = "&cYou cannot interact with this block."

	var questActivationTrigger = "&7You have activated the quest &a{quest}&7."
	var questIsOneTimeAndAlreadyCompleted = "&7This quest is a onetime quest and is already completed."
	var questOrderNoNextQuestWasFound = "&7No next quest was found."
	var nextQuestMessage = "&7Your next quest is &a%1\$s"
	var questInProgressPlaceholder = "&e&lIN PROGRESS"
	var questCompletedPlaceholder = "&a&lCOMPLETED"
	var questNotStarted = "&c&lNOT STARTED"
	var questProgressCompletedChar = "&a="
	var questProgressInCompleteChar = "&7="
	var questProgressBarMessage = "&a&l{quest-name} {progress-bar} &a{percentage}%"

	var islandNetherTeleported = "&7You have been teleported to your nether island."
	var islandEndTeleported = "&7You have been teleported to your end island."

	var commandCreateNonAlphaNumeric = "&7This island tag is not &calphanumeric&7."
	var commandCreateLength = "&7Island tag must be between &c%1\$s&7 and &c%2\$s&7 characters long."

	var chestShopSpecifyMaterial = "&cPlease specify a valid Material for your shop."
	var chestShopSpecifyAmount = "&cPlease specify a valid amount for your shop."
	var chestShopSpecifyPrice = "&cPlease specify a valid price for your shop."
	var chestShopAmountTooHigh = "&cYou have exceeded the maximum amount to sell. Please choose between 1-%s."
	var chestShopCreationAtOtherIsland = "&cYou can only create shops on your own island!"
	var chestShopAlreadyExist = "&cThere already is a shop related to this chest!"
	var chestShopInteractingIsOwner = "&cYou cannot buy/sell from your own shop!"
	var chestShopCreationSuccess = listOf(
			"&7You have created a new shop with the settings:",
			"&6 &6&l* &7Material: &6%1\$s",
			"&6 &6&l* &7Amount: &6%2\$s",
			"&6 &6&l* &7Price: &6%3\$s",
			"&6 &6&l* &7Type: &6%4\$s"
	)
	var chestShopNotInStock = "&cThis shop is out of stock!"
	var chestShopPlayerInsufficientFunds = "&cYou do not have &n$%s&c to spend on this shop!"
	val chestShopPlayerNotInStock = "&cYou do not have enough items to sell here!"
	val chestShopNoSpace = "&cThis shop is currently full, notify the owner and try again later!"
	val chestShopInsufficientFunds = "&cThis shop's owner does not have enough funds to sell, try notifying them!"
	var chestShopPurchased = "&7You have purchased &ax%s &7of &a%s &7from &a&n%s&7 for &a\$%s&7."
	var chestShopPurchasedSeller = "&7You have sold &ax%s &7of &a%s &7to &a&n%s&7 for &a\$%s&7."
	var chestShopSold = "&7You have sold &ax%s &7of &a%s &7to &a&n%s&7 for &a\$%s&7."
	var chestShopSoldSeller = "&a&n%s&7 has sold &ax%s &7of &a%s &7to you for &a\$%s&7."

	var placementLimitReached = "&7Your island has reached the placement limit for &a%s&7!"

	var islandCreatedTitle = "&9SkyblockX"
	var islandCreatedSubtitle = "&7By: ProSavage"

	override fun toString(): String {
		return color(super.toString())
	}
}