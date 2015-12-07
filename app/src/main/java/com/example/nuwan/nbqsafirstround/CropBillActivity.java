package com.example.nuwan.nbqsafirstround;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.views.ProgressBarDeterminate;
import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.File;


import billfiltering.FilterAlgo;
import billprocessing.BusBillImageProcess;
import billprocessing.KeelsBillImageProcess;
import billprocessing.NolimitBillImageProcess;
import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import differentmethods.CroppedImageFile;


public class CropBillActivity extends AppCompatActivity {

    //LOGTAG
    private static final String LOGTAG = "SS";

    //Place to store the photos and tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";

    //Place to store the photos and tess data
    public static final String DATA_PATH_CROPPED_IMAGES = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/croppedImages/";


    //flag to check shared prefences -- app tour guide
    private static final String APPTOUR4 = "apptour4";

    SnapAndSaveDataSource dataSource;

    protected String _path = DATA_PATH + "Testocrs.jpg";
    protected String _path2;
    protected String _path3;


    final int PIC_CROP = 1;
    final int PIC_TAKE = 2;

    ImageView cropImageView;
    ProgressBarCircularIndeterminate progressBarCircularIndeterminate;
    static long billID;
    static String subCategory;
    static String Category;

    ShowcaseView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_bill_two);

        Intent intent = getIntent();
        billID = intent.getLongExtra("billID", 0);
        Log.i(LOGTAG, "Bill ID : "+Long.toString(billID));
        subCategory = intent.getStringExtra("subCategory");
        Category = intent.getStringExtra("Category");

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();

        cropImageView = (ImageView) findViewById(R.id.image_crop);

        progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);

        Uri img = Uri.fromFile(new File(_path.toString()));
        performCrop(img);



        SharedPreferences settings = getSharedPreferences(APPTOUR4, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            initAppTour();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

    }

    private void initAppTour() {
        ViewTarget target = new ViewTarget(R.id.btnDirectSmartScan, this);
        sv = new ShowcaseView.Builder(this, true)
                .setTarget(target)
                .setContentTitle(R.string.CropBillActivity_showcase_main_title)
                .setContentText(R.string.CropBillActivity_showcase_main_message)
                .setStyle(R.style.CustomShowcaseTheme2)
                .hideOnTouchOutside()
                .build();
        sv.setShowcaseY(sv.getShowcaseY() - 500);
        sv.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }


    private void performCrop(Uri picUri) {
        try {

            // Initialize intent
            Intent intent = new Intent("com.android.camera.action.CROP");
            // set data type to be sent
            intent.setDataAndType(picUri, "image/*");

            intent.putExtra("crop", true);
            intent.putExtra("scale", true);


            int count = CroppedImageFile.count();
            Log.i(LOGTAG, "Count cropped : " + Integer.toString(count));
            if (count == 0) {
                _path2 = DATA_PATH_CROPPED_IMAGES + "1.jpg";
            }
            else{
                count++;
                String value = Integer.toString(count);
                Log.i(LOGTAG, "Count cropped : " + Integer.toString(count));
                _path2 = DATA_PATH_CROPPED_IMAGES + value + ".jpg";
            }



            Uri img = Uri.fromFile(new File(_path2.toString()));

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, img);
            startActivityForResult(intent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PIC_CROP) {
            if (data != null) {

                Uri img = Uri.fromFile(new File(_path));
                File fdelete = new File(img.getPath());
                fdelete.delete();

                Bitmap cropBitmap;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inPreferQualityOverSpeed=true;

                cropBitmap = BitmapFactory.decodeFile(_path2, options);
                cropImageView.setVisibility(View.VISIBLE);
                cropImageView.setImageBitmap(cropBitmap);

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Take multiple photos ?")
                        .setContentText("If its a long bill, taking multiple\n" +
                                "photos would ensure accuracy!")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                startCameraActivity();
                            }
                        })
                        .showCancelButton(true)
                        .show();


            }
        }

        if (requestCode == PIC_TAKE) {

            Uri img = Uri.fromFile(new File(_path.toString()));
            performCrop(img);
        }

    }

    public void cancel(View view) {

        deleteBillInfo();

        Intent intent = new Intent(getApplicationContext(), SmartScanActivity.class);
        startActivity(intent);
        finish();

    }

    private void deleteBillInfo() {

        dataSource.open();
        dataSource.deleteBill(billID);

        CharSequence text = "Bill Deleted";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    Handler handlerImagePoc = new Handler(){
        @Override
        public void handleMessage(Message msg) {

           finish();


        }
    };

    public void imageProcess(View view) {

        progressBarCircularIndeterminate.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.INVISIBLE);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                synchronized (this) {
                    try {


                        Intent intent = new Intent(getApplicationContext(), ProcessedImageActivity.class);
                        intent.putExtra("billID", billID);
                        intent.putExtra("subCategory", subCategory);
                        startActivity(intent);

                    } catch (Exception e) {

                    }
                }
                handlerImagePoc.sendEmptyMessage(0);
            }
        };


        Thread imageProcThread = new Thread(r);
        imageProcThread.start();


    }


    Intent intent;
    String results[][];
    String output;
    boolean check;
    Handler handlerScan = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            output = (String) msg.obj;

            if (Category == "Grocery") {

                FilterAlgo fa = new FilterAlgo();
                Log.v(LOGTAG, "filter algo");
//            check = true;


                try {
                    results = fa.filter(output);
                } catch (Exception e) {
                }

                if (results == null || !results[0][0].equalsIgnoreCase("No Error")) {

                    check = false;
                } else {
                    intent.putExtra("Merchant", results[1]);
                    intent.putExtra("date", results[2]);
                    intent.putExtra("time", results[3]);
                    intent.putExtra("total", results[4]);
                    intent.putExtra("item", results[5]);
                    intent.putExtra("quantity", results[6]);
                    intent.putExtra("subtotal", results[7]);
                }
            }

            intent = new Intent(getApplicationContext(), CommonFilteredResultActivity.class);


            intent.putExtra("billID", billID);
            intent.putExtra("result", output);
            intent.putExtra("checkAlgo", check);
            intent.putExtra("subCategory", subCategory);
            intent.putExtra("Category", Category);
            Log.i(LOGTAG, "Crop Result : " + output);
            Log.i(LOGTAG, "Bill ID : " + Long.toString(billID));
            Log.i(LOGTAG, "Sub Cat : "+subCategory);
            startActivity(intent);
            finish();


        }
    };

    public void smartScan(View view) {

        progressBarCircularIndeterminate.setVisibility(View.VISIBLE);
        cropImageView.setVisibility(View.INVISIBLE);

        Runnable r = new Runnable() {
            String output = "";
            String[][] result;
            @Override
            public void run() {


                    synchronized (this) {
                        try {

                            int noOfCrops = CroppedImageFile.count();
                            Log.i(LOGTAG, "No of images : " + noOfCrops);

                            for (int img = 1; img <= noOfCrops; img++) {

                                _path3 = DATA_PATH_CROPPED_IMAGES + img + ".jpg";
                                Log.i(LOGTAG, "Image name : " + _path3);


//                                result = KeelsBillImageProcess.keelsImageOcrForFiltering(_path3);
                                switch (subCategory) {

                                    case "Bus":
                                        result = BusBillImageProcess.busImageOcrForFiltering(_path3);
                                        break;
                                    case "Nolimit":
                                        result = NolimitBillImageProcess.noLimitImageOcrForFiltering(_path3);
                                        break;
                                    case "Reload":
                                        result = BusBillImageProcess.busImageOcrForFiltering(_path3);
                                        break;
                                    case "Keels":
                                        result = KeelsBillImageProcess.keelsImageOcrForFiltering(_path3);
                                        break;
                                    default:
                                        result = KeelsBillImageProcess.keelsImageOcrForFiltering(_path3);
                                        break;
                                }


                                Uri image = Uri.fromFile(new File(_path3));
                                File fdelete = new File(image.getPath());
                                fdelete.delete();



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
                                    Log.i(LOGTAG, "Output :"+output);
                                    output = output + "\n";
                                    k++;

                                }

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

    public void startCameraActivity() {

        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);


        startActivityForResult(intent, PIC_TAKE);

    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crop_bill, menu);
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
