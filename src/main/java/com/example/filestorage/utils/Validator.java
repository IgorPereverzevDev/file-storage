package com.example.filestorage.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static Pattern pattern;
    private static Matcher matcher;


    public static boolean isValidLine(String line) {
        return !line.replaceAll("[\\n\\t ]", "").isEmpty();
    }

    public static boolean isValidName(String name) {
        pattern = Pattern.compile("^[a-z]", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(name);
        return matcher.find();
    }

    public static boolean isValidId(String id) {
        pattern = Pattern.compile("^[0-9]");
        matcher = pattern.matcher(id);
        return matcher.find();
    }

    public static boolean isValidTimeStamp(String timestamp) {
        pattern = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})$");
        matcher = pattern.matcher(timestamp);
        return matcher.find();
    }
}
