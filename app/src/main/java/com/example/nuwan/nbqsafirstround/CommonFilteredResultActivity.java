package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.Time;
import java.util.ArrayList;

import billfiltering.CommonAlgo;
import billfiltering.CommonAlgoFinal;
import billfiltering.FilterAlgo;
import billfiltering.FilterForNolimit;
import billfiltering.RemoveRedundant;
import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import dictionaryandcategory.AutoArrange;
import dictionaryandcategory.Dictionary;
import differentmethods.DateFormat;
import model.BillItem;
import redundant.Duplicate;

public class CommonFilteredResultActivity extends AppCompatActivity {

    String[][] BillDetails; //stores bill info which comes from the filtration
    String[][] newBillDetails; //used when saving
    String[][] DuplicateRemoved; //used when removing duplicated rows
    String[][] CorrectedItem; //used to correct item names and categorize them


    //LOGTAG
    private static final String LOGTAG = "SS";

    SnapAndSaveDataSource dataSource;

    static long billID;
    static int noOfRows;
    static String subCategory;
    static String Category;

    MaterialEditText Shopname;
    MaterialEditText Dates;
    MaterialEditText Times;
    MaterialEditText Total;
    TableLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_filtered_result);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        String result;
        Intent intent = getIntent();

        result = intent.getStringExtra("result").toString();
        Log.i(LOGTAG, "Result is :\n\n " + result + "\n\n");
        billID = intent.getLongExtra("billID", 0);
        Log.i(LOGTAG, "Bill ID : " + Long.toString(billID));
        subCategory = intent.getStringExtra("subCategory");
        Category = intent.getStringExtra("Category");

        boolean checkAlgo = intent.getBooleanExtra("checkAlgo", false);
        Log.i(LOGTAG, "checkAlgo is :\n\n " + checkAlgo + "\n\n");

        ll = (TableLayout) findViewById(R.id.allitems);
        boolean checkFail = false;

        if (checkAlgo && Category == "Grocery") {

            BillDetails[0] = intent.getStringArrayExtra("Merchant");
            BillDetails[1] = intent.getStringArrayExtra("date");
            BillDetails[2] = intent.getStringArrayExtra("time");
            BillDetails[3] = intent.getStringArrayExtra("total");
            String[] item = intent.getStringArrayExtra("item");
            String[] quantity = intent.getStringArrayExtra("quantity");
            String[] subtotal = intent.getStringArrayExtra("subtotal");
            for (int i = 0; i < item.length; i++) {
                BillDetails[i + 4][0] = item[i];
                try {
                    BillDetails[i + 4][1] = Double.toString(Double.parseDouble(subtotal[i]) / Double.parseDouble(quantity[i]));
                } catch (NumberFormatException n) {
                    BillDetails[i + 4][1] = "";
                }

                BillDetails[i + 4][2] = quantity[i];
                BillDetails[i + 4][3] = subtotal[i];

            }
        } else {

            try {

                BillDetails = CommonAlgoFinal.common(result);
            } catch (Exception e) {
                checkFail = true;
            }

        }
