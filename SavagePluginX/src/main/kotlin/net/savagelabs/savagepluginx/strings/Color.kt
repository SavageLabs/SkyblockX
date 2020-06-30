package net.savagelabs.savagepluginx.strings

import org.bukkit.ChatColor

fun color(message: String): String {
    return ChatColor.translateAlternateColorCodes('&', message)
}

fun color(messages: List<String>): List<String> {
    return messages.map { line -> color(line) }
}