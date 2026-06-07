package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateUser() {
        User user = new User("Alice", 30);
        assertEquals("Alice", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    void testNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, 30));
    }

    @Test
    void testBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new User("  ", 30));
    }

    @Test
    void testNegativeAgeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new User("Alice", -1));
    }

    @Test
    void testSetAge() {
        User user = new User("Alice", 30);
        user.setAge(31);
        assertEquals(31, user.getAge());
    }

    @Test
    void testToString() {
        User user = new User("Alice", 30);
        assertEquals("User{name='Alice', age=30}", user.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        User u1 = new User("Alice", 30);
        User u2 = new User("Alice", 30);
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }
}
