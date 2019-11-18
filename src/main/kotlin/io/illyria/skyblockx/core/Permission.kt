package io.illyria.skyblockx.core

import io.illyria.skyblockx.persist.Config
import org.bukkit.entity.HumanEntity
import org.bukkit.permissions.Permissible
import org.bukkit.permissions.PermissionAttachmentInfo
import java.util.concurrent.atomic.AtomicInteger


enum class Permission(val node: String, val description: String) {
    CREATE("create", "create an island."),
    GO("go", "go to your island."),
    DELETE("delete", "delete your island."),
    BYPASS("bypass", "administrator bypass permisssions."),
    BORDER("border", "update your island border."),
    COOP("coop", "co-op a player."),
    MEMBER("member", "manage island members."),
    REMOVE("remove", "remove/expel a player from your island."),
    TELEPORT("teleport", "teleport to another island."),
    LEAVE("leave", "leave your island."),
    MENU("menu", "open the island menu."),
    HOME("home", "manage island homes."),
    ALLOWVISITOR("allowvisitor", "allow visitors on your island."),
    QUEST("quest", "opening the questing gui."),
    OBSIDIANTOLAVA("obsidiantolava", "turn obsidian to lava with an empty bucket"),
    SE_SAVESTUCT("se.savestructure", "administrator skyblock edit save"),
    SE_PASTESTRUCT("se.pastestructure", "administrator skyblock edit paste"),
    SE_REGIONS("se.regions", "set positions and make a region");


    fun getFullPermissionNode(): String {
        return "${Config.skyblockPermissionPrefix}.${this.node}"
    }

}


fun hasPermission(humanEntity: HumanEntity, permission: Permission): Boolean {
    return humanEntity.hasPermission(permission.getFullPermissionNode())
}

fun hasPermission(humanEntity: HumanEntity, permission: String): Boolean {
    return humanEntity.hasPermission(permission)
}

fun getMaxPermission(permissable: Permissible, permission: String): Int {
    if (permissable.isOp) {
        return -1
    }

    // Atomic cuz values need to be final to be accessed from lambda.
    val max = AtomicInteger()

    permissable.effectivePermissions.stream()
        .map(PermissionAttachmentInfo::getPermission)
        .map { perm -> perm.toString().toLowerCase() }
        .filter { permString -> permString.startsWith(permission) }
        .map { permString -> permString.replace("$permission.", "") }
        .forEach { value ->
            // If the value is *, then its basically infinity
            if (value.equals("*", true)) {
                max.set(-1)
                return@forEach
            }

            // Other foreach set it to -1.
            if (max.get() == -1) {
                return@forEach
            }

            try {
                // Get the int from name
                val amount = Integer.parseInt(value)

                // Check if our value is bigger than the one we have.
                if (amount > max.get()) {
                    max.set(amount)
                }
            } catch (exception: NumberFormatException) {
                // hehe you got ignored like women ignore me
            }
        }

    return max.get()
}