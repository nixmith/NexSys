// src/main/java/com/nexsys/util/NormalizedNameRegistry.java
package com.nexsys.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for registries that normalize names for lookup.
 *
 * @param <T> the type of entries in the registry
 */
public abstract class NormalizedNameRegistry<T> {
    private final Map<String, T> entries = new ConcurrentHashMap<>();
    private final Map<String, T> normalizedIndex = new ConcurrentHashMap<>();

    /**
     * Normalize a name by converting to lowercase and removing whitespace.
     *
     * @param name the name to normalize
     * @return the normalized name
     */
    protected String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
    }

    /**
     * Get the name from an entry.
     *
     * @param entry the entry
     * @return the name
     */
    protected abstract String getName(T entry);

    /**
     * Get the ID from an entry.
     *
     * @param entry the entry
     * @return the ID
     */
    protected abstract String getId(T entry);

    /**
     * Register a new entry.
     *
     * @param entry the entry to register
     * @throws IllegalArgumentException if an entry with the same name already exists
     */
    public void register(T entry) {
        Objects.requireNonNull(entry, "Entry cannot be null");

        String id = getId(entry);
        String name = getName(entry);
        String normalizedName = normalizeName(name);

        Objects.requireNonNull(id, "Entry ID cannot be null");
        Objects.requireNonNull(name, "Entry name cannot be null");

        T existing = normalizedIndex.get(normalizedName);
        if (existing != null && !getId(existing).equals(id)) {
            throw new IllegalArgumentException(
                    "An entry with name '" + name + "' (normalized: " + normalizedName + ") already exists"
            );
        }

        entries.put(id, entry);
        normalizedIndex.put(normalizedName, entry);
    }

    /**
     * Get an entry by ID.
     *
     * @param id the ID
     * @return the entry, or null if not found
     */
    public T getById(String id) {
        return entries.get(id);
    }

    /**
     * Get an entry by name (case-insensitive, whitespace-insensitive).
     *
     * @param name the name
     * @return the entry, or null if not found
     */
    public T getByName(String name) {
        return normalizedIndex.get(normalizeName(name));
    }

    /**
     * Remove an entry by ID.
     *
     * @param id the ID
     * @return the removed entry, or null if not found
     */
    public T remove(String id) {
        T entry = entries.remove(id);
        if (entry != null) {
            normalizedIndex.remove(normalizeName(getName(entry)));
        }
        return entry;
    }

    /**
     * Get all entries.
     *
     * @return unmodifiable collection of all entries
     */
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(entries.values());
    }

    /**
     * Get all IDs.
     *
     * @return unmodifiable set of all IDs
     */
    public Set<String> getAllIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    /**
     * Check if an entry exists by ID.
     *
     * @param id the ID
     * @return true if exists
     */
    public boolean containsId(String id) {
        return entries.containsKey(id);
    }

    /**
     * Check if an entry exists by name.
     *
     * @param name the name
     * @return true if exists
     */
    public boolean containsName(String name) {
        return normalizedIndex.containsKey(normalizeName(name));
    }

    /**
     * Clear all entries.
     */
    public void clear() {
        entries.clear();
        normalizedIndex.clear();
    }

    /**
     * Get the number of entries.
     *
     * @return the number of entries
     */
    public int size() {
        return entries.size();
    }
}