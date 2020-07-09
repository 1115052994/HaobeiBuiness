package com.netmi.baselibrary.utils.yangmu;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;



/**
 * Created by Administrator on 2017/3/2.
 */

public class PhotoMannager {
    private static final String TAG = "ym";

    public static Bitmap getFromPhotoAlbum(Intent data, Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = data.getData();
//        Log.e(TAG, "onActivityResult: "+uri.getPath() );
        Bitmap photo = null;
        if (uri == null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                photo = (Bitmap) bundle.get("data"); //get bitmap
            } else {
                Toast.makeText(context, "图片获取失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                photo = BitmapFactory.decodeStream(resolver.openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (photo != null) {
            //旋转图片
            int degress = PhotoUtil.readPictureDegree(getPath(context, uri));
            photo = PhotoUtil.rotateBit(photo, degress);
            Log.e(TAG, "getFromPhotoAlbum: 执行了旋转图片" + degress);
        }
        return photo;
    }



    public static Bitmap scaleBitmap(Bitmap photo) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(),
                photo.getHeight(), matrix, true);
        return photo;
    }

    public static File saveBitmap(String path, String fileName, Bitmap photo, int quality) {
        File file = new File(path);//将要保存图片的路径
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + File.separator + fileName);
        if (file.exists()) {
            return file;
        }
        try {


            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File saveBitmap(String path, String fileName, Bitmap photo) {
        File file = new File(path);//将要保存图片的路径
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + File.separator + fileName);
        if (file.exists()) {
            return file;
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static String GetImageStr(File file) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码

        return new String(Base64.encode(data, Base64.DEFAULT));//返回Base64编码过的字节数组字符串
    }

    //将 BASE64 编码的字符串 s 进行解码
//    public static byte[] getFromBASE64(String s) {
//        if (s == null) return null;
//        BASE64Decoder decoder = new BASE64Decoder();
//        try {
//            byte[] b = decoder.decodeBuffer(s);
//            return b;
//        } catch (Exception e) {
//            Log.e(TAG, " getFromBASE64 catch Exception   " + e.getMessage());
//            return null;
//        }
//    }

    //将 BASE64 编码的字符串 s 进行解码,转换成Bitmap
    public static Bitmap bitmapGetFromBASE64(String s) {
        if (s == null) return null;
//        BASE64Decoder decoder = new BASE64Decoder();
        try {
//            s = new String (s.getBytes(),"utf-8");
            byte[] bytes = Base64.decode(s, Base64.DEFAULT);
            Bitmap result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return result;
        } catch (Exception e) {
            Log.e(TAG, " catch Exception   " + e.getMessage());
            return null;
        }
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    //获取相册图片路径
    public static String getPath(final Context context, final Uri uri) {

//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
