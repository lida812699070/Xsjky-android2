package cn.xsjky.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OK on 2016/3/23.
 */
public class DataFormatUtils {
    public static String getDate(Long time) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        // yyyy-MM-dd HH:mm:ss
        String d = format.format(new Date(time));
        return d;
    }
}
