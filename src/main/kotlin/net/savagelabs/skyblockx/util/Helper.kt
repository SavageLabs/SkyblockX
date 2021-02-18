package net.savagelabs.skyblockx.util

/**
 * If empty, return other, else return this.
 *
 * @param other [String] the string to return if empty.
 * @return [String]
 */
fun String.ifEmpty(other: String): String =
    if (this.isEmpty()) other else this