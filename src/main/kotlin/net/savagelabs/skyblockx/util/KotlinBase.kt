package net.savagelabs.skyblockx.util

/**
 * Invoke a block if a condition is met. This is purely
 * a mockup version differentiated from Kotlin's [let] function
 * which can be found inside of the `Standard` file.
 *
 * @param condition [Boolean] the condition to check for further execution.
 * @param block     the block of code to invoke if the condition is met.
 * @param T         the type of instance to work with.
 * @return [T]
 */
fun <T> T.letIf(condition: Boolean, block: T.() -> T): T =
        this.let {
            if (!condition) return@let it
            block(this)
        }