package io.illyria.skyblockx.core

import org.bukkit.ChatColor

fun color(message: String): String {
    return ChatColor.translateAlternateColorCodes('&', message)
}