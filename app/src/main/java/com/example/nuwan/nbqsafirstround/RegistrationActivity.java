package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import differentmethods.RandomNumber;
import differentmethods.SharedPreference;
import model.User;


public class RegistrationActivity extends AppCompatActivity {

    SnapAndSaveDataSource dataSource;

    //logtag
    private static final String LOGTAG = "SS";

    //Flag to check registered user using preferences
    private static final String REGISTERED = "registered";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String INCOME = "income";


    private static long prefValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        String id = SharedPreference.getDefaults(REGISTERED, this);
        if (!TextUtils.isEmpty(id)) {
            finish();
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        } else if (TextUtils.isEmpty(id)) {
            setContentView(R.layout.activity_registration);
            dataSource = new SnapAndSaveDataSource(this);
            dataSource.open();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }


    public void saveDetails(View v) {

        dataSource.open();

        EditText editText = (EditText) findViewById(R.id.name);
        String name = editText.getText().toString();

        EditText editText2 = (EditText) findViewById(R.id.email);
        String email = editText2.getText().toString();

        EditText editText3 = (EditText) findViewById(R.id.income);
        String income = editText3.getText().toString();


        boolean checkEmail = checkValidation(name, email, income);
        if (checkEmail == true) {



            long uid = RandomNumber.generateRandomNumber();
            String id = Long.toString(uid);

            SharedPreference.setDefaults(REGISTERED, id, this);
            SharedPreference.setDefaults(NAME, name, this);
            SharedPreference.setDefaults(EMAIL, email, this);
            SharedPreference.setDefaults(INCOME, income, this);

            double salary = Double.parseDouble(income);

            Log.i(LOGTAG, "Get");

            User user = new User();
            user.setId(uid);
            user.setName(name);
            user.setEmail(email);
            user.setIncome(salary);

            Log.i(LOGTAG, "User Input Done");

            dataSource.open();
            dataSource.createUser(user);
//            dataSource.createUserTwo(uid, name, email, salary);

//            CharSequence text = "You have registered successfully" + user.getId();
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(this, text, duration);
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show();

            Log.i(LOGTAG, "Send");
//            Intent intent = new Intent(this, MainMenuActivity.class);
            Intent intent = new Intent(this, UnwantedExpQuestionActivity.class);
            startActivity(intent);
            finish();

        } else if (checkEmail == false) {
            SweetAlertDialog pDialog = new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Failed");
            pDialog.setContentText("Please fill the fields correctly!");
            pDialog.setConfirmText("Okay");
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.setCancelable(true);
            pDialog.show();

        }


    }

    public final static boolean checkValidation(String name, CharSequence email, String income) {

//        final String INPUT = "[ a-zA-Z0-9_-]+";
        final String INCOME = "[0-9]+(\\.[0-9]+)?";
        final String INPUT = "[ a-zA-Z0-9]+";
        final Pattern PATTERN = Pattern.compile(INPUT);
        final Pattern PATTERN2 = Pattern.compile(INCOME);
        boolean check = false;

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(income)) {

            return check;
        } else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(income)) {

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && PATTERN.matcher(name).matches() && PATTERN2.matcher(income).matches()) {

                return true;
            } else {
            }
            return check;
        }

        return check;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
