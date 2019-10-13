package io.illyria.skyblockx.command

import io.illyria.skyblockx.core.Permission

class CommandRequirementsBuilder {

    var asPlayer = false
    var asIslandMember = false
    var permission: Permission? = null


    fun asPlayer(value: Boolean): io.illyria.skyblockx.command.CommandRequirementsBuilder {
        this.asPlayer = value
        return this
    }

    fun withPermission(permission: Permission): io.illyria.skyblockx.command.CommandRequirementsBuilder {
        this.permission = permission
        return this
    }

    fun asIslandMember(value: Boolean): io.illyria.skyblockx.command.CommandRequirementsBuilder {
        // Gotta be a player to be a island member
        this.asIslandMember = value
        this.asPlayer = true
        return this
    }

    fun build(): io.illyria.skyblockx.command.CommandRequirements {
        return io.illyria.skyblockx.command.CommandRequirements(permission, asPlayer, asIslandMember)
    }


}