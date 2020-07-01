package net.savagelabs.savagepluginx.persist

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import net.savagelabs.savagepluginx.persist.container.ConfigContainer
import org.bukkit.ChatColor


@JsonIgnoreProperties(ignoreUnknown = true)
class BaseConfig: ConfigContainer {


    companion object {
        lateinit var instance: BaseConfig
    }

    @JsonIgnore
    override val name: String = "storage-config"

    val commandEnginePrefix = "&7[&b!&7] "

    val helpGeneratorPageEntries = 10

    val commandRequirementsNotAPlayer = "&cThis command requires the executor to be a player."
    val commandRequirementsPlayerDoesNotHavePermission = "&cThis command requires the permission %1\$s"

    val commandHelpGeneratorPageInvalid = "&cThe page %1\$s does not exist."
    val commandHelpGeneratorFormat = "&b/%1\$s %2\$s &8> &7 %3\$s"
    val commandHelpGeneratorBackgroundColor = ChatColor.GRAY
    val commandHelpGeneratorClickMeToPaste = "&7Click me to autocomplete."
    val commandHelpGeneratorPageNavBack = "&b<<<"
    val commandHelpGeneratorPageNavNext = "&b>>>"


    val commandParsingArgIsNotInt = "&cThis argument is not an integer, please make it one."
    val commandParsingArgIsNotDouble = "&cThis argument is not a number, please make it one."
    val commandParsingPlayerDoesNotExist = "&cThis player does not exist."
    val commandParsingArgIsNotBoolean = "&cThis argument is not a boolean, please make it 'true' or 'false'"

    var commandEngineFormatHoverable = "&7&o((Hoverable))&r"
    var commandEngineFormatPrefix = " /%1\$s %2\$s"
    var commandEngineFormatRequiredArg = "<%1\$s>"
    var commandEngineFormatRequiredTooltip = "This argument is required."
    var commandEngineFormatOptionalArg = "(%1\$s)"
    var commandEngineFormatOptionalTooltip = "The argument is optional"

    val genericCommandTooFewArgs = "&cThis command requires more arguments."
    val genericCommandTooManyArgs = "&cThis command requires less arguments."

}