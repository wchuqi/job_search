package com.javastudy.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SerializationDemoTest {

    @TempDir
    Path tempDir;

    @Test
    void serializeAndDeserialize_shouldPreserveNameAndAge() throws Exception {
        Path file = tempDir.resolve("person.ser");
        SerializationDemo.Person original = new SerializationDemo.Person("Alice", 30, "secret");

        SerializationDemo.serialize(original, file);
        SerializationDemo.Person deserialized = SerializationDemo.deserialize(file);

        assertEquals("Alice", deserialized.getName());
        assertEquals(30, deserialized.getAge());
    }

    @Test
    void serialize_shouldLoseTransientField() throws Exception {
        Path file = tempDir.resolve("person.ser");
        SerializationDemo.Person person = new SerializationDemo.Person("Bob", 25, "password123");

        SerializationDemo.serialize(person, file);
        SerializationDemo.Person deserialized = SerializationDemo.deserialize(file);

        // transient field password should be null after deserialization
        assertNull(deserialized.getPassword());
    }

    @Test
    void isTransientAfterDeserialize_shouldReturnTrue() throws Exception {
        Path file = tempDir.resolve("person.ser");
        SerializationDemo.Person person = new SerializationDemo.Person("Charlie", 40, "pw");

        SerializationDemo.serialize(person, file);
        assertTrue(SerializationDemo.isTransientAfterDeserialize(file));
    }

    @Test
    void serialize_shouldCreateFile() throws Exception {
        Path file = tempDir.resolve("person.ser");
        SerializationDemo.Person person = new SerializationDemo.Person("Dave", 35, "pw");

        SerializationDemo.serialize(person, file);

        assertTrue(Files.exists(file));
        assertTrue(Files.size(file) > 0);
    }

    @Test
    void deserialize_corruptFile_shouldThrow() throws Exception {
        Path file = tempDir.resolve("corrupt.ser");
        Files.writeString(file, "not a serialized object");

        assertThrows(Exception.class, () ->
                SerializationDemo.deserialize(file));
    }

    @Test
    void person_toString_shouldFormatCorrectly() {
        SerializationDemo.Person person = new SerializationDemo.Person("Eve", 28, "pw");
        String str = person.toString();
        assertTrue(str.contains("Eve"));
        assertTrue(str.contains("28"));
    }

    @Test
    void multipleSerialization_shouldProduceConsistentResults() throws Exception {
        Path file1 = tempDir.resolve("p1.ser");
        Path file2 = tempDir.resolve("p2.ser");
        SerializationDemo.Person person = new SerializationDemo.Person("Frank", 50, "pw");

        SerializationDemo.serialize(person, file1);
        SerializationDemo.serialize(person, file2);

        SerializationDemo.Person d1 = SerializationDemo.deserialize(file1);
        SerializationDemo.Person d2 = SerializationDemo.deserialize(file2);

        assertEquals(d1.getName(), d2.getName());
        assertEquals(d1.getAge(), d2.getAge());
    }
}
