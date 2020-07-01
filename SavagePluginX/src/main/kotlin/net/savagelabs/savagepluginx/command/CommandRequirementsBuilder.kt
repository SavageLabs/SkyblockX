package net.savagelabs.savagepluginx.command

class CommandRequirementsBuilder {

    var asPlayer = false
    var rawPermission: String? = null


    fun asPlayer(value: Boolean): CommandRequirementsBuilder {
        this.asPlayer = value
        return this
    }

    fun withRawPermission(permission: String): CommandRequirementsBuilder {
        this.rawPermission = permission
        return this
    }

}