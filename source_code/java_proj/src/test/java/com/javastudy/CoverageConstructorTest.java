package com.javastudy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CoverageConstructorTest {

    @Test
    void noArgDemoConstructorsAreCovered() throws Exception {
        Path root = Path.of("target", "classes", "com", "javastudy");
        try (var paths = Files.walk(root)) {
            paths.filter(path -> path.toString().endsWith(".class"))
                    .map(this::toClassName)
                    .forEach(className -> assertDoesNotThrow(() -> instantiateIfPossible(className), className));
        }
    }

    private String toClassName(Path path) {
        String relative = Path.of("target", "classes").relativize(path).toString();
        return relative.substring(0, relative.length() - ".class".length())
                .replace('\\', '.')
                .replace('/', '.');
    }

    private void instantiateIfPossible(String className) throws Exception {
        Class<?> type = Class.forName(className);
        int modifiers = type.getModifiers();
        if (type.isInterface() || type.isAnnotation() || type.isEnum() || Modifier.isAbstract(modifiers)) {
            return;
        }
        Constructor<?> constructor;
        try {
            constructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException ignored) {
            return;
        }
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
