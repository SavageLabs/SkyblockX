package net.savagelabs.skyblockx.registry

/**
 * This inline class is the default identifier of type [String] for registries.
 */
inline class Identifier(val name: String)

/**
 * This abstraction layer is used to set up multiple registries with similar functionality.
 */
abstract class Registry<Identifier, Value> constructor(initialValue: MutableMap<Identifier, Value> = hashMapOf()) {
    /**
     * [String] the name of this registry.
     */
    abstract val name: String

    /**
     * [MutableMap] the mutable map containing cached keys & values in this registry.
     */
    protected val registry: MutableMap<Identifier, Value> by lazyOf(initialValue)

    /**
     * Assign a value to the registry by identifier.
     *
     * @param identifier [Identifier] the identifier to assign the corresponding value to.
     * @param value      [Value] the value to be assigned to this registry.
     * @throws RegistryException if a value is already bound to the passed down identifier or the identifier and/or value is null.
     */
    @Throws(RegistryException::class)
    open fun register(identifier: Identifier, value: Value) {
        // In case of Java interoperability, a value by null might be passed so we'll throw an exception if that is the case.
        if (identifier == null || value == null) {
            throw RegistryException("Identifier and/or value must not be null")
        }

        // If the key already exists, we'll throw an exception.
        if (identifier in registry) {
            throw RegistryException("Value with identifier $identifier already exists in the $name registry")
        }

        // Registration can now be complete, let's assign the key with it's corresponding value.
        registry[identifier] = value
    }

    /**
     * Unregister a value from this registry by passed down identifier.
     */
    open fun unregister(identifier: Identifier): Value? {
        // In case of Java interoperability, a value by null might be passed so we'll throw an exception if that is the case.
        if (identifier == null) {
            throw RegistryException("Identifier must not be null")
        }

        // Remove from the registry map.
        return registry.remove(identifier)
    }

    /**
     * Unregister all values from this registry.
     */
    open fun unregisterAll(beforeRemoval: Value.() -> Unit = {}) {
        val iterator = registry.values.iterator()
        while (iterator.hasNext()) {
            beforeRemoval.invoke(iterator.next())
            iterator.remove()
        }
    }

    /**
     * Get a value by it's designated identifier.
     *
     * @param identifier [Identifier] the identifier that is bound to the corresponding value.
     * @return [Value] the registered value if any, otherwise null.
     */
    open fun byIdentifier(identifier: Identifier): Value? = registry[identifier]

    /**
     * This exception is the base of all errors thrown during registry failures.
     */
    internal class RegistryException constructor(
            message: String
    ) : RuntimeException(message)
}