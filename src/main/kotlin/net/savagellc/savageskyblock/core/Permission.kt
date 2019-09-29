package net.savagellc.savageskyblock.core

import net.savagellc.savageskyblock.persist.Config
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.permissions.Permissible
import org.bukkit.permissions.PermissionAttachmentInfo
import java.util.concurrent.atomic.AtomicInteger


enum class Permission(val node: String) {
    CREATE("create"),
    GO("go"),
    DELETE("delete"),
    BYPASS("bypass"),
    COOP("coop"),
    REMOVE("remove"),
    TELEPORT("teleport"),
    HOME("home"),
    OBSIDIANTOLAVA("obsidiantolava"),
    SE_SAVESTUCT("se.savestructure"),
    SE_PASTESTRUCT("se.pastestructure"),
    SE_REGIONS("se.regions");


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
                // Get the int from string
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