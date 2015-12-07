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

import java.text.SimpleDateFormat;

import billfiltering.FilterForNolimit;
import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import model.BillItem;


public class NoNameNonStaticFilteredResultActivity extends AppCompatActivity {

    String[][] BillDetails;

    //LOGTAG
    private static final String LOGTAG = "SS";

    SnapAndSaveDataSource dataSource;

    static long billID;
    static int noOfRows;

    MaterialEditText Shopname;
    MaterialEditText Dates;
    MaterialEditText Times;
    MaterialEditText Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_name_non_static_filtered_result);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        String result;
        Intent intent = getIntent();
        result = intent.getStringExtra("result").toString();
        billID = intent.getLongExtra("id", 0);


        BillDetails = FilterForNolimit.nolimit(result);
        fill(BillDetails);
        check();
    }

    public void fill(String[][] bill) {

        Shopname = (MaterialEditText) findViewById(R.id.billMerch);
        Dates = (MaterialEditText) findViewById(R.id.billDate);
        Times = (MaterialEditText) findViewById(R.id.billTime);
        Total = (MaterialEditText) findViewById(R.id.billTotal);

        if (bill[0][0] != null) {
            Total.setText(bill[0][0]);
        }
        if (bill[1][0] != null) {
            Dates.setText(bill[1][0]);
        }



        noOfRows = bill.length - 2;


        TableLayout ll = (TableLayout) findViewById(R.id.allitems);
        TextView column1 = new TextView(this);
        column1.setTextColor(getResources().getColor(R.color.text_colors));
        column1.setTextSize(20);
        column1.setGravity(Gravity.CENTER);

        TextView column2 = new TextView(this);
        column2.setTextColor(getResources().getColor(R.color.text_colors));
        column2.setTextSize(20);
        column2.setGravity(Gravity.CENTER);

        TextView column3 = new TextView(this);
        column3.setTextColor(getResources().getColor(R.color.text_colors));
        column3.setTextSize(20);
        column3.setGravity(Gravity.CENTER);

        TextView column4 = new TextView(this);
        column4.setTextColor(getResources().getColor(R.color.text_colors));
        column4.setTextSize(20);
        column4.setGravity(Gravity.CENTER);


        TableRow row = new TableRow(this);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        column1.setText("Item Name");
        column2.setText("Price");
        column3.setText("Quantity");
        column4.setText("Subtotal");

        row.addView(column1);
        row.addView(column2);
        row.addView(column3);
        row.addView(column4);

        ll.addView(row);

        int index = 2;
        for (int i = 0; i < noOfRows; i++) {


            EditText item = new EditText(this);
            item.setTextColor(getResources().getColor(R.color.text_colors));
            item.setTextSize(20);

            EditText price = new EditText(this);
            price.setTextColor(getResources().getColor(R.color.text_colors));
            price.setTextSize(20);

            EditText quantity = new EditText(this);
            quantity.setTextColor(getResources().getColor(R.color.text_colors));
            quantity.setTextSize(20);

            EditText subtotal = new EditText(this);
            subtotal.setTextColor(getResources().getColor(R.color.text_colors));
            subtotal.setTextSize(20);

            row = new TableRow(this);

            lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            Log.v(LOGTAG, "Length :" + Integer.toString(noOfRows));
            item.setText(bill[index][0]);
            price.setText(bill[index][1]);
            quantity.setText(bill[index][2]);
            subtotal.setText(bill[index][3]);

            row.addView(item);
            row.addView(price);
            row.addView(quantity);
            row.addView(subtotal);

            ll.addView(row);
            index++;

        }


    }

    public void check() {

        if (TextUtils.isEmpty(Shopname.getText().toString()) && TextUtils.isEmpty(Dates.getText().toString()) && TextUtils.isEmpty(Times.getText().toString()) && TextUtils.isEmpty(Total.getText().toString())) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Filtration has failed!")
                    .setConfirmText("Ok!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            dataSource.deleteBill(billID);
                            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();

        }
        else if (TextUtils.isEmpty(Dates.getText().toString()) || TextUtils.isEmpty(Total.getText().toString())) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Some fields are empty!")
                    .show();
        }
        else{

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Successfull...")
                    .setContentText("Filtration successful!\n" +
                            "Make sure to fill the empty fields")
                    .setConfirmText("Done!")
                    .show();

        }
    }

    public void updateBill() {

        dataSource.open();

//        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
//
//        String formattedDate = df.format(Dates.getText().toString());

        boolean update = dataSource.updateBill(billID, Shopname.getText().toString(), Dates.getText().toString(), Times.getText().toString(), Double.parseDouble(Total.getText().toString()));

    }


    public void save(View view) {

        dataSource.open();
        if (!TextUtils.isEmpty(Long.toString(billID)) && !TextUtils.isEmpty(Shopname.getText().toString()) && !TextUtils.isEmpty(Dates.getText().toString()) && !TextUtils.isEmpty(Times.getText().toString()) && !TextUtils.isEmpty(Total.getText().toString())) {

            updateBill();
            int index = 2;
            for (int i = 0; i < noOfRows; i++) {

                enterBillItem(billID, BillDetails[index][0], Double.parseDouble(BillDetails[index][1]), Double.parseDouble(BillDetails[index][2]), Double.parseDouble(BillDetails[index][3]));
                index++;
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


    public void enterBillItem(long bid, String itemName, double price, double qty, double amount) {

        dataSource.open();

        BillItem billItem = new BillItem();
        billItem.set_billItemName(itemName);
        billItem.set_billItemPrice(price);
        billItem.set_billItemQuantity(qty);
        billItem.set_billItemAmount(amount);
        billItem.set_billItemBillId(bid);
        //BillItem item = dataSource.createBillItem(billItem);
        dataSource.createBillItem(billItem);
    }


    public void retry(View view) {
        dataSource.open();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure...?")
                .setConfirmText("Yes!")
                .setCancelText("No!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        boolean delete = dataSource.deleteBill(billID);
                        Intent intent = new Intent(getApplicationContext(), SmartScanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .showCancelButton(true)
                .show();


    }

    public void cancel(View view) {


        dataSource.open();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure...?")
                .setConfirmText("Yes!")
                .setCancelText("No!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        boolean delete = dataSource.deleteBill(billID);
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .showCancelButton(true)
                .show();


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_name_non_static_filtered_result, menu);
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
