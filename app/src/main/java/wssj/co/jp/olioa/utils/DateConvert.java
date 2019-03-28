package wssj.co.jp.olioa.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DaiKySy on 3/28/2019.
 */
public class DateConvert {

    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat DATE_FULL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final DateFormat DATE_NANO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static String formatToString(DateFormat format, long time) {
        return format.format(new Date(time));
    }

    public static String convertDateToDate(String time, DateFormat originFormat, DateFormat desFormat) {
        if (!TextUtils.isEmpty(time)) {
            try {
                return desFormat.format(originFormat.parse(time));
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
        return Constants.EMPTY_STRING;
    }

//    public static String formatDate(int day, int month, int year) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month - 1, day);
//        return DATE_FORMAT.format(calendar.getTime());
//    }
//
//    public static String getTime(int hr, int min) {
//        Time tme = new Time(hr, min, 0);
//        Format formatter = new SimpleDateFormat("a h:mm");
//        return formatter.format(tme);
//    }
//
//    public static String getTime(int hr, int min, String format) {
//        Time tme = new Time(hr, min, 0);
//        Format formatter = new SimpleDateFormat(format);
//        return formatter.format(tme);
//    }
//
//    public static Calendar convertTime(String time, String format) {
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat formatter = new SimpleDateFormat(format);
//        try {
//            cal.setTime(formatter.parse(time));
//            return cal;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
