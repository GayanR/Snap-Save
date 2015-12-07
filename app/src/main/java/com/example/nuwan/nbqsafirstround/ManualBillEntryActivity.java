package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import differentmethods.DateFormat;
import differentmethods.RandomNumber;
import differentmethods.SharedPreference;
import fr.ganfra.materialspinner.MaterialSpinner;
import model.Bill;


public class ManualBillEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //logtag
    private static final String LOGTAG = "SS";

    SnapAndSaveDataSource dataSource;

    private TextView dateTextView;
    private TextView textViewDate;
//    private FloatLabel total;

    MaterialEditText merchantName;
    MaterialEditText total;


    private static long billID;
    private static long prefValue;
    //Flag to check registered user using preferences
    public static final String REGISTERED = "registered";

    private static String category;
    private static String subCategory;


//    FloatLabel merchantName;

    MaterialSpinner spinner;
    MaterialSpinner subSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_bill_entry);

        subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
        initSpinner();

        String id = SharedPreference.getDefaults(REGISTERED, this);
        Log.i(LOGTAG, "ManualBillEntryActivity " + id);
        prefValue = Long.parseLong(id);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        billID = RandomNumber.generateRandomNumber();

        dateTextView = (TextView)findViewById(R.id.date_textview);
//        total =(FloatLabel)findViewById(R.id.total);
//        merchantName = (FloatLabel) findViewById(R.id.merchName);

        total =(MaterialEditText)findViewById(R.id.total);
        merchantName = (MaterialEditText) findViewById(R.id.merchName);

        textViewDate =(TextView)findViewById(R.id.tvDate);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ManualBillEntryActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    public void getAmount(View view){

        Intent intent = new Intent(this,DynamicProductRowGenerationActivity.class);
        intent.putExtra("billID", billID);
        total.setEnabled(false);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            Log.i(LOGTAG, Double.toString(data.getDoubleExtra("result", -1)));
            total.setText(data.getDoubleExtra("result", -1) + "");

        }

    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = "";
        monthOfYear++;
        if ((monthOfYear) == 1) {
            month = "Jan";

        } else if ((monthOfYear) == 2) {
            month = "Feb";
        } else if ((monthOfYear) == 3) {
            month = "Mar";
        } else if ((monthOfYear) == 4) {
            month = "Apr";
        } else if ((monthOfYear) == 5) {
            month = "May";
        } else if ((monthOfYear) == 6) {
            month = "Jun";
        } else if ((monthOfYear) == 7) {
            month = "Jul";

        } else if ((monthOfYear) == 8) {
            month = "Aug";
        } else if ((monthOfYear) == 9) {
            month = "Sep";
        } else if ((monthOfYear) == 10) {
            month = "Oct";
        } else if ((monthOfYear) == 11) {
            month = "Nov";
        } else if ((monthOfYear) == 12) {
            month = "Dec";
        }

        String date = "" + month + " " + dayOfMonth + "," + year;
        String normalDate = dayOfMonth + "-" + monthOfYear + "-" + year;
        Log.i(LOGTAG, normalDate);
        dateTextView.setText(date);
        textViewDate.setText(normalDate);

    }

    public void save(View view) {

        if (!TextUtils.isEmpty(category)) {

            if (!TextUtils.isEmpty(subCategory) && !TextUtils.isEmpty(merchantName.getText().toString()) && !TextUtils.isEmpty(textViewDate.getText().toString()) && !TextUtils.isEmpty(total.getText().toString())) {

                enterBillDetailsToDB();
            }


        }
        else if (TextUtils.isEmpty(category) || TextUtils.isEmpty(merchantName.getText().toString()) || TextUtils.isEmpty(textViewDate.getText().toString()) || TextUtils.isEmpty(total.getText().toString())) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Fill all the fields!")
                    .show();


        }


    }

    public void cancel(View view) {

//        Intent intent = new Intent(getApplicationContext(), SelectBillEntryMethod.class);
//        startActivity(intent);
//        finish();

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
                        Intent intent = new Intent(getApplicationContext(), SelectBillEntryMethod.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .showCancelButton(true)
                .show();

    }

    public void enterBillDetailsToDB() {

        dataSource.open();
        String date = DateFormat.changeDateFormat(textViewDate.getText().toString());

        if (dataSource.checkBill(billID)) {

            boolean check = dataSource.updateBill(billID, merchantName.getText().toString(), category, subCategory, date, Double.parseDouble(total.getText().toString()));

            if (check) {

                String title = "Successfull...";

                String msg = "All the data have been saved!";

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(title)
                        .setContentText(msg)
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

            }
            else{

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Saving has failed!")
                        .show();

            }

        }
        else if (!dataSource.checkBill(billID)) {

            Bill bill = new Bill();
            bill.set_billId(billID);
            bill.set_billUserId(prefValue);
            bill.set_billName(merchantName.getText().toString());
            bill.set_billCat(category);
            bill.set_billSubCat(subCategory);
            bill.set_billDate(date);
            bill.set_billTotal(Double.parseDouble(total.getText().toString()));

            dataSource.createBill(bill);

            String title = "Successfull...";

            String msg = "All the data have been saved!";

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(msg)
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
        }


    }

    public void initSpinner() {

        String[] ITEMS = {"Transportation", "Grocery", "Alcohol and Tobacco", "Communication", "Meal", "Fast Food", "Clothes", "Utility", "Entertainment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
    }


    public void initSubSpinner() {

        String[] Transportation = {"Bus", "Cab", "Train", "Flight"};
        String[] Grocery = {"Keels", "Arpico", "Cargills", "Sathosa", "Laughs"};
        String[] Communication = {"Reload", "Cards"};
        String[] AlcoholandTobacco = {"Wine", "Whiskey", "Cigarettes", "Brandy", "Beer"};
        String[] Meal = {"Hilton", "Galadari", "Jetwing"};
        String[] FastFood = {"PnS", "Fab", "Caravan", "McDonalds", "KFC", "PizzaHut", "Dinemore", "Dominos"};
        String[] Clothes = {"Nolimit", "Odel"};
        String[] Utility = {"Electricity", "Water", "Telephone"};
        String[] Entertainment = {"Movie", "Bowling", "Gaming"};
        String[] Other = {"Other"};


        switch(category){
            case "Transportation" :
                ArrayAdapter<String> adapterT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Transportation);
                adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterT);
                break;
            case "Grocery" :
                ArrayAdapter<String> adapterG = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Grocery);
                adapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterG);
                break;
            case "Communication" :
                ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Communication);
                adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterC);
                break;
            case "Alcohol and Tobacco" :
                ArrayAdapter<String> adapterAAT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AlcoholandTobacco);
                adapterAAT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterAAT);
                break;
            case "Meal" :
                ArrayAdapter<String> adapterM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Meal);
                adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterM);
                break;
            case "Fast Food" :
                ArrayAdapter<String> adapterFF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FastFood);
                adapterFF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterFF);
                break;
            case "Clothes" :
                ArrayAdapter<String> adapterCL = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Clothes);
                adapterCL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterCL);
                break;
            case "Utility" :
                ArrayAdapter<String> adapterU = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utility);
                adapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterU);
                break;
            case "Entertainment" :
                ArrayAdapter<String> adapterE = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Entertainment);
                adapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterE);
                break;
            case "Other" :
                ArrayAdapter<String> adapterO = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Other);
                adapterO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterO);
                break;
            default :

        }


    }

    @Override
    public void onBackPressed() {

        boolean delete = dataSource.deleteBill(billID);
        Intent intent = new Intent(getApplicationContext(), SelectBillEntryMethod.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }


    @Override
    protected void onResume() {
        super.onResume();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category = spinner.getSelectedItem().toString();

                subSpinner.setVisibility(View.VISIBLE);
                initSubSpinner();

//                CharSequence toastmsg = category;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(getApplicationContext(), toastmsg, duration);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//                toast.show();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subCategory = subSpinner.getSelectedItem().toString();
                merchantName.setText(subCategory);

//                CharSequence toastmsg = subCategory;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(getApplicationContext(), toastmsg, duration);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manual_bill_entry, menu);
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
