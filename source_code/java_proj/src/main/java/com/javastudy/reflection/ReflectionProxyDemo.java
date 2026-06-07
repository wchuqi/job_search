package com.javastudy.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class ReflectionProxyDemo {

    public List<String> inspectClass(Class<?> type) {
        return List.of(type.getSimpleName(), type.getDeclaredFields().length + " fields",
                type.getDeclaredMethods().length + " methods");
    }

    public String annotationName(Class<?> type) {
        CommandName annotation = type.getAnnotation(CommandName.class);
        return annotation == null ? "" : annotation.value();
    }

    public Object construct(Class<?> type) throws ReflectiveOperationException {
        return type.getDeclaredConstructor().newInstance();
    }

    public String invokeExecute(Command command, String value) throws ReflectiveOperationException {
        Method method = command.getClass().getMethod("execute", String.class);
        return (String) method.invoke(command, value);
    }

    public String methodHandleExecute(Command command, String value) throws Throwable {
        return (String) MethodHandles.publicLookup()
                .findVirtual(command.getClass(), "execute", MethodType.methodType(String.class, String.class))
                .invoke(command, value);
    }

    public Command timingProxy(Command target, List<String> calls) {
        InvocationHandler handler = (Object proxy, Method method, Object[] args) -> {
            calls.add(method.getName());
            return method.invoke(target, args);
        };
        return (Command) Proxy.newProxyInstance(
                Command.class.getClassLoader(), new Class<?>[]{Command.class}, handler);
    }

    public Map<String, Command> loadCommands() {
        Map<String, Command> commands = new LinkedHashMap<>();
        for (Command command : ServiceLoader.load(Command.class)) {
            commands.put(annotationName(command.getClass()), command);
        }
        return commands;
    }

    public List<String> jpmsKeywords() {
        return List.of("module-info", "requires", "exports", "opens", "uses", "provides");
    }

    public List<String> reflectionAlternatives() {
        return List.of("cache reflection objects", "MethodHandle", "bytecode generation",
                "AOT indexing");
    }
}
