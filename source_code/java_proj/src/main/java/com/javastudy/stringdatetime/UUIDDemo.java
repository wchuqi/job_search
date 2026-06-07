package com.javastudy.stringdatetime;

import java.util.UUID;

/**
 * Demonstrates UUID (Universally Unique Identifier) in Java.
 * <p>
 * Key points:
 * <ul>
 *   <li>UUID.randomUUID() generates a type 4 (random) UUID</li>
 *   <li>A UUID is a 128-bit value, displayed as 32 hex chars with 4 hyphens</li>
 *   <li>Format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx</li>
 *   <li>Practically unique: probability of collision is negligible</li>
 *   <li>UUID.fromString() parses the standard string representation</li>
 *   <li>UUID can be used as database primary keys, request IDs, etc.</li>
 * </ul>
 */
public class UUIDDemo {

    /**
     * Generate a random UUID (version 4).
     */
    public static UUID randomUUID() {
        return UUID.randomUUID();
    }

    /**
     * Generate a UUID and return its string representation.
     * Format: 8-4-4-4-12 hex digits separated by hyphens.
     */
    public static String randomUUIDString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Parse a UUID from its string representation.
     * Throws IllegalArgumentException if format is invalid.
     */
    public static UUID fromString(String uuidString) {
        return UUID.fromString(uuidString);
    }

    /**
     * Check that two random UUIDs are (almost certainly) different.
     */
    public static boolean areDifferent(UUID a, UUID b) {
        return !a.equals(b);
    }

    /**
     * Get the most significant 64 bits.
     */
    public static long getMostSignificantBits(UUID uuid) {
        return uuid.getMostSignificantBits();
    }

    /**
     * Get the least significant 64 bits.
     */
    public static long getLeastSignificantBits(UUID uuid) {
        return uuid.getLeastSignificantBits();
    }

    /**
     * UUID version: randomUUID() returns version 4.
     */
    public static int getVersion(UUID uuid) {
        return uuid.version();
    }

    /**
     * UUID variant: the variant of the UUID.
     */
    public static int getVariant(UUID uuid) {
        return uuid.variant();
    }

    /**
     * Compare UUIDs (they implement Comparable).
     */
    public static int compare(UUID a, UUID b) {
        return a.compareTo(b);
    }

    /**
     * Generate multiple UUIDs and verify uniqueness.
     * Returns the count of unique UUIDs out of the requested count.
     */
    public static int generateUniqueCount(int count) {
        java.util.Set<UUID> seen = new java.util.HashSet<>();
        for (int i = 0; i < count; i++) {
            seen.add(UUID.randomUUID());
        }
        return seen.size();
    }

    /**
     * Create a UUID from specific long values (for testing/reproducibility).
     */
    public static UUID fromBits(long mostSigBits, long leastSigBits) {
        return new UUID(mostSigBits, leastSigBits);
    }
}
