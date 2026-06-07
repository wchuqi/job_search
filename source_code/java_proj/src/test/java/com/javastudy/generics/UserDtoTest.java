package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void testRecordAccessors() {
        UserDto dto = new UserDto(1L, "Alice");
        assertEquals(1L, dto.id());
        assertEquals("Alice", dto.name());
    }

    @Test
    void testRecordEquals() {
        UserDto a = new UserDto(1L, "Alice");
        UserDto b = new UserDto(1L, "Alice");
        assertEquals(a, b);
    }

    @Test
    void testRecordHashCode() {
        UserDto a = new UserDto(1L, "Alice");
        UserDto b = new UserDto(1L, "Alice");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testRecordToString() {
        UserDto dto = new UserDto(1L, "Alice");
        assertTrue(dto.toString().contains("1"));
        assertTrue(dto.toString().contains("Alice"));
    }

    @Test
    void testNullNameThrows() {
        assertThrows(NullPointerException.class, () -> new UserDto(1L, null));
    }

    @Test
    void testBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new UserDto(1L, "  "));
    }

    @Test
    void testDisplayName() {
        UserDto dto = new UserDto(1L, "Alice");
        assertEquals("User[1]: Alice", dto.displayName());
    }
}
