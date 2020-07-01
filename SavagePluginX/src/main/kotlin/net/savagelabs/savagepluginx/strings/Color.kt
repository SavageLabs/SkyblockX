package net.savagelabs.savagepluginx.strings

import org.bukkit.ChatColor

fun color(message: String) = ChatColor.translateAlternateColorCodes('&', message)

fun color(messages: List<String>) = messages.map { line -> color(line) }
