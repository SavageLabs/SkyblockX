package net.savagelabs.skyblockx.registry.impl

import net.savagelabs.skyblockx.placeholder.Placeholder
import net.savagelabs.skyblockx.registry.Registry

/**
 * This implementation is the registry for present placeholders.
 */
object PlaceholderRegistry : Registry<Class<out Placeholder<*>>, Placeholder<*>>() {
    /**
     * [String] the name of this registry.
     */
    override val name: String = "Placeholder"

    /**
     * Work all placeholders in string correspondent to the passed class.
     *
     * @param boundTo [Class] the placeholder to process the string with.
     * @param toWork  [String] the string to be worked on.
     * @param value   [T] the value to process the string with.
     * @param T       the type correspondent to the placeholder.
     * @return [String]
     */
    fun <T> work(boundTo: Class<out Placeholder<T>>, toWork: String, value: T): String {
        // necessity
        val placeholder = byIdentifier(boundTo) ?: return toWork; placeholder as Placeholder<T>
        val startCharacter = placeholder.startCharacter
        val endCharacter = placeholder.endCharacter

        // work the string
        var workedOn = toWork
        var lastStartChar = -1
        var lastEndChar = -1

        for ((index, char) in toWork.withIndex()) {
            // if the char is equals to the start character, set the last start char's index
            if (char == startCharacter) {
                lastStartChar = index
            }

            // if the char is equals to the end character, set the last end char's index
            if (char == endCharacter) {
                lastEndChar = index
            }

            // move forward to the next entry if the last start char or last end char is equals to -1
            if (lastStartChar == -1 || lastEndChar == -1) {
                continue
            }

            // collect the type of placeholder between the start and end characters
            var placeholderFound = ""
            for (charIndex in lastStartChar + 1 until lastEndChar) {
                placeholderFound += toWork[charIndex]
            }

            val result = placeholder.process(value, placeholderFound)
            if (result != placeholderFound) workedOn = workedOn.replace("{$placeholderFound}", result)

            lastStartChar = -1
            lastEndChar = -1
        }

        // return the worked string
        return workedOn
    }
}