//        boolean checkFail = false;
//        try {
//
//            BillDetails = CommonAlgoFinal.common(result);
//        }catch(Exception e) {
//            checkFail = true;
//
//        }
//
        if(checkFail){
            failDialogBox();
        }

        boolean check = Duplicate.checkRedudant(BillDetails);
        if (check == false) {
            Log.i(LOGTAG, "Inside 1st if");
            DuplicateRemoved = Duplicate.removeSame(BillDetails);//removing duplicate rows
            tableFlush(DuplicateRemoved);//filling the table
            check();

        }
        else{
            Log.i(LOGTAG, "Inside 1st else");
            tableFlush(BillDetails);
            check();
        }

    }

    private void failDialogBox(){
        SweetAlertDialog pDialog = new SweetAlertDialog(CommonFilteredResultActivity.this, SweetAlertDialog.ERROR_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Error...!");
        pDialog.setContentText("The Process has failed.\nInput using manual entry.\nClick Ok to continue.");
        pDialog.setConfirmText("Ok");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dataSource.deleteBill(billID);
                Intent intent = new Intent(getApplicationContext(), ManualBillEntryActivity.class);
                startActivity(intent);
                finish();

            }
        });
        pDialog.show();
    }

    public void tableFlush(String[][] bill) {

        Shopname = (MaterialEditText) findViewById(R.id.billMerch);
        Dates = (MaterialEditText) findViewById(R.id.billDate);
        Times = (MaterialEditText) findViewById(R.id.billTime);
        Total = (MaterialEditText) findViewById(R.id.billTotal);

        if (!TextUtils.isEmpty(bill[0][0])) {
            Shopname.setText(bill[0][0]);
        } else if (TextUtils.isEmpty(bill[0][0])) {
            Shopname.setText(subCategory);
        }

        if (bill[1][0] != null) {
            Dates.setText(bill[1][0]);
            Log.i(LOGTAG, "Date is 1 : " + bill[1][0]);
        }
        if (bill[2][0] != null) {
            Times.setText(bill[2][0]);
        }
        if (bill[3][0] != null) {
            Total.setText(bill[3][0]);
        }

        Log.i(LOGTAG, "Bill length : " + Integer.toString(bill.length));
        if (bill.length != 4) {

            //remove the 1st 4 rows which were utilized for merchant,date,time and total
            noOfRows = bill.length - 4;
            Log.i(LOGTAG, "Number of Rows : " + Integer.toString(noOfRows));


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

            TextView column5 = new TextView(this);
            column5.setTextColor(getResources().getColor(R.color.text_colors));
            column5.setTextSize(20);
            column5.setGravity(Gravity.CENTER);


            TableRow row = new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);

            column1.setText("Item Name ");
            column2.setText("Price ");
            column3.setText("Quantity  ");
            column4.setText("Subtotal ");
            column5.setText("Category ");

            row.addView(column1);
            row.addView(column2);
            row.addView(column3);
            row.addView(column4);
            row.addView(column5);

            ll.addView(row);

//            ArrayList<String> input = new ArrayList<String>();
//            int repeat = 4;
//            for (int i = 0; i < noOfRows; i++) {
//
//                String put = bill[repeat][0] + " " + bill[repeat][1] + " " + bill[repeat][2] + " " + bill[repeat][3];
//                input.add(put);
//                repeat++;
//
//            }
//
//            RemoveRedundant rr = new RemoveRedundant();
//            ArrayList<String> output = rr.removeSame(input);
//
////            int repeatTwo = 4;
//
//            for (int i = 0; i < output.size(); i++) {
//                String[] splitStr;
//                splitStr = output.get(i).split(" ");



            CorrectedItem = AutoArrange.autoCorrect(bill);
            int indexOne = 4;
            int indexTwo = 0;
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

                EditText category = new EditText(this);
                category.setTextColor(getResources().getColor(R.color.text_colors));
                category.setTextSize(20);

                row = new TableRow(this);

                lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                Log.v(LOGTAG, "Length :" + Integer.toString(noOfRows));



                    item.setText(CorrectedItem[indexTwo][0]);
                    price.setText(bill[indexOne][1]);
                    quantity.setText(bill[indexOne][2]);
                    subtotal.setText(bill[indexOne][3]);
                    category.setText(CorrectedItem[indexTwo][1]);



//                String subTotal = checkSubTotal(bill[index][1], bill[index][2]);
//
//                if (subTotal.equals(bill[index][3])) {
//                    subtotal.setText(bill[index][3]);
//                } else {
//                    subtotal.setText(subTotal);
//                }


                row.addView(item);
                row.addView(price);
                row.addView(quantity);
                row.addView(subtotal);
                row.addView(category);

                ll.addView(row);
                indexOne++;
                indexTwo++;

            }
        }


    }

//    public String checkSubTotal(String price, String quantity) {
//
//        double mul = 0;
//
//        if (quantity.equals("1")) {
//            return price;
//        } else if (!quantity.equals("1")) {
//            double p = Double.parseDouble(price);
//            double q = Double.parseDouble(quantity);
//            mul = mul + (p * q);
//
//        }
//        return Double.toString(mul);
//    }

