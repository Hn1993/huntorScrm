package com.huntor.mscrm.app2.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
    private static final String TAG = "PictureUtils";
    private static String imgPath;

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "检查图片旋转角度：" + degree);
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Log.d(TAG, "内存溢出！");
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * Get a BitmapDrawable from a local file that is scaled down to fit the current Window size.
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a, String path) {

        float destWidth = 960;
        float destHeight = 1280;
        if (a != null) {
            Display display = a.getWindowManager().getDefaultDisplay();

            destWidth = display.getWidth();
            destHeight = display.getHeight();
        }
        // read in the dimensions of the image on disk
        Bitmap bitmap = getScaledDrawable(path, destWidth, destHeight);
        return new BitmapDrawable(a.getResources(), bitmap);
    }

    /**
     * 压缩图片
     *
     * @param path       图片路径
     * @param destWidth  目标宽度
     * @param destHeight 目标高度
     * @return
     */
    public static Bitmap getScaledDrawable(String path, float destWidth, float destHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > destHeight || srcWidth > destWidth) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round((float) srcHeight / (float) destHeight);
                } else {
                    inSampleSize = Math.round((float) srcWidth / (float) destWidth);
                }
            }
            options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            options.inPurgeable = true; // 系统可以收回
            options.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            //旋转图片
            bitmap = rotateBitmapByDegree(bitmap, getBitmapDegree(path));
            return bitmap;
        } catch (Exception e) {
            Log.d(TAG, "解析图片地址错误");
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getScaledDrawable(String path, float destWidth, float destHeight, boolean checkRotation) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > destHeight || srcWidth > destWidth) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round((float) srcHeight / (float) destHeight);
                } else {
                    inSampleSize = Math.round((float) srcWidth / (float) destWidth);
                }
            }

            options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            options.inPurgeable = true; // 系统可以收回
            options.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            return bitmap;
        } catch (Exception e) {
            Log.d(TAG, "解析图片地址错误");
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 从 系统资源id 加载压缩图片
     *
     * @param res
     * @param resId     The resource id of the image data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /****
     * @param files   图片文件
     * @param width   压缩后图片的宽度
     * @param height  压缩后图片的高度
     * @param quality 压缩后图片的质量   （0到100）
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public static HashMap<String, byte[]> compressFile(Map<String, File> files, float width, float height, int quality) throws FileNotFoundException,
            IOException {
        HashMap<String, byte[]> dataMap = new HashMap<String, byte[]>();
        for (Entry<String, File> file : files.entrySet()) {

            byte[] data;
            // 压缩图片 I
            String srcPath = file.getValue().getAbsolutePath();
            boolean isPicture = (srcPath.contains(".jpg") || srcPath.contains(".png"));

            if (isPicture) { // 压缩

                Bitmap bitmap = PictureUtils.getScaledDrawable(srcPath, width, height);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
                    Log.d("uplpoad 开始压缩图片", "原始 size" + bitmap.getByteCount());

                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                data = baos.toByteArray();
                // outStream.write(imageBytes);
                Log.d("uplpoad 压缩图片结束", "压缩后 size:" + data.length);

                bitmap.recycle();
            } else { // 不压缩
                InputStream is = new FileInputStream(file.getValue());
                byte[] buf = new byte[1024];
                int len = 0;
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                while ((len = is.read(buf, 0, buf.length)) > 0) {
                    bout.write(buf, 0, len);
                }
                data = bout.toByteArray();
                is.close();
            }
            dataMap.put(file.getKey(), data);
        }
        return dataMap;
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return int inSampleSize 缩小的比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * cleanup bitmap and ImageView
     *
     * @param imageView
     */
    public static void cleanImageView(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (!(imageView.getDrawable() instanceof BitmapDrawable))
            return;

        // clean up the view's image for the sake of memory
        BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }

    public static void cleanImageViewBitmap(ImageView imageView) {

        if (!(imageView.getDrawable() instanceof BitmapDrawable))
            return;

        // clean up the view's image for the sake of memory
        BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }


    public static void saveMyBitmap(Bitmap mBitmap, String path) {
        File file = new File(path);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getImgPath() {
        return imgPath;
    }

    public static void setImgPath(String imgPath) {
        PictureUtils.imgPath = imgPath;
    }
}
