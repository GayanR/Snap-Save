package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import billfiltering.FilterBus;
import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;


public class StaticFilteredResultActivity extends AppCompatActivity {

    //LOGTAG
    private static final String LOGTAG = "SS";

    static long billID;
    static String subCategory;

    SnapAndSaveDataSource dataSource;

    EditText billType,billDate,billTime,billTotal;


    String[] BillDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_filtered_result);

        if (savedInstanceState != null) {

            Toast.makeText(this, savedInstanceState.getString("message"), Toast.LENGTH_LONG).show();
        }
        else if(savedInstanceState == null) {

            dataSource = new SnapAndSaveDataSource(this);
            dataSource.open();


            String result;
            Intent intent = getIntent();
            result = intent.getStringExtra("result").toString();
            billID = intent.getLongExtra("billID", 0);
            subCategory = intent.getStringExtra("subCategory");

            billType = (EditText) findViewById(R.id.billType);
            billDate = (EditText) findViewById(R.id.billDate);
            billTime = (EditText) findViewById(R.id.billTime);
            billTotal = (EditText) findViewById(R.id.billTotal);


            BillDetails = FilterBus.filter(result);
            Log.v(LOGTAG, "BillDetails status : " + BillDetails[0]);


            if (BillDetails[0].compareTo("error") == 0) {

                String title = "Filtration Could Not Complete...";
                String msg = "Process has Failed\n" +
                        "The process could not capture all the details,\n" +
                        "You could either retry or enter the details manually and save.";

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(title)
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .show();


            } else if (BillDetails[0].compareTo("noerror") == 0) {


                String title = "Filtration Has Succeeded...";

                String msg = "Make sure every field has correct information\n" +
                        "You can either save or cancel";

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(title)
                        .setContentText(msg)
                        .show();

            }


            billType.setText(subCategory);
            billDate.setText(BillDetails[1]);
            billTime.setText(BillDetails[2]);
            billTotal.setText(BillDetails[3]);

        }

    }

    public void save(View view) {

        String date="",time;
        double total;

//        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
//        String formattedDate = df.format(date);

        date = billDate.getText().toString();
        time = billTime.getText().toString();
        total = Double.parseDouble(billTotal.getText().toString());

        updateBill(billID, date, time, total);

    }

    public void retry(View view) {

        dataSource.open();
        Log.v(LOGTAG, "retry");
        boolean delete = dataSource.deleteBill(billID);
        Log.v(LOGTAG, "Before if " + String.valueOf(delete));
        if (delete == true) {
            Log.v(LOGTAG, "Bill Deleted");
            CharSequence text = "Bill Deleted";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), SmartScanActivity.class);
            startActivity(intent);
            finish();
        }



    }

    public void cancel(View view) {

        dataSource.open();
        Log.v(LOGTAG, "retry");
        boolean delete = dataSource.deleteBill(billID);
        Log.v(LOGTAG, "Before if " + String.valueOf(delete));
        if (delete == true) {
            Log.v(LOGTAG, "Bill Deleted");
            CharSequence text = "Bill Deleted";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void updateBill(long bid, String date, String time, double total) {
        dataSource.open();
        boolean update = dataSource.updateBillNoName(bid, date, time, total);
        if (update == true) {
            CharSequence text = "Bill Has Been Saved";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();

        }
        else if (update == false) {
            CharSequence text = "Bill details not updated";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", "The Bill has been saved successfully");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_static_filtered_result, menu);
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
