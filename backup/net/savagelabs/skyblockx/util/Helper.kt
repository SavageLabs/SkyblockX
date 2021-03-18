package net.savagelabs.skyblockx.util

import com.cryptomorin.xseries.XMaterial.supports
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.placeholder.Placeholder
import net.savagelabs.skyblockx.registry.impl.PlaceholderRegistry

/**
 * [Regex] this regex is bound to handle hex color codes in strings.
 */
val RGB_COLOR_REGEX = Regex("#([A-Fa-f0-9]{6})")

/**
 * [String] handle all color codes in a string, including hex ones (if supported).
 */
inline val String.colored
    get() = color(this).letIf(supports(16)) {
        replace(RGB_COLOR_REGEX)
        { net.md_5.bungee.api.ChatColor.of(it.value).toString() }
    }

/**
 * If empty, return other, else return this.
 *
 * @param other [String] the string to return if empty.
 * @return [String]
 */
fun String.ifEmpty(other: String): String =
    if (this.isEmpty()) other else this

/**
 *
 * @param type  [Class] the type of placeholder to work with.
 * @param value [T] the value to provide the registry work function with.
 * @param T     the type correspondent to placeholder & value.
 * @return [String]
 */
fun <T> String.withPlaceholders(type: Class<out Placeholder<T>>, value: T): String =
        PlaceholderRegistry.work(type, this, value)