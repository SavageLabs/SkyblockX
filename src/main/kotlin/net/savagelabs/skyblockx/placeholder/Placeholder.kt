package net.savagelabs.skyblockx.placeholder

/**
 * This interface contains necessary abstract properties and the single method used to process
 * all possible placeholders by one single identifier which will be handled
 * in a few else if statements or bound by Kotlin a simple usage of the `when` function.
 *
 * @param Type the type that is specified during implementation and will be used in the process.
 */
interface Placeholder<Type> {
    /**
     * [Char] this property holds the value representing the start character of placeholders.
     */
    val startCharacter: Char

    /**
     * [Char] this property holds the value representing the end character of placeholders.
     */
    val endCharacter: Char

    /**
     * Process value by passed down identifier (placeholder origin without brackets)
     * with extra objects if it were to be a requirement.
     *
     * @param value      [Type] the value to be processed.
     * @param identifier [String] the identifier/origin placeholder to process BY.
     * @param extra      [Any] array of extra objects if it were to be a requirement.
     */
    fun process(value: Type, identifier: String, vararg extra: Any): String
}