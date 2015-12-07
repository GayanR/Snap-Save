package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import billfiltering.FilterAlgo;
import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import model.BillItem;


public class NonStaticFilteredResultActivity extends AppCompatActivity {

    //LOGTAG
    private static final String LOGTAG = "SS";

    static long billID;

    static String results[][];

    SnapAndSaveDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_static_filtered_result);

        if (savedInstanceState != null) {

            Toast.makeText(this, savedInstanceState.getString("message"), Toast.LENGTH_LONG).show();
        }
        else if(savedInstanceState == null) {

            dataSource = new SnapAndSaveDataSource(this);
            dataSource.open();


            Log.v(LOGTAG, "in filter");
            Intent intent = getIntent();
            String result = intent.getStringExtra("result");
            billID = intent.getLongExtra("id", 0);

            FilterAlgo fa = new FilterAlgo();
            Log.v(LOGTAG, "filter algo");
            results = fa.filter(result);
            Log.v(LOGTAG, "String came");
            init(results);
        }
    }

    public void init(String[][] result) {

        MaterialEditText Shopname = (MaterialEditText) findViewById(R.id.billMerch);
        MaterialEditText Dates = (MaterialEditText) findViewById(R.id.billDate);
        MaterialEditText Times = (MaterialEditText) findViewById(R.id.billTime);
        MaterialEditText Total = (MaterialEditText) findViewById(R.id.billTotal);

        if (result[0][0].equalsIgnoreCase("No Error")) {
            if (result[1][0] != null) {
                Shopname.setText(result[1][0]);

            }
            if (result[2][0] != null) {
                Dates.setText(result[2][0]);
            }
            if (result[3][0] != null) {
                Times.setText(result[3][0]);
                Log.v(LOGTAG, "Time" + result[3][0]);
            }
            if (result[4][0] != null) {
                Total.setText(result[4][0]);
            }


            TableLayout ll = (TableLayout) findViewById(R.id.allitems);

            TextView column1 = new TextView(this);
            column1.setTextColor(getResources().getColor(R.color.text_colors));
            column1.setTextSize(20);
            TextView column2 = new TextView(this);
            column2.setTextColor(getResources().getColor(R.color.text_colors));
            column2.setTextSize(20);
            TextView column3 = new TextView(this);
            column3.setTextColor(getResources().getColor(R.color.text_colors));
            column3.setTextSize(20);
//            column3.setGravity(Gravity);

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            column1.setText("Item Name");
            column2.setText("Quantity");
            column3.setText("Subtotal");
            row.addView(column1);
            row.addView(column2);
            row.addView(column3);


            ll.addView(row);
            for (int i = 0; i < result[5].length; i++) {
                EditText item = new EditText(this);
                item.setTextColor(getResources().getColor(R.color.text_colors));
                item.setTextSize(20);
                EditText quantity = new EditText(this);
                quantity.setTextColor(getResources().getColor(R.color.text_colors));
                quantity.setTextSize(20);
                EditText subtotal = new EditText(this);
                subtotal.setTextColor(getResources().getColor(R.color.text_colors));
                subtotal.setTextSize(20);


                row = new TableRow(this);
                lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                Log.v(LOGTAG, "Length :" + Integer.toString(result[5].length));
                item.setText(result[5][i]);
                quantity.setText(result[6][i]);
                subtotal.setText(result[7][i]);
                row.addView(item);
                row.addView(quantity);
                row.addView(subtotal);

                ll.addView(row);

            }

            String title = "Filtration Has Succeeded...";

            String msg = "Make sure every field has correct information\n" +
                    "You can either save or cancel";

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(msg)
                    .show();

        } else {

            String title = "Filtration Could Not Complete...";
            String msg = "Process has Failed\n" +
                    "The process could not capture all the details,\n" +
                    "You could either retry or enter the details manually and save.";

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(msg)
                    .setConfirmText("Ok")
                    .show();
        }

    }


    public void updateBill() {

        dataSource.open();
        boolean update = dataSource.updateBill(billID, results[1][0], results[2][0], results[3][0], Double.parseDouble(results[4][0]));

        if (update == true) {
            CharSequence text = "Bill details updated";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }
        else{
            CharSequence text = "Bill details not updated";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, text, duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }
    }

    public void save(View view) {

        if (!TextUtils.isEmpty(Long.toString(billID)) && !TextUtils.isEmpty(results[1][0]) && !TextUtils.isEmpty(results[2][0]) && !TextUtils.isEmpty(results[4][0])) {

            updateBill();
            for (int i = 0; i < results[5].length; i++) {

                enterBillItem(billID, results[5][i], Double.parseDouble(results[6][i]), Double.parseDouble(results[7][i]));

            }

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Successfull...")
                    .setContentText("All the details have been saved!")
                    .setConfirmText("Done!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        } else {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Fill all the fields!")
                    .show();

        }


    }

    public void enterBillItem(long bid, String itemName, double qty, double amount) {

        dataSource.open();

        BillItem billItem = new BillItem();
        billItem.set_billItemName(itemName);
        billItem.set_billItemQuantity(qty);
        billItem.set_billItemAmount(amount);
        billItem.set_billItemBillId(bid);
        //BillItem item = dataSource.createBillItem(billItem);
        dataSource.createBillItem(billItem);
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



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", "The Bill has been saved successfully");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_non_static_filtered_result, menu);
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
