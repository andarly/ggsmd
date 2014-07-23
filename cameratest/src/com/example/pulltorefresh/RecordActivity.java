package com.example.pulltorefresh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

public class RecordActivity extends Activity {
    /** Called when the activity is first created. */
    String SD_CARD_TEMP_DIR;
    private byte[] mContent;
    Bitmap myBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        SD_CARD_TEMP_DIR = Environment.getExternalStorageDirectory() + File.separator + "tmpPhoto.jpg";
        Intent takePictureFromCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureFromCameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(SD_CARD_TEMP_DIR)));
        startActivityForResult(takePictureFromCameraIntent, 10);
        //
    }


    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver resolver = getContentResolver();
        try {
            File f = new File(SD_CARD_TEMP_DIR);
            try {
                Uri capturedImage = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(
                        getContentResolver(), f.getAbsolutePath(), null, null));
                // Log.i("camera", "Selected image: " +
                // capturedImage.toString());
                // f.delete();
                Toast toast = Toast.makeText(getApplicationContext(), capturedImage.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                //
                ImageView a = (ImageView) findViewById(R.id.imageView1);
                // 将图片内容解析成字节数组
                mContent = readStream(resolver.openInputStream(Uri.parse(capturedImage.toString())));
                // 将字节数组转换为ImageView可调用的Bitmap对象
                myBitmap = getPicFromBytes(mContent, null);
                // //把得到的图片绑定在控件上显示
                a.setImageBitmap(myBitmap);
                //
                //
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }


    //
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }


    //
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }


    //
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    //
}