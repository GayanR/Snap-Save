package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import database.SnapAndSaveDataSource;


public class MainActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    SnapAndSaveDataSource dataSource;


    //LOGTAG
    private static final String LOGTAG = "SS";

    //Place to store the photos and tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        String[] paths = new String[]{DATA_PATH, DATA_PATH + "tessdata/", DATA_PATH + "croppedImages/", DATA_PATH + "reports/"};

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(LOGTAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(LOGTAG, "Created directory " + path + " on sdcard");
                }
            }

        }

        try {
            getAssetAppFolder("tessdata");
        } catch (Exception e) {
            e.printStackTrace();
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);



    }


    private void getAssetAppFolder(String dir) throws Exception{

        {
            File f = new File(DATA_PATH + dir);
            if (!f.exists() || !f.isDirectory())
                f.mkdirs();
        }
        AssetManager am = getAssets();

        String[] aplist = am.list(dir);

        for (String strf : aplist) {
            try {
                InputStream is = am.open(dir + "/" + strf);
                copyToDisk(dir, strf, is);
            } catch (Exception ex) {

                getAssetAppFolder(dir + "/" + strf);
            }
        }



    }


    public void copyToDisk(String dir,String name,InputStream is) throws IOException {
        int size;
        byte[] buffer = new byte[2048];

        FileOutputStream fout = new FileOutputStream(DATA_PATH +"/"+dir+"/" +name);
        BufferedOutputStream bufferOut = new BufferedOutputStream(fout, buffer.length);

        while ((size = is.read(buffer, 0, buffer.length)) != -1) {
            bufferOut.write(buffer, 0, size);
        }
        bufferOut.flush();
        bufferOut.close();
        is.close();
        fout.close();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
