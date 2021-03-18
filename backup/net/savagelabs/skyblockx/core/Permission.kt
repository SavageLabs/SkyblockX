package net.savagelabs.skyblockx.core

import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.GUIConfig
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
	CHAT("chat", "toggle island chat", PermissionDefault.TRUE),
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
		GUIConfig.instance.createGUIConfig.islandInfo.forEach { islandType ->
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
	return hasPermission(humanEntity, permission.getFullPermissionNode())
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

	// simple for loop rather than 5 different loops in a stream by mapping & filtering
	for (attachment in permissable.effectivePermissions) {
		val attachedPermission = attachment.permission.toLowerCase()
		if (!attachedPermission.startsWith(permission)) continue

		val processedPermission = attachedPermission.replace("$permission.", "")
		if (processedPermission.equals("*", ignoreCase = true)) {
			max.set(-1)
			continue
		}

		val latestMax = max.get()
		if (latestMax == -1) {
			continue
		}

		val amount = processedPermission.toIntOrNull() ?: continue
		if (amount > latestMax) max.set(amount)
	}

	// return the latest max
	return max.get()
}