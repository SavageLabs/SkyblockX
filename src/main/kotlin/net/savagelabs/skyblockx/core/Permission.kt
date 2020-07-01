package net.savagelabs.skyblockx.core

import net.savagelabs.skyblockx.persist.Config
import org.bukkit.entity.HumanEntity
import org.bukkit.permissions.Permissible
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.PluginManager
import java.util.concurrent.atomic.AtomicInteger


enum class Permission(val node: String, val description: String, val permissionDefault: PermissionDefault) {
    CREATE("create", "create an island.", PermissionDefault.TRUE),
    RELOAD("reload", "reload the plugin", PermissionDefault.OP),
    GO("go", "go to your island.", PermissionDefault.TRUE),
    GO_SET("go.set", "set the go point of the island", PermissionDefault.TRUE),
    DELETE("delete", "delete your island.", PermissionDefault.TRUE),
    BORDER("border", "update your island border.", PermissionDefault.TRUE),
    COOP("coop", "co-op a player.", PermissionDefault.TRUE),
    MEMBER("member", "manage island members.", PermissionDefault.TRUE),
    REMOVE("remove", "remove/expel a player from your island.", PermissionDefault.TRUE),
    BIOME("biome", "set the island biome", PermissionDefault.TRUE),
    TELEPORT("teleport", "teleport to another island.", PermissionDefault.TRUE),
    LEAVE("leave", "leave your island.", PermissionDefault.TRUE),
    RENAME("rename", "rename your island.", PermissionDefault.TRUE),
    MENU("menu", "open the island menu.", PermissionDefault.TRUE),
    RESET("reset", "reset your island.", PermissionDefault.TRUE),
    HOME("home", "manage island homes.", PermissionDefault.TRUE),
    WORTH("worth", "view your island's value and level", PermissionDefault.TRUE),
    ALLOWVISITOR("allowvisitor", "allow visitors on your island.", PermissionDefault.TRUE),
    QUEST("quest", "opening the questing gui.", PermissionDefault.TRUE),
    OBSIDIANTOLAVA("obsidiantolava", "turn obsidian to lava with an empty bucket", PermissionDefault.FALSE),
    CALC("calc", "calculate your island's value", PermissionDefault.TRUE),
    CHEST("chest", "open your island chest", PermissionDefault.TRUE),
    INFO("info", "gets island top information", PermissionDefault.TRUE),
    SE_SAVESTUCT("se.savestructure", "administrator skyblock edit save", PermissionDefault.OP),
    SE_PASTESTRUCT("se.pastestructure", "administrator skyblock edit paste", PermissionDefault.OP),
    SE_REGIONS("se.regions", "set positions and make a region", PermissionDefault.OP),
    ADMIN_DELETEISLAND("admin.deleteisland", "delete island of a player", PermissionDefault.OP),
    ADMIN_OPENCHEST("admin.openchest", "open a island's chest", PermissionDefault.OP),
    ADMIN_NEWOWNER("admin.newowner", "change an island's owner", PermissionDefault.OP),
    ADMIN_KICKFROMISLAND("admin.kickfromisland", "kick a player from their island.", PermissionDefault.OP),
    ADMIN_BYPASS("admin.bypass", "bypass any island restrictions", PermissionDefault.OP),
    ADMIN_CALC("admin.calc", "calculate all island's value", PermissionDefault.OP);


    fun getFullPermissionNode(): String {
        return "${Config.instance.skyblockPermissionPrefix}.${this.node}"
    }

}

fun registerAllPermissions(pluginManager: PluginManager) {
    Permission.values().forEach { permission ->
        // Check if existing for hot-reload :)
        if (pluginManager.getPermission(permission.getFullPermissionNode()) == null) {
            pluginManager.addPermission(
                org.bukkit.permissions.Permission(
                    permission.getFullPermissionNode(),
                    permission.description,
                    permission.permissionDefault
                )
            )
        }
        Config.instance.islandCreateGUIIslandTypes.forEach { islandType ->
            if (pluginManager.getPermission(islandType.requirementPermission) == null) {
                pluginManager.addPermission(
                    org.bukkit.permissions.Permission(
                        islandType.requirementPermission,
                        islandType.name,
                        PermissionDefault.OP
                    )
                )
            }
        }
    }
}

fun hasPermission(humanEntity: HumanEntity, permission: Permission): Boolean {
    if (humanEntity.isOp) return true
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