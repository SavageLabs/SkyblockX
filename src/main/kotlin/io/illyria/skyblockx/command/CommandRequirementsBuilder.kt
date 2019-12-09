package io.illyria.skyblockx.command

import io.illyria.skyblockx.core.Permission

class CommandRequirementsBuilder {

    var asPlayer = false
    var asIslandMember = false
    var asLeader = false
    var permission: Permission? = null


    fun asPlayer(value: Boolean): CommandRequirementsBuilder {
        this.asPlayer = value
        return this
    }

    fun asLeader(value: Boolean): CommandRequirementsBuilder {
        this.asLeader = value
        return this
    }

    fun withPermission(permission: Permission): CommandRequirementsBuilder {
        this.permission = permission
        return this
    }

    fun asIslandMember(value: Boolean): CommandRequirementsBuilder {
        // Gotta be a player to be a island member
        this.asIslandMember = value
        this.asPlayer = true
        return this
    }

    fun build(): CommandRequirements {
        return CommandRequirements(permission, asPlayer, asIslandMember, asLeader)
    }


}