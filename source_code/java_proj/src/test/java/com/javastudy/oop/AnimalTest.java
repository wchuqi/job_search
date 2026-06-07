package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void testAnimalSpeak() {
        Animal animal = new Animal("Animal");
        assertEquals("Animal makes a sound", animal.speak());
    }

    @Test
    void testDogSpeak() {
        Dog dog = new Dog("Rex");
        assertEquals("Rex barks", dog.speak());
    }

    @Test
    void testCatSpeak() {
        Cat cat = new Cat("Whiskers");
        assertEquals("Whiskers meows", cat.speak());
    }

    @Test
    void testPolymorphism() {
        // 知识点：多态 - 父类引用指向子类对象
        Animal animal = new Dog("Rex");
        assertEquals("Rex barks", animal.speak()); // 运行时调用Dog.speak()

        animal = new Cat("Whiskers");
        assertEquals("Whiskers meows", animal.speak()); // 运行时调用Cat.speak()
    }
}
