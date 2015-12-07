package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;


import org.opencv.android.Utils;

import java.io.File;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import differentmethods.CroppedImageFile;
import differentmethods.RandomNumber;
import differentmethods.SharedPreference;
import fr.ganfra.materialspinner.MaterialSpinner;
import model.Bill;


public class SmartScanActivity extends AppCompatActivity {

    //LOGTAG
    private static final String LOGTAG = "SS";

    //Place to store the photos and tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";

    //Place to store the photos and tess data
    public static final String DATA_PATH_CROPPED_IMAGES = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/croppedImages/";

    private String _path;
    private String _path2;

    private static String category;
    private static String subCategory = null;

    private static long prefValue;
    private static long billID;
    SnapAndSaveDataSource dataSource;

    //Flag to check registered user using preferences
    public static final String REGISTERED = "registered";

    //flag to check shared prefences -- app tour guide
    private static final String APPTOUR3 = "apptour3";

    MaterialSpinner spinner;
    MaterialSpinner subSpinner;


    ShowcaseView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_scan);

        erasePreviousImages();

        subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
        initSpinner();

        _path = DATA_PATH + "Testocrs.jpg";


        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        String id = SharedPreference.getDefaults(REGISTERED, this);
        prefValue = Long.parseLong(id);

        SharedPreferences settings = getSharedPreferences(APPTOUR3, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            initAppTour();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

    }

    private void initAppTour() {
        ViewTarget target = new ViewTarget(R.id.btnStartCamera, this);
        sv = new ShowcaseView.Builder(this, true)
                .setTarget(target)
                .setContentTitle(R.string.SmartScanActivity_showcase_main_title)
                .setContentText(R.string.SmartScanActivity_showcase_main_message)
                .setStyle(R.style.CustomShowcaseTheme2)
                .hideOnTouchOutside()
                .build();
        sv.setShowcaseY(sv.getShowcaseY() - 500);
        sv.show();
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
        String[] Clothes = {"Nolimit", "Odel"};
        String[] FastFood = {"PnS", "Fab", "Caravan", "McDonalds", "KFC", "PizzaHut", "Dinemore", "Dominos"};
//        String[] Utility = {"Electricity", "Water", "Telephone"};
        String[] Entertainment = {"Movie", "Bowling", "Gaming"};


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
//            case "Utility" :
//                ArrayAdapter<String> adapterU = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utility);
//                adapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
//                subSpinner.setAdapter(adapterU);
//                break;
            case "Entertainment" :
                ArrayAdapter<String> adapterE = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Entertainment);
                adapterE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subSpinner = (MaterialSpinner) findViewById(R.id.spinnerSub);
                subSpinner.setAdapter(adapterE);
                break;
            default :

        }


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

//               CharSequence toastmsg = subCategory;
//               int duration = Toast.LENGTH_SHORT;
//               Toast toast = Toast.makeText(getApplicationContext(), toastmsg, duration);
//               toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//               toast.show();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    public void cancel(View view) {

        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();

    }



    public void startCamera(View view) {

        Log.v(LOGTAG, "SUB :" + subCategory);

        if (!subCategory.equals("Select Sub Category")) {

            enterBillDetailsToDB();

        } else {

//            CharSequence toastmsg = "Please select a category !!!";
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(getApplicationContext(), toastmsg, duration);
//            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//            toast.show();

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please select a category !!!")
                    .show();

        }

    }

    public void enterBillDetailsToDB() {

        dataSource.open();
        Bill bill = new Bill();

        billID = RandomNumber.generateRandomNumber();
        bill.set_billId(billID);
        bill.set_billCat(category);
        bill.set_billSubCat(subCategory);
        bill.set_billUserId(prefValue);

        dataSource.createBill(bill);


        Log.v(LOGTAG, "Starting Camera app");
        startCameraActivity();


    }

    public boolean checkValidationEditText(String cat) {

        final String INPUT = "[ a-zA-Z0-9]+";
        final Pattern PATTERN = Pattern.compile(INPUT);
        boolean check = false;

        if (TextUtils.isEmpty(cat)){

            return check;
        } else if (!TextUtils.isEmpty(cat)) {
            if (PATTERN.matcher(cat).matches()) {
                return true;
            } else
                return check;
        }

        return check;
    }

    public void erasePreviousImages() {


        try {
            int noOfCrops = CroppedImageFile.count();

            for (int img = 1; img <= noOfCrops; img++) {

                _path2 = DATA_PATH_CROPPED_IMAGES + img + ".jpg";

                Uri image = Uri.fromFile(new File(_path2));
                File fdelete = new File(image.getPath());
                if (fdelete.exists()) {
                    fdelete.delete();
                }

            }

            _path = DATA_PATH + "Testocrs.jpg";

            Uri image = Uri.fromFile(new File(_path));
            File fdelete = new File(image.getPath());
            if (fdelete.exists()) {
                fdelete.delete();
            }

        } catch (Exception e) {

        }


    }




    public void startCameraActivity() {

        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);


        startActivityForResult(intent, 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.i(LOGTAG, "resultCode: " + resultCode);

        if (resultCode == -1) {

            Intent intent = new Intent(this, CropBillActivity.class);
            intent.putExtra("billID", billID);
            intent.putExtra("subCategory", subCategory);
            intent.putExtra("Category", category);
            startActivity(intent);
            finish();

        } else {
            Log.v(LOGTAG, "User cancelled");
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart_scan, menu);
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
