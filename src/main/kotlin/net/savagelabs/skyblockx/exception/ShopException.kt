package net.savagelabs.skyblockx.exception

/**
 * This exception is for Shop based failures.
 */
class ShopException constructor(
        override val message: String
) : RuntimeException(message)