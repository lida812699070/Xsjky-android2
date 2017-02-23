package cn.xsjky.android.util;

/**
 * Created by OK on 2016/3/17.
 */
public class OnClickutils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}

