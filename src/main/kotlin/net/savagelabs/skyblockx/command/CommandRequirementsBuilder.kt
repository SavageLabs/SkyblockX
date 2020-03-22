package net.savagelabs.skyblockx.command

import io.illyria.skyblockx.core.Permission

class CommandRequirementsBuilder {

    var asPlayer = false
    var asIslandMember = false
    var asLeader = false
    var permission: Permission? = null


    fun asPlayer(value: Boolean): _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder {
        this.asPlayer = value
        return this
    }

    fun asLeader(value: Boolean): _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder {
        this.asLeader = value
        return this
    }

    fun withPermission(permission: Permission): _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder {
        this.permission = permission
        return this
    }

    fun asIslandMember(value: Boolean): _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder {
        // Gotta be a player to be a island member
        this.asIslandMember = value
        this.asPlayer = true
        return this
    }

    fun build(): _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirements {
        return _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirements(
            permission,
            asPlayer,
            asIslandMember,
            asLeader
        )
    }


}