package com.example.nuwan.nbqsafirstround;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import database.SnapAndSaveDataSource;


public class CalendarActivity extends AppCompatActivity {


    //logtag
    private static final String LOGTAG = "SS";

    SnapAndSaveDataSource dataSource;

    static String[][] arr;


    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    SimpleDateFormat ddmyyyy = new SimpleDateFormat("dd-M-yyyy",Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM", Locale.getDefault());




    private Map<Date, List<Booking>> bookings = new HashMap<>();
//    private Map<Date, String> bookings = new HashMap<>();

    public class Booking {
        private String title;
        private Date ds;

        public Booking(String title, Date ds) {
            this.title = title;
            this.ds = ds;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        loadCal();

//        dataSource.getCategoryExpense();
//        dataSource.getExpenseFromOneDay();



        final ActionBar actionBar = getSupportActionBar();
        final List<String> mutableBookings = new ArrayList<>();

        final ListView bookingsListView = (ListView) findViewById(R.id.bookings_listview);
//        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mutableBookings);
        final ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.calendar_adapter, R.id.list_content, mutableBookings);
        bookingsListView.setAdapter(adapter);
        CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);
        try {
            addEvents(compactCalendarView);

        } catch (Exception e) {

        }
        String[] dayNames = {"M", "T", "W", "T", "F", "S", "S"};
        compactCalendarView.setDayColumnNames(dayNames);

        //set initial title
        actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {


//                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
//                String s = formatter.format(dateClicked);

                ;
                List<Booking> bookingsFromMap = bookings.get(dateClicked);
                Log.d("MainActivity", "inside onclick " + dateClicked);
                //inside onclick Fri Jul 24 00:00:00 GMT+05:30 2015
                if (bookingsFromMap != null) {
                    Log.d("MainActivityss", bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Booking booking : bookingsFromMap) {
                        mutableBookings.add(booking.title);
                        Log.d("MainActivityss title", booking.title);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });
    }


    public void loadCal() {

        dataSource.open();
//        Cursor res = dataSource.getAllBillData();

        ArrayList<String> result = dataSource.getDailyExpenses();


        arr = new String[result.size()][3];

        int k = 0;
        for (String s : result) {
            String[] splitr = s.split("\\|\\|");
            arr[k][0] = splitr[0];
            arr[k][1] = splitr[1];
            arr[k][2] = splitr[2];
            k++;
        }

//        arr = new String[res.getCount()][3];
//
//        int k = 0;
//        while (res.moveToNext()) {
//            arr[k][0] = res.getString(1);
//            arr[k][1] = res.getString(4);
//            arr[k][2] = res.getString(6);
//
//            k++;
//        }
//
//        for (int i = 0; i < arr.length; i++) {
//            Log.i(LOGTAG + "name", arr[i][0]);
//            Log.i(LOGTAG + "amnt", arr[i][1]);
//            Log.i(LOGTAG + "date", arr[i][2]);
//        }

    }

    private void addEvents(CompactCalendarView compactCalendarView) throws ParseException {
        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();
//        Cursor res = dataSource.getAllBillData();

//        String[][] arr = new String[res.getCount()][3];
//
//        int k = 0;
//        while (res.moveToNext()) {
//            arr[k][0] = res.getString(1);//name
//            arr[k][1] = res.getString(4);//Date
//            arr[k][2] = res.getString(6);//Total
//
//            k++;
//        }



//        int  getCount= dataSource.getTotalCountExpense();
//        dataSource.getDateOfExpense();
//        String oneDate= dataSource.getOneDateOfExpense();
        Date dateSample = null;
        for (int i = 0; i < arr.length; i++) {


            try {
                dateSample = (Date) ddmyyyy.parse(arr[i][1]);
                currentCalender.setTimeInMillis(System.currentTimeMillis());

                currentCalender.add(Calendar.DATE, i);
                currentCalender.setTime(dateSample);
                compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(255, 255, 255, 255)));
//                bookings.put(currentCalender.getTime(), createBookings());
                bookings.put(currentCalender.getTime(), Arrays.asList(new Booking("Total Expenditure Rs : " + arr[i][0], currentCalender.getTime())));
                Log.i("getTime", currentCalender.getTime().toString());


            } catch (Exception e) {

            }


//            Log.i(LOGTAG + "name", arr[i][0]);
//            Log.i(LOGTAG + "amnt", arr[i][1]);
//            Log.i(LOGTAG + "date", arr[i][2]);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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
