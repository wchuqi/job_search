package com.javastudy.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 知识点：Java 序列化
 * Serializable 接口, serialVersionUID, 反序列化安全风险
 */
public class SerializationDemo {

    /**
     * 可序列化的 Person 类
     * serialVersionUID 用于版本控制, 反序列化时校验一致性
     */
    public static class Person implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int age;
        private transient String password; // transient 字段不参与序列化

        public Person(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public String toString() {
            return "Person{name='%s', age=%d, password='%s'}".formatted(name, age, password);
        }
    }

    /**
     * 序列化对象到文件
     */
    public static void serialize(Person person, Path path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(person);
        }
    }

    /**
     * 从文件反序列化对象
     * 安全风险: 反序列化不可信数据可能导致远程代码执行
     */
    public static Person deserialize(Path path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Person) ois.readObject();
        }
    }

    /**
     * 演示 transient 字段的效果: password 在序列化后为 null
     */
    public static boolean isTransientAfterDeserialize(Path path) throws IOException, ClassNotFoundException {
        Person person = deserialize(path);
        return person.getPassword() == null;
    }
}
