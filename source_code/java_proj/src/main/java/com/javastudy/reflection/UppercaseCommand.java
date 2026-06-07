package com.javastudy.reflection;

@CommandName("upper")
public class UppercaseCommand implements Command {
    @Override
    public String execute(String input) {
        return input.toUpperCase();
    }
}
