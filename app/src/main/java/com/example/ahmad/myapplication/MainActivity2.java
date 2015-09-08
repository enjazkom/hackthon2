package com.example.ahmad.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;


public class MainActivity2 extends ActionBarActivity {
    ProgressBar progBar;
    int progBarProgress = 0;
    ImageView img;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void incDec(View vv) {
        switch (vv.getId()) {
            case R.id.buttonInc:
                progBarProgress += 5;
                progBar.setProgress(progBarProgress);
                progressDialog.show();
                break;
            case R.id.buttonDec:
                progBarProgress -= 5;
                progBar.setProgress(progBarProgress);
                progressDialog.dismiss();
                break;
        }
    }

    public void toMain(View vvv) {
        //startActivity(new Intent(this, MainActivity3.class));
        //img.setImageBitmap(getResizedBitmacp(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/imgsToTest/6.jpg", 500));
        //String ss = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/imgsToTest/6.jpg";
        //Canvas cc = new Canvas(getResizedBitmacp(ss, 500));
        startActivity(new Intent(this, MainActivity4.class));
    }

    private Bitmap getResizedBitmap(String bitmapPath, int maxSize) {

        BitmapFactory.Options infoOpt = new BitmapFactory.Options();
        infoOpt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapPath, infoOpt);
        int oriH = infoOpt.outHeight;
        int oriW = infoOpt.outWidth;
        int H_ScaleFactor = (int) Math.pow((maxSize / oriH), -1);
        int W_ScaleFactor = (int) Math.pow((maxSize / oriW), -1);

        BitmapFactory.Options scalOptn = new BitmapFactory.Options();
        scalOptn.inSampleSize = Math.max(H_ScaleFactor, W_ScaleFactor);
        Bitmap resBitmap = BitmapFactory.decodeFile(bitmapPath, scalOptn);
        return resBitmap;
    }

    private Bitmap getResizedBitmacp(String bitmapPath, int maxSize) {
        BitmapFactory.Options infoOpt = new BitmapFactory.Options();
        infoOpt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapPath, infoOpt);
        int oriH = infoOpt.outHeight;
        int oriW = infoOpt.outWidth;
        int H_ScaleFactor = (int) Math.pow((maxSize + (maxSize * 0.5) / oriH), -1);
        int W_ScaleFactor = (int) Math.pow((maxSize + (maxSize * 0.5) / oriW), -1);

        BitmapFactory.Options scalOptn = new BitmapFactory.Options();
        scalOptn.inSampleSize = Math.max(H_ScaleFactor, W_ScaleFactor);
        Bitmap resBitmap = BitmapFactory.decodeFile(bitmapPath, scalOptn);
        double factor = 0.0;
        int bitmapHight = resBitmap.getHeight();
        int bitmapWidth = resBitmap.getWidth();
        if (bitmapWidth > bitmapHight) {
            factor = (bitmapHight * 1.0) / bitmapWidth;
            resBitmap = Bitmap.createScaledBitmap(resBitmap, maxSize, (int) (maxSize * factor), false);
        } else {
            factor = (bitmapWidth * 1.0) / bitmapHight;
            resBitmap = Bitmap.createScaledBitmap(resBitmap, (int) (maxSize * factor), maxSize, false);
        }
        return resBitmap;
    }
}
