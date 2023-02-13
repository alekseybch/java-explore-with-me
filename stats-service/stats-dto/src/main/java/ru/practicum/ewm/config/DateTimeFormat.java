package ru.practicum.ewm.config;

import java.time.format.DateTimeFormatter;

public class DateTimeFormat {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    }

}
