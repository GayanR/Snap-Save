package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;


public class SelectBillEntryMethod extends AppCompatActivity {

    ExpandableMenuOverlay menuOverlay;

    ShowcaseView sv;

    //flag to check shared prefences -- app tour guide
    private static final String APPTOUR2 = "apptour2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bill_entry_method);
        initSelectBillEntryButtons();


        SharedPreferences settings = getSharedPreferences(APPTOUR2, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            initAppTour();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
    }

    private void initAppTour() {
        ViewTarget target = new ViewTarget(R.id.trick_textview, this);
        sv = new ShowcaseView.Builder(this, true)
                .setTarget(target)
                .setContentTitle(R.string.SelectBillEntryMethod_showcase_main_title)
                .setContentText(R.string.SelectBillEntryMethod_showcase_main_message)
                .setStyle(R.style.CustomShowcaseTheme2)
                .hideOnTouchOutside()
                .build();
    }

    private void initSelectBillEntryButtons() {

        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {
                switch (action) {
                    case MID:
                        // do stuff and dismiss
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case LEFT:
                        Intent intentL = new Intent(getApplicationContext(), SmartScanActivity.class);
                        startActivity(intentL);
//                        finish();
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case RIGHT:
                        Intent intentR = new Intent(getApplicationContext(), ManualBillEntryActivity.class);
                        startActivity(intentR);
//                        finish();
                        menuOverlay.getButtonMenu().toggle();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_bill_entry_method, menu);
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
