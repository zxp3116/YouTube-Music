package com.example.youtube_music;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    static ParsePosition parsePosition_Sort;
    static SimpleDateFormat simpleDateFormat, simpleDateFormat_Sort;
    static Date date_Sort, getDate, date;
    static long aLong, changeLong;
    static String subStringTime, changeString;


    public static Date stringToDate(String dateString) {

        parsePosition_Sort = new ParsePosition(0);
        simpleDateFormat_Sort = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date_Sort = simpleDateFormat_Sort.parse(dateString, parsePosition_Sort);
        return date_Sort;
    }

    public static String getShortTime(String string) {
        aLong = Calendar.getInstance().getTimeInMillis();
        getDate = getDateByString(string);
        if (getDate == null) {
            return changeString;
        }
        changeLong = (aLong - getDate.getTime()) / 1000;
        if (changeLong > 7 * 24 * 60 * 60) {
            subStringTime = string.substring(0, 10);
            changeString = subStringTime;
        } else if (changeLong > 2 * 24 * 60 * 60) {
            changeString = (int) (changeLong / (24 * 60 * 60)) + " 天前";
        } else if (changeLong > 24 * 60 * 60) {
            changeString = "昨天";
        } else if (changeLong > 60 * 60) {
            changeString = (int) (changeLong / (60 * 60)) + " 小時前";
        } else if (changeLong > 60) {
            changeString = (int) (changeLong / (60)) + " 分鐘前";
        } else if (changeLong > 1) {
            changeString = changeLong + " 秒前";
        } else {
            changeString = "剛剛";
        }
        return changeString;
    }

    public static String getYouTubeTime(String string) {
        aLong = Calendar.getInstance().getTimeInMillis();
        getDate = getDateByString(string);
        if (getDate == null) {
            return changeString;
        }
        changeLong = (aLong - getDate.getTime()) / 1000;
        if (changeLong > 7 * 24 * 60 * 60) {
            subStringTime = string.substring(0, 10);
            changeString = subStringTime;
        } else if (changeLong > 2 * 24 * 60 * 60) {
            changeString = (int) (changeLong / (24 * 60 * 60)) + " 天前";
        } else if (changeLong > 24 * 60 * 60) {
            changeString = "昨天";
        } else if (changeLong > 60 * 60) {
            changeString = (int) (changeLong / (60 * 60)) + " 小時前";
        } else if (changeLong > 60) {
            changeString = (int) (changeLong / (60)) + " 分鐘前";
        } else if (changeLong > 1) {
            changeString = changeLong + " 秒前";
        } else {
            changeString = "剛剛";
        }
        return changeString;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static Date getDateByString(String string) {
        if (string == null) {
            return date;
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
