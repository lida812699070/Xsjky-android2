package cn.xsjky.android.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.view.View;

import cn.xsjky.android.ui.ActivityDocumentDetailActivity;

public class BitmapUtils {
    public static final String FILE_NAME = "/print.tmp";

    public static Bitmap saveViewBitmap(View view) {
        try {
            Bitmap mBitmap = createViewBitmap(view);
            File f = new File(Environment.getExternalStorageDirectory()
                    .toString() + FILE_NAME);
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return mBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertToBitmap(int w, int h) {
        String path = Environment.getExternalStorageDirectory().toString() + FILE_NAME;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
                BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public static Bitmap getBitmap(String filePath) {
        File myCaptureFile = new File(filePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
//      第一次：设为true时，仅仅得到边界，即宽高
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(myCaptureFile.getAbsolutePath(), options);
//      第二次：将options的值设为Config.RGB_565，会比默认的Config.ARGB_8888减少一半内存；
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //想压缩到多少像素
        // options.inSampleSize = Math.max(options.outWidth, options.outHeight) / 500;
        options.inSampleSize = calculateInSampleSize(options, 600, 800);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(myCaptureFile.getAbsolutePath(), options);
        //Toast.makeText(MainActivity.this, options.outWidth+"X"+options.outHeight, Toast.LENGTH_SHORT).show();
        //bitmap = ColorToGray(bitmap);
        return bitmap;
    }

    //灰化处理
    public static Bitmap ColorToGray(Bitmap myBitmap) {
// Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pix = new int[width * height];
        myBitmap.getPixels(pix, 0, width, 0, 0, width, height);

// Apply pixel-by-pixel change
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (pix[index] >> 16) & 0xff;
                int g = (pix[index] >> 8) & 0xff;
                int b = pix[index] & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                r = gray;
                g = gray;
                b = gray;
                pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                index++;
            } // x
        } // y
        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pix, 0, width, 0, 0, width, height);
        myBitmap = null;
        pix = null;
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private Bitmap ImageCompressL(Bitmap bitmap) {
        double targetwidth = Math.sqrt(100.00 * 1000);
        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());
            // 缩放图片动作
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    public static void saveFile(Bitmap bm, String fileName) {
        /*String path = getSDPath() +"/revoeye/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }*/
        //File myCaptureFile = new File(path + fileName);
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
