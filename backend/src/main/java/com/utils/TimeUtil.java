package com.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utility class for handling date and time conversions between SQL and Java time types
 */
public class TimeUtil {

    // Private constructor to prevent instantiation
    private TimeUtil() {}

    /**
     * Converts java.sql.Date to java.time.LocalDate
     * @param sqlDate the SQL Date to convert (cannot be null)
     * @return corresponding LocalDate
     * @throws IllegalArgumentException if sqlDate is null
     */
    public static LocalDate toLocalDate(Date sqlDate) {
        if (sqlDate == null) {
            throw new IllegalArgumentException("SQL Date cannot be null");
        }
        return sqlDate.toLocalDate();
    }

    /**
     * Converts java.time.LocalDate to java.sql.Date
     * @param localDate the LocalDate to convert (cannot be null)
     * @return corresponding SQL Date
     * @throws IllegalArgumentException if localDate is null
     */
    public static Date toSqlDate(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalArgumentException("LocalDate cannot be null");
        }
        return Date.valueOf(localDate);
    }

    /**
     * Converts java.sql.Timestamp to java.time.LocalDateTime
     * @param timestamp the SQL Timestamp to convert (cannot be null)
     * @return corresponding LocalDateTime
     * @throws IllegalArgumentException if timestamp is null
     */
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        return timestamp.toLocalDateTime();
    }

    /**
     * Converts java.time.LocalDateTime to java.sql.Timestamp
     * @param localDateTime the LocalDateTime to convert (cannot be null)
     * @return corresponding SQL Timestamp
     * @throws IllegalArgumentException if localDateTime is null
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new IllegalArgumentException("LocalDateTime cannot be null");
        }
        return Timestamp.valueOf(localDateTime);
    }

    /**
     * Gets current date as java.sql.Date
     * @return current date as SQL Date
     */
    public static Date currentSqlDate() {
        return Date.valueOf(LocalDate.now());
    }

    /**
     * Gets current timestamp as java.sql.Timestamp
     * @return current timestamp
     */
    public static Timestamp currentTimestamp() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * Checks if a date is in the past
     * @param date the date to check
     * @return true if the date is before today
     * @throws IllegalArgumentException if date is null
     */
    public static boolean isPastDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.isBefore(LocalDate.now());
    }

    /**
     * Checks if a date is in the future
     * @param date the date to check
     * @return true if the date is after today
     * @throws IllegalArgumentException if date is null
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.isAfter(LocalDate.now());
    }
}