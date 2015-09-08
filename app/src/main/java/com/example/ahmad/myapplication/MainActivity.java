package com.example.ahmad.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {
    public static final String RES_KEY = "resKey";
    private ProgressBar progBar;
    private TextView txtToView;
    private TextView logger;
    private Handler handlerOrPortal = new Handler();
    private Thread findingDupsThread;
    private long tookTime;
    private static final byte PROG_SET_VISIBILITY = 0;
    private static final byte PROG_SET_MAX = 1;
    private static final byte PROG_SET_PROGRESS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progBar = (ProgressBar) findViewById(R.id.progressBarMain);
        txtToView = (TextView) findViewById(R.id.progressBarMainTxt);
        logger = (TextView) findViewById(R.id.textView2);
        /*findingDupsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startFindingDuplicatesProcess();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void to_nxt(View vv) {
        switch (vv.getId()) {
            case R.id.button:
                //findingDupsThread.setPriority(findingDupsThread.getThreadGroup().getMaxPriority());
                tookTime = System.currentTimeMillis();
                if (findingDupsThread == null)
                    findingDupsThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            startFindingDuplicatesProcess();
                        }
                    });

                try {
                    findingDupsThread.start();
                } catch (IllegalThreadStateException e) {
                }

                break;
            case R.id.button2:
                findingDupsThread.interrupt();
                findingDupsThread = null;
                break;
        }

    }

    public void startFindingDuplicatesProcess() {
        if (Thread.currentThread().isInterrupted()) return;
        updateProgBar(View.VISIBLE, PROG_SET_VISIBILITY, 1);
        String absPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/imgsToTest/";
        File dir = new File(absPath);
        ArrayList<AnalayzedImg> preArrList = new ArrayList<>();
        analyzePath(dir, preArrList);
        if (Thread.currentThread().isInterrupted()) return;
        AnalayzedImg[] analayzedImgsArr = new AnalayzedImg[preArrList.size()];
        analayzedImgsArr = preArrList.toArray(analayzedImgsArr);
        preArrList = null;
        System.err.println("images Count: " + analayzedImgsArr.length);
        for (AnalayzedImg oneA : analayzedImgsArr) {
            //System.err.println(oneA.getImgPath());
            System.err.println("Red: " + oneA.getRed(0) + ", Green: " + oneA.getGreen(0) + ", Blue: " + oneA.getBlue(0));
        }

        Arrays.sort(analayzedImgsArr);
        System.err.println("Sooooooooooorting!!!!!!----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        for (AnalayzedImg oneA : analayzedImgsArr) {
            //System.err.println(oneA.getImgPath());
            System.err.println("Red: " + oneA.getRed(0) + ", Green: " + oneA.getGreen(0) + ", Blue: " + oneA.getBlue(0));
        }

        System.err.println("----------------------------------------------------");

        ArrayList<ArrayList<AnalayzedImg>> lvl_1_Res = new ArrayList<>();
        ArrayList<ArrayList<AnalayzedImg>> lvl_2_Res = new ArrayList<>();
        int totalComparisons = 0; //test
        updateProgBar(0, PROG_SET_PROGRESS, 2);
        updateProgBar(analayzedImgsArr.length, PROG_SET_MAX, 2);
        for (int i = 0; i < analayzedImgsArr.length; i++) {
            if (Thread.currentThread().isInterrupted()) return;
            boolean firstDuplication = true;
            for (int j = i + 1; j < analayzedImgsArr.length; j++) {
                if (Thread.currentThread().isInterrupted()) return;
                totalComparisons++;
                if (analayzedImgsArr[i].simpleComparing(analayzedImgsArr[j])) {
                    if (firstDuplication) {
                        lvl_1_Res.add(new ArrayList<AnalayzedImg>());
                        lvl_1_Res.get(lvl_1_Res.size() - 1).add(analayzedImgsArr[i]);
                        firstDuplication = false;
                    }
                    lvl_1_Res.get(lvl_1_Res.size() - 1).add(analayzedImgsArr[j]);
                }
            }
            updateProgBar(i + 1, PROG_SET_PROGRESS, 2);
        }
        System.err.println("Total Comparisons: " + totalComparisons); //test
        System.err.println(); //test
        System.err.println(); //test
        System.err.println(); //test
        for (ArrayList<AnalayzedImg> oneSet : lvl_1_Res) {
            int countOfComponent = 0;  //test
            for (AnalayzedImg oneComponent : oneSet) { //test
                System.err.println("Red: " + oneComponent.getRed(0) + ", Green: " //test
                        + oneComponent.getGreen(0) + ", Blue: " + oneComponent.getBlue(0)); //test
                countOfComponent++; //test
            }
            System.err.println("Count Of Components in this group: " + countOfComponent); //test
            System.err.println("-----------------------"); //test
        }
        System.err.println("----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        System.err.println("---------------Detailed Comparing:---------------");
        System.err.println("----------------------------------------------------");
        updateProgBar(0, PROG_SET_PROGRESS, 3);
        int deepLvl1Size = 0;
        int findingLvl2Prog = 0;
        for (ArrayList<AnalayzedImg> oneSet : lvl_1_Res) {
            deepLvl1Size += oneSet.size();
        }
        updateProgBar(deepLvl1Size + 10, PROG_SET_MAX, 3);
        for (ArrayList<AnalayzedImg> oneSet : lvl_1_Res)
            for (int i = 0; i < oneSet.size(); i++) {
                boolean firstDuplication = true;
                for (int j = i + 1; j < oneSet.size(); j++) {
                    if (Thread.currentThread().isInterrupted()) return;
                    if (oneSet.get(i).detailedComparing(oneSet.get(j))) {
                        if (firstDuplication) {
                            System.err.println("-----------------------"); //test
                            lvl_2_Res.add(new ArrayList<AnalayzedImg>());
                            lvl_2_Res.get(lvl_2_Res.size() - 1).add(oneSet.get(i));
                            firstDuplication = false;
                            printAnalyzedImgInfos(oneSet.get(i));// test
                        }
                        lvl_2_Res.get(lvl_2_Res.size() - 1).add(oneSet.get(j));
                        printAnalyzedImgInfos(oneSet.get(j));// test
                    }
                }
                findingLvl2Prog++;
                updateProgBar(findingLvl2Prog, PROG_SET_PROGRESS, 3);
            }
        System.err.println("-----------------------"); //test
        System.err.println("-----------------------"); //test
        System.err.println("after deleteArrListRepetition()"); //test
        System.err.println("-----------------------"); //test
        deleteArrListRepetition(lvl_2_Res);
        long endTime = System.currentTimeMillis();
        updateProgBar(findingLvl2Prog + 10, PROG_SET_PROGRESS, 3);
        updateProgBar(deepLvl1Size + 10, PROG_SET_MAX, 3, "Took (" + (endTime - tookTime) + ") millins");
        for (ArrayList<AnalayzedImg> oneRow : lvl_2_Res) {
            for (AnalayzedImg oneElement : oneRow)
                printAnalyzedImgInfos(oneElement);// test
            System.err.println("-----------------------"); //test
        }
    }

    public void analyzePath(File pathToAnalyze, ArrayList<AnalayzedImg> arrListToStoreIn) {
        updateProgBar(0, PROG_SET_PROGRESS, 1);
        String[] filesList = pathToAnalyze.list();
        ArrayList<File> dirFiles = new ArrayList<>();
        updateProgBar(filesList.length, PROG_SET_MAX, 1, pathToAnalyze.getName());
        int progBarProgress = 0;
        updateProgBar(progBarProgress, PROG_SET_PROGRESS, 1);
        for (String fileName : filesList) {
            if (Thread.currentThread().isInterrupted()) return;
            File oneFile = new File(pathToAnalyze, fileName);
            if (oneFile.exists() && oneFile.canWrite() && !oneFile.isHidden())
                if (oneFile.isFile()) {
                    String filenameArray[] = oneFile.getName().split("\\.");
                    String ext = filenameArray[filenameArray.length - 1];
                    if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif")) {
                        try {
                            arrListToStoreIn.add(new AnalayzedImg(oneFile.getAbsolutePath()));
                        } catch (NullPointerException ex) {
                            //nothing, only to skip this file
                        }
                    }
                } else if (oneFile.isDirectory()) {
                    dirFiles.add(oneFile);
                }


            progBarProgress++;
            updateProgBar(progBarProgress, PROG_SET_PROGRESS, 1);
        }
        for (File dirFile : dirFiles) {
            if (Thread.currentThread().isInterrupted()) return;
            analyzePath(dirFile, arrListToStoreIn);
        }
    }

    /**
     * @param updateValue
     * @param updateType  0 to setViability, 1 to setMax, 2 to setProgress
     */
    public void updateProgBar(int updateValue, byte updateType, final int stageNumber) {
        final int val = updateValue;
        class myRunnable implements Runnable {
            int val;
            byte type;
            byte stage;

            myRunnable(int val, byte type, byte stageNumber) {
                this.val = val;
                this.type = type;
                this.stage = stageNumber;
            }

            @Override
            public void run() {
                switch (type) {
                    case PROG_SET_VISIBILITY:
                        progBar.setVisibility(val);
                        break;
                    case PROG_SET_MAX:
                        progBar.setMax(val);
                        String stageTxt = "";
                        switch (stageNumber) {
                            case 1:
                                stageTxt = (String) getText(R.string.progBar_stage_1);
                                break;
                            case 2:
                                stageTxt = (String) getText(R.string.progBar_stage_2);
                                break;
                            case 3:
                                stageTxt = (String) getText(R.string.progBar_stage_3);
                                break;
                        }
                        String txt = String.format("(%d) %s\n", stage, stageTxt);
                        txtToView.setText(txt);
                        logger.append(txt);
                        break;
                    case PROG_SET_PROGRESS:
                        progBar.setProgress(val);
                        break;
                }
            }
        }
        handlerOrPortal.post(new myRunnable(updateValue, updateType, (byte) stageNumber));
    }

    public void updateProgBar(int updateValue, byte updateType, final int stageNumber, final String extraInfos) {
        final int val = updateValue;
        class myRunnable implements Runnable {
            int val;
            byte type;
            byte stage;
            String extraInfo;

            myRunnable(int val, byte type, byte stageNumber, String extraInfo) {
                this.val = val;
                this.type = type;
                this.stage = stageNumber;
                this.extraInfo = extraInfo;
            }

            @Override
            public void run() {
                switch (type) {
                    case PROG_SET_VISIBILITY:
                        progBar.setVisibility(val);
                        break;
                    case PROG_SET_MAX:
                        progBar.setMax(val);
                        String stageTxt = "";
                        switch (stageNumber) {
                            case 1:
                                stageTxt = getText(R.string.progBar_stage_1) + " " + extraInfo;
                                break;
                            case 2:
                                stageTxt = getText(R.string.progBar_stage_2) + " " + extraInfo;
                                break;
                            case 3:
                                stageTxt = getText(R.string.progBar_stage_3) + " " + extraInfo;
                                break;
                        }
                        String txt = String.format("(%d) %s\n", stage, stageTxt);
                        txtToView.setText(txt);
                        logger.append(txt);
                        break;
                    case PROG_SET_PROGRESS:
                        progBar.setProgress(val);
                        break;
                }
            }
        }
        handlerOrPortal.post(new myRunnable(updateValue, updateType, (byte) stageNumber, extraInfos));
    }

    public void deleteArrListRepetition(ArrayList<ArrayList<AnalayzedImg>> resSet) {
        for (int row = 0; row < resSet.size(); row++) {
            compareRowToRow:
            for (int rowToCompare = 0; rowToCompare < resSet.size(); rowToCompare++) {
                if (row == rowToCompare)
                    continue;

                if (resSet.get(row).size() >= resSet.get(rowToCompare).size()) {
                    for (int coulmn = 0; coulmn < resSet.get(rowToCompare).size(); coulmn++)
                        if (!resSet.get(row).contains(resSet.get(rowToCompare).get(coulmn)))
                            continue compareRowToRow;

                    resSet.remove(rowToCompare);
                    row = 0;
                }
            }
        }
    }

    public void printAnalyzedImgInfos(AnalayzedImg analayzedImg) {
        for (int i = 0; i < 5; i++)
            System.err.println("Red: " + analayzedImg.getRed(i) + ", Green: " //test
                    + analayzedImg.getGreen(i) + ", Blue: " + analayzedImg.getBlue(i));
        System.err.println(analayzedImg.getImgPath());
        System.err.println("^^^^^^^^^^^^^^^^^");
    }

    //TODO: delete notEfficiantWaySadly method.
    public void notEfficiantWaySadly() {
        String absPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/imgsToTest/9GAG";
        File dir = new File(absPath);
        ArrayList<AnalayzedImg> preArrList = new ArrayList<>();
        analyzePath(dir, preArrList);
        AnalayzedImg[] analayzedImgsArr = new AnalayzedImg[preArrList.size()];
        analayzedImgsArr = preArrList.toArray(analayzedImgsArr);
        preArrList = null;
        System.err.println("images Count: " + analayzedImgsArr.length);
        for (AnalayzedImg oneA : analayzedImgsArr) {
            //System.err.println(oneA.getImgPath());
            System.err.println("Red: " + oneA.getRed(0) + ", Green: " + oneA.getGreen(0) + ", Blue: " + oneA.getBlue(0));
        }

        Arrays.sort(analayzedImgsArr);
        System.err.println("Sooooooooooorting!!!!!!----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        System.err.println("----------------------------------------------------");
        for (AnalayzedImg oneA : analayzedImgsArr) {
            //System.err.println(oneA.getImgPath());
            System.err.println("Red: " + oneA.getRed(0) + ", Green: " + oneA.getGreen(0) + ", Blue: " + oneA.getBlue(0));
        }

        System.err.println("----------------------------------------------------");
        ArrayList<ArrayList<AnalayzedImg>> lvlOne_res;
        lvlOne_res = findFirstDuplcationLvl(analayzedImgsArr);
        int sumOfCoparisons = 0;
        int innnndx = 0;
        for (ArrayList<AnalayzedImg> eachLvl1 : lvlOne_res) {
            for (AnalayzedImg oneA : eachLvl1) {
                //System.err.println(oneA.getImgPath());
                System.err.println("Red: " + oneA.getRed(0) + ", Green: " + oneA.getGreen(0) + ", Blue: " + oneA.getBlue(0));
            }
            System.err.println("Count of images in the (" + innnndx + ") group: " + eachLvl1.size());
            for (int iaia = eachLvl1.size(); iaia > 0; iaia--) {
                sumOfCoparisons += iaia;
            }
            System.err.println("-----------------------");
            innnndx++;
        }
        System.err.println("Count of comparisons: " + sumOfCoparisons);
        System.err.println("Count of duplicated: " + lvlOne_res.size());
    }


    //TODO: delete pickOneSubResSet method.
    public void pickOneSubResSet(int startIndx, int endIndx
            , AnalayzedImg[] anlisysRes, ArrayList<ArrayList<AnalayzedImg>> resLvl1, int testingPoint) {

        int subLastIndx = endIndx + 1;
        for (int innerElementTesting = startIndx + 1; innerElementTesting <= endIndx; innerElementTesting++) {
            if (anlisysRes[innerElementTesting].compareRed(anlisysRes[subLastIndx], testingPoint)) {
                while (subLastIndx < anlisysRes.length) {
                    // for testing the after last element of the subset,
                    //[startIndx 10][*15][17][22][endIndx 28]:[*31][Test: 34][70][110]
                    if (anlisysRes[innerElementTesting].compareRed(anlisysRes[subLastIndx + 1]))
                        subLastIndx++;
                    else
                        break;
                }
                ArrayList<AnalayzedImg> newSubset = new ArrayList<>();
                for (int i = innerElementTesting; i <= subLastIndx; i++) {
                    newSubset.add(anlisysRes[i]);
                }
                resLvl1.add(newSubset);
                pickOneSubResSet(innerElementTesting, subLastIndx, anlisysRes, resLvl1, testingPoint);
                break;
            }
        }
    }

    //TODO: delete findFirstDuplcationLvl method.
    public ArrayList<ArrayList<AnalayzedImg>> findFirstDuplcationLvl(AnalayzedImg[] analayzedImgs) {
        ArrayList<ArrayList<AnalayzedImg>> firstRes = new ArrayList<>();
        for (int i = 0; i < analayzedImgs.length; i++) {
            int j = i;
            for (; j + 1 < analayzedImgs.length; j++)
                if (!analayzedImgs[i].compareRed(analayzedImgs[j + 1])) break;
            ArrayList<AnalayzedImg> oneSet = new ArrayList<>();
            switch (j - i) {
                case 0:
                    break;
                case 1:
                    oneSet.add(analayzedImgs[i]);
                    oneSet.add(analayzedImgs[j]);
                    firstRes.add(oneSet);
                    i = j + 1;
                    break;
                default:
                    for (int innerII = i; innerII <= j; innerII++)
                        oneSet.add(analayzedImgs[innerII]);
                    firstRes.add(oneSet);

                    pickOneSubResSet(i, j, analayzedImgs, firstRes, 0);
                    i = j + 1;
                    break;
            }
        }

        return firstRes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "" + requestCode, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, data.getStringExtra(RES_KEY), Toast.LENGTH_SHORT).show();
    }
}
