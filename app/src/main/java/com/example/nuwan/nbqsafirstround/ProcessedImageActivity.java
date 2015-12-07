package com.example.nuwan.nbqsafirstround;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import billprocessing.BusBillImageProcess;
import billprocessing.NolimitBillImageProcess;
import database.SnapAndSaveDataSource;


public class ProcessedImageActivity extends AppCompatActivity {

    //LOGTAG
    private static final String LOGTAG = "SS";

    SnapAndSaveDataSource dataSource;
    static long billID;
    static String subCategory;


    //Place to store the photos and tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";

    protected String _path2 = DATA_PATH + "Cropocr.jpg";

    static List<Mat> subMats = new ArrayList<Mat>();

    ScrollView sv;
    ProgressBarCircularIndeterminate progressBarCircularIndeterminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processed_image);

        Intent intent = getIntent();
        billID = intent.getLongExtra("billID", 0);
        subCategory = intent.getStringExtra("subCategory");

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        sv = (ScrollView) findViewById(R.id.scrollView);
        progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);

        onImageView();
        LinearLayout layout = (LinearLayout)findViewById(R.id.imageLayout);

        for (Mat m : subMats) {

            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap(m.width(), m.height(), conf);

            Utils.matToBitmap(m, bitmap);

            bitmap.setDensity(300);

            ImageView image = new ImageView(this);

            image.setImageBitmap(bitmap);

            layout.addView(image);
        }
    }

    private void onImageView() {

        Log.v(LOGTAG, "Display Segmented Image ");


        subMats = BusBillImageProcess.busImageOcrForDisplaying(_path2);
        Collections.reverse(subMats);
        sv.setVisibility(View.VISIBLE);

    }
    Intent intent;
    Handler handlerScan = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            String output = (String) msg.obj;

            switch (subCategory) {

                case "Bus":
                    intent= new Intent(getApplicationContext(), CommonFilteredResultActivity.class);
                    break;
                case "Nolimit":
                    intent = new Intent(getApplicationContext(), CommonFilteredResultActivity.class);
                    break;
                case "Reload":
                    intent = new Intent(getApplicationContext(), CommonFilteredResultActivity.class);
                    break;
                default:
                    break;
            }

            intent.putExtra("billID", billID);
            intent.putExtra("result", output);
            intent.putExtra("subCategory", subCategory);
            startActivity(intent);
            finish();


        }
    };

    public void startSmartScan(View view) {

        progressBarCircularIndeterminate.setVisibility(View.VISIBLE);
        sv.setVisibility(View.VISIBLE);

        Runnable r = new Runnable() {
            String output = "";
            String[][] result;
            @Override
            public void run() {


                synchronized (this) {
                    try {


                        switch (subCategory) {

                            case "Bus":
                                result = BusBillImageProcess.busImageOcrForFiltering(_path2);
                                break;
                            case "Nolimit":
                                result = NolimitBillImageProcess.noLimitImageOcrForFiltering(_path2);
                                break;
                            case "Reload":
                                result = BusBillImageProcess.busImageOcrForFiltering(_path2);
                                break;
                            default:
                                result = NolimitBillImageProcess.noLimitImageOcrForFiltering(_path2);
                                break;
                        }


                        int k = 0;
                        int length;


                        while (k < result.length) {
                            length = result[k].length - 1;
                            for (int i = length; i >= 0; i--) {

                                if (result[k][i] != null) {
                                    output = output + result[k][i] + " ";
                                } else {
                                    continue;
                                }


                            }
                            output = output + "\n";
                            k++;

                        }

                    } catch (Exception e) {

                    }
                }

                String message = output;
                Message msg = Message.obtain(); // Creates an new Message instance
                msg.obj = message; // Put the string into Message, into "obj" field.
                msg.setTarget(handlerScan); // Set the Handler
                msg.sendToTarget(); //Send the message

            }

        };

        Thread scanThread = new Thread(r);
        scanThread.start();


    }

    public void cancel(View view) {
        dataSource.open();
        dataSource.deleteBill(billID);

        CharSequence text = "Bill Deleted";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        Intent intent = new Intent(getApplicationContext(), SmartScanActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_processed_image, menu);
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
