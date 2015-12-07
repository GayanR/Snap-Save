package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import database.SnapAndSaveDataSource;
import differentmethods.DemoBase;

import differentmethods.ColorTemplate;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;


public class MainMenuActivity extends DemoBase implements
        OnChartValueSelectedListener {

    private static final String LOGTAG = "SS";

    ExpandableMenuOverlay menuOverlay;

    SnapAndSaveDataSource dataSource;

    private float income;
    private float expense;

    private PieChart mChart;

    private Typeface tf;

    ShowcaseView sv;

    //flag to check shared prefences -- app tour guide
    private static final String APPTOUR1 = "apptour1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initMainMenuButtons();

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initLoadChart();

        SharedPreferences settings = getSharedPreferences(APPTOUR1, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            initAppTour();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }


    }

    private void initAppTour() {
        ViewTarget target = new ViewTarget(R.id.trick_textview, this);
        sv = new ShowcaseView.Builder(this, true)
                .setTarget(target)
                .setContentTitle(R.string.MainMenuActivity_showcase_main_title)
                .setContentText(R.string.MainMenuActivity_showcase_main_message)
                .setStyle(R.style.CustomShowcaseTheme2)
                .hideOnTouchOutside()
                .build();

    }





    private void initLoadChart() {



        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);



        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);



        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        mChart.setCenterText("Your current month's \n Expenses");
        mChart.setCenterTextSize(20);
        setData(3, 100);

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);



    }

    private void initMainMenuButtons() {

        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {
                switch (action) {
                    case MID:

                        Intent intentM = new Intent(getApplicationContext(), SelectStatisticsEntryMethod.class);
                        startActivity(intentM);
                        // do stuff and dismiss
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case LEFT:
                        Intent intentL = new Intent(getApplicationContext(), SelectBillEntryMethod.class);
                        startActivity(intentL);
//                        finish();
//                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case RIGHT:
                        Intent intentR = new Intent(getApplicationContext(), SettingsActivity.class);

//                        Intent intentR = new Intent(getApplicationContext(), DebuggingActivity.class);
                        startActivity(intentR);
//                        finish();
//                        menuOverlay.getButtonMenu().toggle();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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

//        switch (item.getItemId()) {
//            case R.id.actionToggleValues: {
//                for (DataSet<?> set : mChart.getData().getDataSets())
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHole: {
//                if (mChart.isDrawHoleEnabled())
//                    mChart.setDrawHoleEnabled(false);
//                else
//                    mChart.setDrawHoleEnabled(true);
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionDrawCenter: {
//                if (mChart.isDrawCenterTextEnabled())
//                    mChart.setDrawCenterText(false);
//                else
//                    mChart.setDrawCenterText(true);
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleXVals: {
//
//                mChart.setDrawSliceText(!mChart.isDrawSliceTextEnabled());
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionSave: {
//                // mChart.saveToGallery("title"+System.currentTimeMillis());
//                mChart.saveToPath("title" + System.currentTimeMillis(), "");
//                break;
//            }
//            case R.id.actionTogglePercent:
//                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
//                mChart.invalidate();
//                break;
//            case R.id.animateX: {
//                mChart.animateX(1800);
//                break;
//            }
//            case R.id.animateY: {
//                mChart.animateY(1800);
//                break;
//            }
//            case R.id.animateXY: {
//                mChart.animateXY(1800, 1800);
//                break;
//            }
//        }
//        return true;
    }


    private void setData(int count, float range) {

        dataSource.open();

        Cursor inc = dataSource.getAllUserData();
        inc.moveToFirst();

        Calendar cal = Calendar.getInstance(Locale.US);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(cal.getTime());
        String[] splitStr;
        splitStr = formattedDate.split("\\-");

        Log.i(LOGTAG, "Month : " + splitStr[1]);

        double exp = dataSource.getTotalOfMonth(splitStr[1]);

        income = (float) inc.getDouble(3);
        Log.i(LOGTAG, "Income : " + Float.toString(income));

        expense = (float) exp;
        Log.i(LOGTAG, "Expense : " + Float.toString(expense));


        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        income = income - expense;
        yVals1.add(new Entry(income, 1));
        yVals1.add(new Entry(expense, 2));


        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Your Spendings");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.GREENANDRED_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }



    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
