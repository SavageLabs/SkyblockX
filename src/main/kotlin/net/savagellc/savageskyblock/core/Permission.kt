package net.savagellc.savageskyblock.core

import net.savagellc.savageskyblock.persist.Config
import org.bukkit.entity.HumanEntity

enum class Permission(val node: String) {
    CREATE("create"),
    GO("go"),
    DELETE("delete"),
    SE_REGIONS("se.regions")

}


fun hasPermission(humanEntity: HumanEntity, permission: Permission): Boolean {
    return humanEntity.hasPermission("${Config.skyblockPermissionPrefix}.${permission.node}")
}