package cn.xsjky.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${lida} on 2016/5/5.
 */
public class DateFormatUtils {
    public static String getData(long time){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date= null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
