package com.example.ahmad.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Ahmad on 23 ديس، 14 م.
 */
public class AnalayzedImg implements Serializable, Comparable {
    private String imgPath;
    private short[][] fivePointsColor = new short[5][3];
    private static final int MIN_IDENT_VALUE = 20;
    private static final double MAX_SIZE_FOR_BITMAP = 500.0;
    private boolean isDitailesAnalayized = false;


    public String getImgPath() {
        return imgPath;
    }

    public AnalayzedImg(String imgPath) {
        this.imgPath = imgPath;
        analayzePoint(null, 0);
    }

    public AnalayzedImg() {
        this.imgPath = "";
        fivePointsColor = null;
    }

    public boolean compareRed(AnalayzedImg compTo) {
        return ((Math.abs(this.getRed(0) - compTo.getRed(0))) <= MIN_IDENT_VALUE);
    }

    public boolean compareRed(AnalayzedImg compTo, int testingPoind) {
        return ((Math.abs(this.getRed(testingPoind) - compTo.getRed(testingPoind))) <= MIN_IDENT_VALUE);
    }

    private void analayzePoint(Bitmap imgBitmap, int pointNumber) {
        int totalPixel_Count = 0;
        int totalPixel_values_Red = 0;
        int totalPixel_values_Green = 0;
        int totalPixel_values_Blue = 0;
        if (imgBitmap == null) {
            imgBitmap = getSampledSizeBitmap();
        }

        /*
        int img_Hight = 0;
        try {
            img_Hight = imgBitmap.getHeight();
        } catch (Exception e) {
            System.err.println("Exception in file: " + imgPath);
            System.exit(1);
        }
         */
        int img_Hight = imgBitmap.getHeight();
        int img_Width = imgBitmap.getWidth();
        int point_Hight = (int) (img_Hight * 0.1);
        int point_Width = (int) (img_Width * 0.1);
        int xStart = getX_Start(pointNumber, img_Width, point_Width);
        int yStart = getY_Start(pointNumber, img_Hight, point_Hight);

        for (int y = yStart; y < yStart + point_Hight; y++) {
            for (int x = xStart; x < xStart + point_Width; x++) {
                int rVal = Color.red(imgBitmap.getPixel(x, y));
                int gVal = Color.green(imgBitmap.getPixel(x, y));
                int bVal = Color.blue(imgBitmap.getPixel(x, y));
                totalPixel_values_Red += rVal;
                totalPixel_values_Green += gVal;
                totalPixel_values_Blue += bVal;
                totalPixel_Count++;
            }
        }
        short avgRed = (short) (totalPixel_values_Red / totalPixel_Count);
        short avgGreen = (short) (totalPixel_values_Green / totalPixel_Count);
        short avgBlue = (short) (totalPixel_values_Blue / totalPixel_Count);
        fivePointsColor[pointNumber][0] = avgRed;
        fivePointsColor[pointNumber][1] = avgGreen;
        fivePointsColor[pointNumber][2] = avgBlue;
    }

    private int getX_Start(int poinNumber, int imgWi, int poinWi) {
        int startX = -1;
        switch (poinNumber) {
            case 0:
                int midX = imgWi / 2;
                startX = midX - (poinWi / 2);
                break;
            case 1:
            case 4:
                startX = 0;
                break;
            case 2:
            case 3:
                startX = imgWi - poinWi;
                break;
        }
        return startX;
    }

    private int getY_Start(int poinNumber, int imgHi, int poinHi) {
        int startY = -1;
        switch (poinNumber) {
            case 0:
                int midY = imgHi / 2;
                startY = midY - (poinHi / 2);
                break;
            case 1:
            case 2:
                startY = 0;
                break;
            case 3:
            case 4:
                startY = imgHi - poinHi;
                break;
        }
        return startY;
    }

    public void detailedAnalyzing() {
        if (isDitailesAnalayized) return;
        Bitmap imgBitmap = getSampledSizeBitmap();
        for (int i = 1; i < 5; i++) {
            analayzePoint(imgBitmap, i);
        }
        isDitailesAnalayized = true;
    }

    private Bitmap getSampledSizeBitmap() {
        BitmapFactory.Options infoOpt = new BitmapFactory.Options();
        infoOpt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, infoOpt);
        int oriH = infoOpt.outHeight;
        int oriW = infoOpt.outWidth;
        int H_ScaleFactor = (int) Math.pow((MAX_SIZE_FOR_BITMAP / oriH), -1);
        int W_ScaleFactor = (int) Math.pow((MAX_SIZE_FOR_BITMAP / oriW), -1);

        BitmapFactory.Options scalOptn = new BitmapFactory.Options();
        scalOptn.inSampleSize = Math.max(H_ScaleFactor, W_ScaleFactor);
        Bitmap resBitmap = BitmapFactory.decodeFile(imgPath, scalOptn);
        return resBitmap;
    }

    private boolean onePointComparing(AnalayzedImg comTo, int poinNumber) {
        short firstRed = (short) Math.max(fivePointsColor[poinNumber][0], comTo.getRed(poinNumber));
        short secondRed = (short) Math.min(fivePointsColor[poinNumber][0], comTo.getRed(poinNumber));


        short firstGreen = (short) Math.max(fivePointsColor[poinNumber][1], comTo.getGreen(poinNumber));
        short secondGreen = (short) Math.min(fivePointsColor[poinNumber][1], comTo.getGreen(poinNumber));

        short firstBlue = (short) Math.max(fivePointsColor[poinNumber][2], comTo.getBlue(poinNumber));
        short secondBlue = (short) Math.min(fivePointsColor[poinNumber][2], comTo.getBlue(poinNumber));

        if (firstRed - secondRed <= MIN_IDENT_VALUE) {
            if (firstGreen - secondGreen <= MIN_IDENT_VALUE) {
                if (firstBlue - secondBlue <= MIN_IDENT_VALUE) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean simpleComparing(AnalayzedImg comTo) {
        return onePointComparing(comTo, 0);
    }

    public boolean detailedComparing(AnalayzedImg comTo) {
        this.detailedAnalyzing();
        comTo.detailedAnalyzing();
        for (int i = 1; i <= 4; i++) {
            if (!onePointComparing(comTo, i)) {
                //System.err.println("Out at i = " + i);
                return false;
            }
        }
        return true;
    }

    public short getRed(int pointIndx) {
        return fivePointsColor[pointIndx][0];
    }

    public short getGreen(int pointIndx) {
        return fivePointsColor[pointIndx][1];
    }

    public short getBlue(int pointIndx) {
        return fivePointsColor[pointIndx][2];
    }

    @Override
    public int compareTo(Object another) {
        AnalayzedImg comparedToObj = (AnalayzedImg) another;
        if (this.fivePointsColor[0][0] > comparedToObj.getRed(0)) {
            return 1;
        } else if (this.fivePointsColor[0][0] == comparedToObj.getRed(0)) {
            return 0;
        } else {
            return -1;
        }
    }

}
