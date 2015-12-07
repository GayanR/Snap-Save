package com.example.nuwan.nbqsafirstround;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;

import database.SnapAndSaveDataSource;
import differentmethods.ColorTemplate;
import differentmethods.DemoBase;


public class PieChartActivity extends DemoBase implements
        OnChartValueSelectedListener {

    private static final String LOGTAG = "SS";
    static String[][] arr;




    SnapAndSaveDataSource dataSource;


    private float expense,income;

    private PieChart mChart;

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);


        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();
        ArrayList<String> res = dataSource.getExpByCat();

        arr = new String[res.size()][2];

        int k = 0;
        for (String s : res) {

            String[] splitr = s.split("\\|\\|");
            arr[k][0] = splitr[0];
            arr[k][1] = splitr[1];
            k++;
        }

//        int k = 0;
//        while (res.moveToNext()) {
//            arr[k][0] = res.getString(1);
//            arr[k][1] = res.getString(4);
//            arr[k][2] = res.getString(6);
//
//            k++;
//        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mChart =configureChart(mChart);
        mChart = setData(mChart);




        Log.i("set Data arr.lenght", Integer.toString(arr.length));

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextSize(10);
    }

    public PieChart configureChart(PieChart chart){
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(false);

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

        mChart.setCenterText("Your Total\nExpenses");
        mChart.setCenterTextSize(20);

        return mChart;

    }

    private  PieChart setData(PieChart chart) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < arr.length; i++) {
            Log.i(LOGTAG + "arr length in loop", Integer.toString(arr.length));
            yVals1.add(new Entry((float) Double.parseDouble(arr[i][0]), i));
            Log.i(LOGTAG + "Values", Float.toString((float) Double.parseDouble(arr[i][0])));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            Log.i(LOGTAG + "arr length in loop2", Integer.toString(arr.length));
            xVals.add(arr[i][1]);
        }

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(3f);
        set1.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
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

        set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
        return mChart;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pie_chart, menu);
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
}
