package com.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    // Private constructor to prevent instantiation
    private ValidationUtils() {}

    // Regex patterns
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^[0-9]{10,15}$");

    // Gender constants
    private static final String[] VALID_GENDERS = {"MALE", "FEMALE", "OTHER"};


    /**
     * Validates email format
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates mobile number format
     * @param mobile the mobile number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMobile(String mobile) {
        if (mobile == null) return false;
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * Validates gender
     * @param gender the gender to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidGender(String gender) {
        if (gender == null) return false;
        for (String validGender : VALID_GENDERS) {
            if (validGender.equalsIgnoreCase(gender)) {
                return true;
            }
        }
        return false;
    }

    public static void validateNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    /**
     * Validates class/grade level (1-12)
     * @param classroom the class to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidClass(int classroom) {
        return classroom >= 1 && classroom <= 12;
    }
}
