package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import differentmethods.SharedPreference;
import model.Bill;
import model.BillItem;


public class DynamicProductRowGenerationActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    private ScrollView sv;
    private LinearLayout l1;
    private LinearLayout l2;

    SnapAndSaveDataSource dataSource;
    private static long billID;
    private static long prefValue;

    //Flag to check registered user using preferences
    public static final String REGISTERED = "registered";




    List<TextView> name_list = new ArrayList<TextView>();
    List<TextView> amount_list = new ArrayList<TextView>();

    //logtag
    private static final String LOGTAG = "SS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_product_row_generation);

        Intent intent = getIntent();
        billID = intent.getLongExtra("billID", 0);

        String id = SharedPreference.getDefaults(REGISTERED, this);
        Log.i(LOGTAG, "DynamicProductRowGenerationActivity " + id);
        prefValue = Long.parseLong(id);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        sv = (ScrollView)findViewById(R.id.addProduct_Form);
        l1= (LinearLayout)findViewById(R.id.add_form);
    }

    @Override
    public void onBackPressed() {

        boolean delete = dataSource.deleteBill(billID);
        Intent intent = new Intent(getApplicationContext(), ManualBillEntryActivity.class);
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
        dataSource.open();

    }

    public void create(View v){


        EditText etv1 = new EditText(this);
        EditText etv2 = new EditText(this);
//        etv1.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//        etv2.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        etv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        etv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));


        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(300,100); // Width , height
        etv1.setLayoutParams(lparams);
        etv2.setLayoutParams(lparams);


        etv1.setHint("Item Name");
        etv1.setTextColor(getResources().getColor(R.color.text_colors));
        etv1.setHintTextColor(getResources().getColor(R.color.text_colors));
        etv1.setInputType(InputType.TYPE_CLASS_TEXT);
        etv1.setTextSize(15);
//        etv1.setGravity(Gravity.START);


        etv2.setHint("Item Amount");
        etv2.setHintTextColor(getResources().getColor(R.color.text_colors));
        etv2.setTextColor(getResources().getColor(R.color.text_colors));
        etv2.setInputType(InputType.TYPE_CLASS_NUMBER);
        etv2.setTextSize(15);

        l2 = new LinearLayout(this);
        l2.setOrientation(LinearLayout.HORIZONTAL);
        l2.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        l2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        l2.setWeightSum(2f);

        name_list.add(etv1);
        amount_list.add(etv2);

        l2.addView(etv2,0);
        l2.addView(etv1,0);
        if(l2.getParent()!=null)
            ((ViewGroup) l2.getParent()).removeView(l2);
        l1.addView(l2);



    }

    public void remove(View v){
        l1.removeAllViews();
    }

    Intent returnIntent;
    double sum = 0;

    public void done(View v) {

        //create bill
        dataSource.open();
        Bill bill = new Bill();
        bill.set_billId(billID);
        bill.set_billUserId(prefValue);
        dataSource.createBillTwo(bill);


        returnIntent = new Intent();

        //insert products to bill item
        for (int i = 0; i < amount_list.size(); i++) {

            BillItem billItem = new BillItem();
            billItem.set_billItemName(name_list.get(i).getText().toString());
            billItem.set_billItemAmount(Double.parseDouble(amount_list.get(i).getText().toString()));
            billItem.set_billItemBillId(billID);

            dataSource.createBillItemTwo(billItem);

        }



        String sumNew = "";
        for (TextView tview : amount_list) {
            sum += Double.parseDouble(tview.getText().toString());
            sumNew = Double.toString(sum);
            Log.i(LOGTAG + " The total amount: ", sumNew);


        }

        String title = "Successfull...";

        String msg = "Item list has been updated!";

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmText("Done!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        returnIntent.putExtra("result", sum);
                        Log.i(LOGTAG + "The total amount is ,", Double.toString(sum));
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .show();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dynamic_product_row_generation, menu);
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
