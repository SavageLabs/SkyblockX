package net.savagellc.savageskyblock.persist

import net.prosavage.baseplugin.serializer.Serializer

object Message {

    @Transient
    private val instance = this

    var commandRequirementsNotAPlayer = "This command requires the executor to be a player."
    var commandRequirementsNotAnIslandMember = "This command requires the executor to be a island member, create one using \"/is create\"."
    var commandRequirementsPlayerDoesNotHavePermission = "This command requires the permission %1\$s"


    var commandParsingArgIsNotInt = "This argument is not an integer, please make it one."


    var genericCommandsTooFewArgs = "This command requires more arguments."
    var genericCommandsTooManyArgs = "This command requires less arguments."


    var commandBaseHelp = "The base command for skyblock."
    var commandBaseHelpMessage = "Please execute /is help."

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





    var listenerBlockPlacementDenied = "You can only place blocks inside of your island."

    var skyblockEditPositionSet = "You have set position %1\$s to %2\$s"
    var skyblockEditStructureSaved = "You have saved a structure to a file called %1\$s"
    var skyblockEditErrorPositionsNotInSameWorld = "Position 1 and position 2 are no in the same world."


    fun save() {
        Serializer().save(instance)
    }

    fun load() {
        Serializer().load(instance, Message::class.java, "message")
    }



}