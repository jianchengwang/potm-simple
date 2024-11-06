package org.example.potm.framework.utils;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jianchengwang
 * @date 2023/4/8
 */
public class DateUtils {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter dateMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private static DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalDate parseDate(String dateText) {
        return LocalDate.parse(dateText, dateFormatter);
    }

    public static LocalDateTime parseDateTime(String dateText, Boolean maxDateTime) {
        if(dateText.length() == 10) {
            if(maxDateTime) {
                dateText += " 23:59:59";
            } else {
                dateText += " 00:00:00";
            }
        }
        return LocalDateTime.parse(dateText, dateTimeFormatter);
    }

    public static String formatDateMonth(LocalDateTime dateTime) {
        return dateMonthFormatter.format(dateTime);
    }

    public static String formatDate(LocalDate date) {
        return dateFormatter.format(date);
    }

    public static String format24Hour(LocalDateTime dateTime) {
        String result = hourFormatter.format(dateTime.plusHours(1));
        if (result.equals("00:00")) {
            return "24:00";
        }
        return result;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTimeFormatter.format(dateTime);
    }

    public static String addYearMonths(String inputMonth, int monthsToAdd) {
        YearMonth yearMonth = YearMonth.parse(inputMonth, dateMonthFormatter);
        YearMonth twoMonthsPrior = yearMonth.plusMonths(monthsToAdd);
        return twoMonthsPrior.format(dateMonthFormatter);
    }

    public static String addMonthDays(String inputMonthDay, int daysToAdd) {
        LocalDate localDate = LocalDate.parse(inputMonthDay, dateFormatter);
        localDate = localDate.plusDays(daysToAdd);
        return localDate.format(dateFormatter);
    }

    public static int calDays(LocalDate startTime, LocalDate endTime) {
        if(startTime == null || endTime == null) {
            return 1;
        }
        return ((int) (endTime.toEpochDay() - startTime.toEpochDay())) + 1;
    }

    public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dateList.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dateList;
    }

    public static List<LocalDateTime> getHourlyDateTime(LocalDate date) {
        List<LocalDateTime> hourlyList = new ArrayList<>();
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN); // 当天的零点
        LocalDateTime endDateTime = LocalDateTime.of(date.plusDays(1), LocalTime.MIN); // 第二天的零点

        while (startDateTime.isBefore(endDateTime)) {
            hourlyList.add(startDateTime);
            startDateTime = startDateTime.plusHours(1);
        }
        return hourlyList;
    }

    // 获取前几日数据包含今天，输出格式化yyyy-MM-dd
    public static List<String> getBeforeNDays(LocalDate date, int days) {
        List<String> dateList = new ArrayList<>();
        dateList.add(date.format(dateFormatter));
        for (int i = 1; i < days; i++) {
            dateList.add(date.minusDays(i).format(dateFormatter));
        }
        return dateList;
    }

    // 获取上个月的今日
    public static String getLastMonthDate(LocalDate date, boolean outMonthFormat) {
        return date.minusMonths(1).format(outMonthFormat? dateMonthFormatter: dateFormatter);
    }

    // 获取去年今天
    public static String getLastYearDate(LocalDate date, boolean outMonthFormat) {
        return date.minusYears(1).format(outMonthFormat? dateMonthFormatter: dateFormatter);
    }

    public static List<String> getBeforeNMonth(LocalDate date, int months) {
        List<String> dateList = new ArrayList<>();
        dateList.add(date.format(dateMonthFormatter));
        for (int i = 1; i < months; i++) {
            dateList.add(date.minusMonths(i).format(dateMonthFormatter));
        }
        return dateList;
    }

    public static String getLastMonth(String month) {
        YearMonth yearMonth = YearMonth.parse(month, dateMonthFormatter);
        return yearMonth.minusMonths(1).format(dateMonthFormatter);
    }

    public static String getLastYearMonth(String month) {
        YearMonth yearMonth = YearMonth.parse(month, dateMonthFormatter);
        return yearMonth.minusYears(1).format(dateMonthFormatter);
    }
}
