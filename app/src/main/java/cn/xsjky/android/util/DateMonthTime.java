package cn.xsjky.android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ${lida} on 2016/11/28.
 */
public class DateMonthTime {
    //1、获取当月第一天
    public static String monthFirstForDate() {
//规定返回日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
//设置为第一天
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = sf.format(gcLast.getTime());
//打印本月第一天
        return day_first;
    }

    //2、获取当月最后一天
    public static String monthEndForDatelast() {
//获取Calendar
        Calendar calendar = Calendar.getInstance();
//设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(calendar.DATE));
//设置日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sf.format(calendar.getTime());
        return ss;
    }

    //3、非常简单和实用的获取本月第一天和最后一天
    public static void testt() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        System.out.println("===============本月first day:" + first);

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        System.out.println("===============本月last day:" + last);
    }

    //4、获取上个月的第一天
    public static String getBeforeFirstMonthdate(int month) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("上个月第一天：" + format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    //5、获取上个月的最后一天
    public static String getBeforeLastMonthdate(int monthNumber) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month + monthNumber);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return sf.format(calendar.getTime());
    }

    //6、获取当前日期的前一周或者前一个月时间

    public static String getFirstDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cl = Calendar.getInstance();
// cl.setTime(dateNow);
// cl.add(Calendar.DAY_OF_YEAR, -1);
//一天
// cl.add(Calendar.WEEK_OF_YEAR, -1);
//一周
        cl.add(Calendar.MONTH, -1);
//从现在算，之前一个月,如果是2个月，那么-1-----》改为-2
        Date dateFrom = cl.getTime();
        return sdf.format(dateFrom);
    }

}