//    public String checkPrice(String quantity, String subTotal) {
//
//        double mul = 0;
//
//        if (quantity == "1") {
//            return subTotal;
//        }else if (quantity != "1") {
//            double s = Double.parseDouble(subTotal);
//            double q = Double.parseDouble(quantity);
//            mul = mul + (s / q);
//
//        }
//        return Double.toString(mul);
//    }

    public void check() {

        if (TextUtils.isEmpty(Shopname.getText().toString()) && TextUtils.isEmpty(Dates.getText().toString()) && TextUtils.isEmpty(Times.getText().toString()) && TextUtils.isEmpty(Total.getText().toString())) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Filtration has failed !")
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

        } else if (TextUtils.isEmpty(Shopname.getText().toString()) || TextUtils.isEmpty(Dates.getText().toString()) || TextUtils.isEmpty(Times.getText().toString()) || TextUtils.isEmpty(Total.getText().toString())) {

            if (TextUtils.isEmpty(Times.getText().toString())) {
                Times.setText("No need to fill !");
                Times.setEnabled(false);
            }

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Some fields are empty !\n" +
                            "If available, please fill the relevant fields.")
                    .show();
        } else {


            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Successfull...")
                    .setContentText("Filtration successful !\n" +
                            "Please check whether the fields are accurate.\n" +
                            "If necessary do the changes.")
                    .setConfirmText("Done!")
                    .show();

        }
    }

    public void updateBill() {

        dataSource.open();

        String newDate = null;
        String date = DateFormat.changeDateFormat(Dates.getText().toString());
        String[] splitter = date.split("\\-");
        if (splitter[1].contains("0")) {
            newDate = splitter[0] + "-" + splitter[1].replaceAll("0", "") + "-" + splitter[2];
        }else {
            newDate = date;
        }

        if (Times.getText().toString() == "No need to fill !") {
            boolean update = dataSource.updateBill(billID, Shopname.getText().toString(), newDate, "00:00:00", Double.parseDouble(Total.getText().toString()));

        } else {
            boolean update = dataSource.updateBill(billID, Shopname.getText().toString(), newDate, Times.getText().toString(), Double.parseDouble(Total.getText().toString()));

        }


    }

    public void getDynamicValues() {

        int rowIndex = 1;
        newBillDetails = new String[noOfRows][5];
        for (int i = 0; i < noOfRows; i++) {

            for (int j = 0; j < 5; j++) {
                TableRow tr = (TableRow) ll.getChildAt(rowIndex);
                EditText ed = (EditText) tr.getChildAt(j);
                newBillDetails[i][j] = ed.getText().toString();

            }
            rowIndex++;

        }

    }

    public void save(View view) {

        dataSource.open();

        if (!TextUtils.isEmpty(Long.toString(billID)) && !TextUtils.isEmpty(Shopname.getText().toString()) && !TextUtils.isEmpty(Dates.getText().toString()) && !TextUtils.isEmpty(Times.getText().toString()) && !TextUtils.isEmpty(Total.getText().toString())) {

            if (BillDetails.length == 4) {
                updateBill();

            } else {

                updateBill();
                getDynamicValues();
//                int index = 4;
                for (int i = 0; i < noOfRows; i++) {

                    enterBillItem(billID, newBillDetails[i][0], Double.parseDouble(newBillDetails[i][1]), Double.parseDouble(newBillDetails[i][2]), Double.parseDouble(newBillDetails[i][3]), newBillDetails[i][4]);
//                    index++;
                }


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


    public void enterBillItem(long bid, String itemName, double price, double qty, double amount, String category) {

        dataSource.open();

        BillItem billItem = new BillItem();
        billItem.set_billItemName(itemName);
        billItem.set_billItemPrice(price);
        billItem.set_billItemQuantity(qty);
        billItem.set_billItemAmount(amount);
        billItem.set_billItemBillId(bid);
        billItem.set_billItemCat(category);

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

        Log.i(LOGTAG, "Bill ID cancel : " + Long.toString(billID));
        dataSource.open();

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure...?")
                .setCancelText("No!")
                .setConfirmText("Yes!")
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
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common_filtered_result, menu);
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
