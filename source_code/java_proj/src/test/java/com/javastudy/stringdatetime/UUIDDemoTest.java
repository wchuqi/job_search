package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDDemoTest {

    @Test
    void randomUUIDIsNotNull() {
        assertNotNull(UUIDDemo.randomUUID());
    }

    @Test
    void randomUUIDStringHasCorrectFormat() {
        String uuid = UUIDDemo.randomUUIDString();
        // Format: 8-4-4-4-12 hex characters
        assertEquals(36, uuid.length());
        assertEquals('-', uuid.charAt(8));
        assertEquals('-', uuid.charAt(13));
        assertEquals('-', uuid.charAt(18));
        assertEquals('-', uuid.charAt(23));
    }

    @Test
    void fromStringParsesValidUUID() {
        String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUIDDemo.fromString(uuidStr);
        assertEquals(uuidStr, uuid.toString());
    }

    @Test
    void fromStringThrowsForInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> UUIDDemo.fromString("not-a-uuid"));
    }

    @Test
    void areDifferentForRandomUUIDs() {
        UUID a = UUIDDemo.randomUUID();
        UUID b = UUIDDemo.randomUUID();
        assertTrue(UUIDDemo.areDifferent(a, b));
    }

    @Test
    void getMostSignificantBits() {
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        long msb = UUIDDemo.getMostSignificantBits(uuid);
        assertNotEquals(0, msb);
    }

    @Test
    void getLeastSignificantBits() {
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        long lsb = UUIDDemo.getLeastSignificantBits(uuid);
        assertNotEquals(0, lsb);
    }

    @Test
    void randomUUIDIsVersion4() {
        UUID uuid = UUIDDemo.randomUUID();
        assertEquals(4, UUIDDemo.getVersion(uuid));
    }

    @Test
    void randomUUIDHasVariant2() {
        UUID uuid = UUIDDemo.randomUUID();
        assertEquals(2, UUIDDemo.getVariant(uuid));
    }

    @Test
    void compareOrdersUUIDs() {
        UUID a = UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID b = UUID.fromString("00000000-0000-0000-0000-000000000001");
        assertTrue(UUIDDemo.compare(a, b) < 0);
        assertTrue(UUIDDemo.compare(b, a) > 0);
        assertEquals(0, UUIDDemo.compare(a, a));
    }

    @Test
    void generateUniqueCountAllUnique() {
        int count = 1000;
        int uniqueCount = UUIDDemo.generateUniqueCount(count);
        assertEquals(count, uniqueCount, "All generated UUIDs should be unique");
    }

    @Test
    void fromBitsCreatesUUID() {
        UUID uuid = UUIDDemo.fromBits(1L, 2L);
        assertEquals(1L, uuid.getMostSignificantBits());
        assertEquals(2L, uuid.getLeastSignificantBits());
    }
}
