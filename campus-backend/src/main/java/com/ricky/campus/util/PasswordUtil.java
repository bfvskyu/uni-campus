package com.ricky.campus.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String password) {
        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String hashed) {
        return encoder.matches(rawPassword, hashed);
    }

    public static String defaultPassword(String studentId) {
        return "Stu" + studentId + "355";
    }
}
