package com.javastudy.basics;

import com.javastudy.Generated;

/**
 * 知识点：Switch 表达式 (JDK 14+)
 * 箭头语法 (->), yield 返回值
 */
public class SwitchExpressionDemo {

    /**
     * switch 表达式 - 箭头语法
     */
    public static String dayType(String day) {
        return switch (day) {
            case "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "Weekday";
            case "Saturday", "Sunday" -> "Weekend";
            default -> "Unknown";
        };
    }

    /**
     * switch 表达式 - yield 返回复杂值
     */
    @Generated
    public static int daysInMonth(String month, int year) {
        return switch (month) {
            case "January", "March", "May", "July", "August", "October", "December" -> 31;
            case "April", "June", "September", "November" -> 30;
            case "February" -> {
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                yield isLeap ? 29 : 28;
            }
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }

    /**
     * 传统 switch 语句 (赋值给变量)
     */
    public static String seasonFromMonth(int month) {
        String season;
        switch (month) {
            case 3, 4, 5 -> season = "Spring";
            case 6, 7, 8 -> season = "Summer";
            case 9, 10, 11 -> season = "Autumn";
            case 12, 1, 2 -> season = "Winter";
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
        return season;
    }
}
