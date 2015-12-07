package com.example.nuwan.nbqsafirstround;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import database.SnapAndSaveDataSource;
import fr.ganfra.materialspinner.MaterialSpinner;


public class BarChartActivity extends AppCompatActivity {

    MaterialSpinner spinner;
    private static String spinnerMonth;

    SnapAndSaveDataSource dataSource;
    String arr[][];

    private static final String LOGTAG = "SS";

    Double totalExpenseForMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        initSpinner();

        Log.i(LOGTAG, "Inside on create");
        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

//        String m = getMonth();
//        loadBarGraph(m);


    }

    private String getMonth() {

        Calendar cal = Calendar.getInstance(Locale.US);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(cal.getTime());
        String[] splitStr;
        splitStr = formattedDate.split("\\-");

        Log.i(LOGTAG, "Month : " + splitStr[1]);

        return  splitStr[1];


    }

    private void loadBarGraph(String month) {

        ArrayList<String> dateExp;
        Log.i(LOGTAG, "Before getDailyExpenses");


//        dateExp = dataSource.getDailyExpenses();
        dateExp = dataSource.getDailyExpensesForSpecificMonth(month);

        Log.i(LOGTAG, "After getDailyExpenses");
        arr = new String[dateExp.size()][3];
        String str[],strNew[];
        int i = 0;
        for (String dateEx : dateExp) {

            str = dateEx.split("\\|\\|");
            strNew = str[1].split("\\-");



            Log.i(LOGTAG, "Inside for each "+str[0]+" "+str[1]);
            arr[i][0] = str[0];
            arr[i][1] = strNew[0];
            arr[i][2] = strNew[1];
            Log.i(LOGTAG, "Data for X : "+str[0]+" | "+strNew[0]+" | "+strNew[1]);
            i++;
            str[0] = null;
            str[1] = null;
        }

        BarChart chart = (BarChart) findViewById(R.id.chart);

        Log.i(LOGTAG, "Before BarData");
        //BarData data = new BarData(getXAxisValues(arr), getDataSet(arr));
        BarData data = new BarData(getXAxisValuesNew(arr), getDataSetNew(arr));

        chart.setData(data);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDescription("Expenses Per Month");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        chart.setVisibleXRange(6f);

//        Calendar c = Calendar.getInstance(Locale.US);
//        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
//        String formattedDate = df.format(c.getTime());
//        String[] splitStr;
//        splitStr = formattedDate.split("\\-");
        if (getMonth().equals(month)) {

        Log.i(LOGTAG, "Month : " + month);

//        totalExpenseForMonth = dataSource.getTotalOfMonth(splitStr[1]);
        totalExpenseForMonth = dataSource.getTotalOfMonth(month);
        Log.i(LOGTAG, "totalExpenseForMonth : " + Double.toString(totalExpenseForMonth));

//        Cursor res = dataSource.getUniqueDays(month);
//        Log.i(LOGTAG, "count : " + Integer.toString(res.getCount()));




            Log.i(LOGTAG, "Inside If GetMonth : " + getMonth());
            Log.i(LOGTAG, "Inside If Month : " + month);

            float x = (Float.parseFloat(Double.toString(totalExpenseForMonth)))/4;
            Log.i(LOGTAG, "totalExpenseForMonth " + Float.toString(x));

            YAxis leftAxis = chart.getAxisLeft();
//        LimitLine l1 = new LimitLine(x, Double.toString(totalExpenseForMonth));
            LimitLine l1 = new LimitLine(x, "Estimated Expense Of Next Week");
            Log.i(LOGTAG, "after limit line ");
            l1.setLineColor(Color.RED);
            l1.setLineWidth(4f);
            l1.setTextColor(Color.BLACK);
            l1.setTextSize(12f);


            leftAxis.addLimitLine(l1);
            Log.i(LOGTAG, "after limit line ");

        }



    }


    private ArrayList<BarDataSet> getDataSetNew (String arr[][]){
        ArrayList<BarDataSet> dataSets= null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {


            BarEntry v1e1 = new BarEntry(Float.parseFloat(arr[i][0]), i); // Jan
            valueSet1.add(v1e1);


        }
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Expenses");
        barDataSet1.setColors(ColorTemplate.LIBERTY_COLORS);
        barDataSet1.setBarSpacePercent(50f);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }


    private  ArrayList<String > getXAxisValuesNew(String arr[][]){
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=0; i< arr.length;i++) {

            switch (arr[i][2]) {
                case "1":
                    xAxis.add(arr[i][1] + "-Jan");
                    break;
                case "2":
                    xAxis.add(arr[i][1] + "-Feb");
                    break;
                case "3":
                    xAxis.add(arr[i][1] + "-Mar");
                    break;
                case "4":
                    xAxis.add(arr[i][1] + "-Apr");
                    break;
                case "5":
                    xAxis.add(arr[i][1] + "-May");
                    break;
                case "6":
                    xAxis.add(arr[i][1] + "-Jun");
                    break;
                case "7":
                    xAxis.add(arr[i][1] + "-Jul");
                    break;
                case "8":
                    xAxis.add(arr[i][1] + "-Aug");
                    break;
                case "9":
                    xAxis.add(arr[i][1] + "-Sep");
                    break;
                case "10":
                    xAxis.add(arr[i][1] + "-Oct");
                    break;
                case "11":
                    xAxis.add(arr[i][1] + "-Nov");
                    break;
                case "12":
                    xAxis.add(arr[i][1] + "-Dec");
                    break;
                default:
                    break;

            }



        }
        return xAxis;
    }

    public void initSpinner() {

        String[] ITEMS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerMonth = spinner.getSelectedItem().toString();

                switch (spinnerMonth) {
                    case "January":
                        loadBarGraph("1");
                        break;
                    case "February":
                        loadBarGraph("2");
                        break;
                    case "March":
                        loadBarGraph("3");
                        break;
                    case "April":
                        loadBarGraph("4");
                        break;
                    case "May":
                        loadBarGraph("5");
                        break;
                    case "June":
                        loadBarGraph("6");
                        break;
                    case "July":
                        loadBarGraph("7");
                        break;
                    case "August":
                        loadBarGraph("8");
                        break;
                    case "September":
                        loadBarGraph("9");
                        break;
                    case "October":
                        loadBarGraph("10");
                        break;
                    case "November":
                        loadBarGraph("11");
                        break;
                    case "December":
                        loadBarGraph("12");
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                loadBarGraph(getMonth());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_chart, menu);
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
