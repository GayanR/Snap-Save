package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import database.SnapAndSaveDataSource;
import differentmethods.SharedPreference;
import model.UnwantedExp;

public class UnwantedExpQuestionActivity extends AppCompatActivity {

    private static long prefValue;
    SnapAndSaveDataSource dataSource;

    //Flag to check registered user using preferences
    public static final String REGISTERED = "registered";

    private RadioButton radioButtonQ1,radioButtonQ2,radioButtonQ3;
    private RadioGroup radioGroup1,radioGroup2,radioGroup3;
    private Button btnSubmit;

    //Strings for Questions
    String Q1,Q2,Q3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unwanted_exp_question);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        String id = SharedPreference.getDefaults(REGISTERED, this);
        prefValue = Long.parseLong(id);



    }

    public void submit(View view) {



        radioGroup1 = (RadioGroup)findViewById(R.id.radiogroup1);
        radioGroup2 = (RadioGroup)findViewById(R.id.radiogroup2);
        radioGroup3 = (RadioGroup)findViewById(R.id.radiogroup3);

        int selectedId1 = radioGroup1.getCheckedRadioButtonId();
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();
        int selectedId3 = radioGroup3.getCheckedRadioButtonId();

        radioButtonQ1 = (RadioButton)findViewById(selectedId1);
        Q1 = radioButtonQ1.getText().toString();

        radioButtonQ2 = (RadioButton)findViewById(selectedId2);
        Q2 = radioButtonQ2.getText().toString();

        radioButtonQ3 = (RadioButton)findViewById(selectedId3);
        Q3 = radioButtonQ3.getText().toString();

        enterUnwanted(Q1, Q2, Q3);

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }


    public void enterUnwanted(String q1, String q2, String q3) {

        dataSource.open();
        UnwantedExp ue = new UnwantedExp();

        ue.setId(prefValue);
        ue.set_catEntertainment(q2);
        ue.set_catFastFood(q3);
        ue.set_catWineAndBev(q1);
        dataSource.createUnwantedExp(ue);


    }

    public void skip(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_unwanted_exp_question, menu);
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
