package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;

public class SettingsActivity extends AppCompatActivity {

    SnapAndSaveDataSource dataSource;

    private static final String EMAIL = "email";
    private static final String INCOME = "income";

    //logtag
    private static final String LOGTAG = "SS";

    TextView emailAddTextView;
    TextView incomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        String email =  dataSource.getUserEmail();
        double income = dataSource.getUserIncome();

        emailAddTextView = (TextView)findViewById(R.id.emailAddressEditTextView);
        incomeTextView = (TextView)findViewById(R.id.incomeEditTextView);

        emailAddTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddTextView.setText("");
            }
        });

        incomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeTextView.setText("");
            }
        });

        emailAddTextView.setText(email);
        incomeTextView.setText(Double.toString(income));
    }

    public  void update (View v)
    {

        boolean checkEmail = checkValidation(emailAddTextView.getText().toString(), incomeTextView.getText().toString());
        if (checkEmail == true) {

            dataSource.open();

            String newEmail = emailAddTextView.getText().toString();
            String newIncome = incomeTextView.getText().toString();
            dataSource.updateUserEmail(newEmail);
            dataSource.updateUserIncome(Double.parseDouble(newIncome));

            SweetAlertDialog pDialog = new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Successful");
            pDialog.setContentText("Fields updated successfully!");
            pDialog.setConfirmText("Okay");
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.setCancelable(true);
            pDialog.show();


        }else if (checkEmail == false) {

            SweetAlertDialog pDialog = new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.ERROR_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Failed");
            pDialog.setContentText("Please fill the fields correctly!");
            pDialog.setConfirmText("Okay");
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.setCancelable(true);
            pDialog.show();


        }

    }
    public  void cancel(View v){

        Intent intent = new Intent(this, SelectStatisticsEntryMethod.class);
        startActivity(intent);
        finish();
    }

    public final static boolean checkValidation(CharSequence email, String income) {

        final String INCOME = "[0-9]+(\\.[0-9]+)?";
        final Pattern PATTERN2 = Pattern.compile(INCOME);
        boolean check = false;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(income)) {

            return check;
        } else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(income)) {

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && PATTERN2.matcher(income).matches()) {

                return true;
            } else {

            }
            return check;
        }

        return check;
    }

}
