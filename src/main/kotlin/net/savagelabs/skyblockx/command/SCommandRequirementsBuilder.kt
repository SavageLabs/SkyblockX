package net.savagelabs.skyblockx.command

import net.savagelabs.skyblockx.core.Permission

class SCommandRequirementsBuilder {

	var asPlayer = false
	var asIslandMember = false
	var asLeader = false
	var permission: Permission? = null
	var rawPermission: String? = null


	fun asPlayer(value: Boolean): SCommandRequirementsBuilder {
		this.asPlayer = value
		return this
	}

	fun asLeader(value: Boolean): SCommandRequirementsBuilder {
		this.asLeader = value
		return this
	}

	fun withPermission(permission: Permission): SCommandRequirementsBuilder {
		this.permission = permission
		return this
	}

	fun asIslandMember(value: Boolean): SCommandRequirementsBuilder {
		// Gotta be a player to be a island member
		this.asIslandMember = value
		this.asPlayer = true
		return this
	}


	fun withRawPermission(permission: String): SCommandRequirementsBuilder {
		this.rawPermission = permission
		return this
	}

	fun build(): SCommandRequirements {
		return SCommandRequirements(
			permission,
			asIslandMember,
			asLeader,
			asPlayer,
			rawPermission
		)
	}

}


