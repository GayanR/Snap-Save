package com.example.nuwan.nbqsafirstround;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.data.general.DefaultPieDataset;
import org.afree.graphics.geom.RectShape;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import database.SnapAndSaveDataSource;
import differentmethods.CroppedImageFile;
import differentmethods.SharedPreference;
import email.GMailSender;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;
import prediction.PredictionMish;
import unwantedexp.UnwantedExp;


public class SelectStatisticsEntryMethod extends AppCompatActivity {


    ExpandableMenuOverlay menuOverlay;

    SnapAndSaveDataSource dataSource;

    //Flag to check registered user using preferences
    private static final String REGISTERED = "registered";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String INCOME = "income";

    //logtag
    private static final String LOGTAG = "SS";

    //chart details
    private float income=0,expense=0;


    //Place to store the photos and tess data
    public static final String DATA_PATH_REPORTS = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/reports/";

    //Place to store the photos and tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_statistics_entry_method);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();


        initMainMenuButtons();
    }

    public void prediction(View view) {

        try {
            PredictionMish p = new PredictionMish();
            DecimalFormat df = new DecimalFormat("#.##");
            double value = Double.valueOf(df.format(p.prediction(getApplicationContext())));

            SweetAlertDialog pDialog = new SweetAlertDialog(SelectStatisticsEntryMethod.this, SweetAlertDialog.NORMAL_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Prediction");
            pDialog.setContentText("You might spend\n Rs : " + Double.toString(value) + "\nin the next month !");
            pDialog.setConfirmText("Okay");
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.setCancelable(true);
            pDialog.show();


        } catch (Exception e) {

            SweetAlertDialog pDialog = new SweetAlertDialog(SelectStatisticsEntryMethod.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setContentText("For Prediction to work,\nYou should shop for at least 2 months!");
            pDialog.setConfirmText("Okay");
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.setCancelable(true);
            pDialog.show();

        }

    }


    public void unwantedexp(View view) {

        try {
            UnwantedExp ue = new UnwantedExp();
            double[] arrUE = ue.calUEForAlcohol(SelectStatisticsEntryMethod.this);

            DecimalFormat df = new DecimalFormat("#.##");
            double sum = 0;
            for (int i = 0; i < arrUE.length; i++) {
                sum = sum + arrUE[i];
            }
            sum = Double.valueOf(df.format(sum));

            if (sum > 0) {
                SweetAlertDialog pDialog = new SweetAlertDialog(SelectStatisticsEntryMethod.this, SweetAlertDialog.NORMAL_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Unwanted Expenses");
                pDialog.setContentText("You have wasted over\n" + Double.toString(sum) + "%\nof your total expense\nfrom this month.\nPlease spend wisely !!");
                pDialog.setConfirmText("Okay");
                pDialog.setCanceledOnTouchOutside(true);
                pDialog.setCancelable(true);
                pDialog.show();

            } else {

                SweetAlertDialog pDialog = new SweetAlertDialog(SelectStatisticsEntryMethod.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setContentText("You have not wasted any money at the moment!");
                pDialog.setConfirmText("Okay");
                pDialog.setCanceledOnTouchOutside(true);
                pDialog.setCancelable(true);
                pDialog.show();

            }




        } catch (Exception e) {

            SweetAlertDialog pDialog = new SweetAlertDialog(SelectStatisticsEntryMethod.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setContentText("You have not wasted any money at the moment!");
            pDialog.setConfirmText("Okay");
            pDialog.setCanceledOnTouchOutside(true);
            pDialog.setCancelable(true);
            pDialog.show();


        }
    }

    int mm;
    public void sendEmail(View view) {

        File dir = new File(DATA_PATH_REPORTS);
        try {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }catch (Exception e){

        }



        String[] ITEMS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance();
        mm = cal.get(Calendar.MONTH);
        Log.i(LOGTAG, "Month : "+Integer.toString(m));

        new MaterialDialog.Builder(this)
                .title("Choose Month")
                .items(ITEMS)
                .itemsCallbackSingleChoice(mm, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        Date date = new Date() ;
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

                        createReport(timeStamp, which);

                        dataSource.open();
                        String userEmail = dataSource.getUserEmail();
                        String userName = dataSource.getUsername();
                        String[] params = new String[3];
                        params[0] = userEmail;
                        params[1] = "Hello "+userName+"\n\nThis your latest report.\n\nBest Regards\nSnap 'N' Save Team";
                        params[2] = timeStamp;
                        new Email().execute(params);

                        return true;
                    }
                })
                .positiveText("Choose")
                .show();





    }

    private class Email extends AsyncTask<String,String,String>
    {
        private String email(String email, String Message, String pdfFileName) {

            try {


                GMailSender sender = new GMailSender();
                sender.sendMail("Snap 'N' Save",
                        Message,
                        "snapnsave.sliit@gmail.com",
                        email, pdfFileName);
                return "Email sent";


            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
                return "Email Failed";
            }

        }

        @Override
        protected String doInBackground(String... params) {
            return email(params[0],params[1], params[2]);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }
    int m;
    public void generateReport(View view) {

        String[] ITEMS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance();
        m = cal.get(Calendar.MONTH);
        Log.i(LOGTAG, "Month : "+Integer.toString(m));

        new MaterialDialog.Builder(this)
                .title("Choose Month")
                .items(ITEMS)
                .itemsCallbackSingleChoice(m, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        Date date = new Date();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

                        File myFile = createReport(timeStamp, which);

                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(Uri.fromFile(myFile), "application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Intent intent = Intent.createChooser(target, "Open File");

                        try {

                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                        }

                        return true;
                    }
                })
                .positiveText("Choose")
                .show();




    }

    private File createReport(String dateTime, int monthNumber) {

        File myFile = null;
        try {
            File pdfFolder = new File(DATA_PATH, "/reports");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i(LOGTAG, "Pdf Directory created");
            }


            myFile = new File(DATA_PATH_REPORTS + dateTime + ".pdf");

            OutputStream output = null;

            output = new FileOutputStream(myFile);


            //Step 1
            Document document = new Document();

            //Step 2
            PdfWriter.getInstance(document, output);


            //Step 3
            document.open();

            //Step 4 Add content

            try {
                // get input stream
                InputStream ims = getApplicationContext().getAssets().open("banner.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                document.add(image);


            } catch (IOException ex) {

            }


            Calendar cal = Calendar.getInstance();
            Date date1 = cal.getTime();
            int y = cal.get(Calendar.YEAR);
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();
            Log.i(LOGTAG, "array month: "+months.length);
            Log.i(LOGTAG, "monthNumber: "+monthNumber);
            Log.i(LOGTAG, "month: "+months[7]);
            Log.i(LOGTAG, "month: "+months[monthNumber]);
            String month = months[monthNumber];
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd");

            // document.add(new Paragraph("Date: " + sdf.format(date1)));
            Paragraph topic = new Paragraph(month + " Month Expense Report " + Integer.toString(y), new Font(Font.FontFamily.HELVETICA, 30.0f, Font.BOLD));
            topic.setAlignment(Paragraph.ALIGN_CENTER);
            //topic.setFont(new Font(Font.FontFamily.COURIER,20.0f,Font.BOLD));

            document.add(topic);
            Cursor res = dataSource.getAllUserData();


            if (res.getCount() != 0) {
                Log.i(LOGTAG, "Get user details for report");
                Long id = Long.parseLong(SharedPreference.getDefaults(REGISTERED, this));
                String name = SharedPreference.getDefaults(NAME, this);
                double salary = dataSource.getUserIncome();

                document.add(new Paragraph("\nName : " + name));
                document.add(new Paragraph("Income : " + Double.toString(salary)));


            }
            document.add(new Paragraph("Date: " + sdf.format(date1) + "\n\n"));


            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new int[]{2, 2});

            table.addCell("Category");
//            table.addCell("Expense of Last Month");
            table.addCell("Expense of this Month");
//            table.addCell("Difference of Months");

            ArrayList<String> CatTotal;
            CatTotal = dataSource.getExpByCatMonth(getMonth(monthNumber));
            double total = dataSource.getTotalOfMonth(getMonth(monthNumber));

            for (String catTot : CatTotal) {

                String[] splitStr;
                splitStr = catTot.split("\\|\\|");
                table.addCell(splitStr[1]);
                table.addCell(splitStr[0]);

            }


            table.addCell("Total (Rs)");
            table.addCell(Double.toString(total));
            document.add(table);
            document.add(new Paragraph("\n\n"));

            double value = 0;
            try {
                PredictionMish p = new PredictionMish();
                DecimalFormat df = new DecimalFormat("#.##");
                value = Double.valueOf(df.format(p.prediction(getApplicationContext())));
            } catch (Exception e) {

            }

            double sum = 0;
            try {
                UnwantedExp ue = new UnwantedExp();
                double[] arrUE = ue.calUEForAlcohol(SelectStatisticsEntryMethod.this);

                DecimalFormat df = new DecimalFormat("#.##");
                for (int i = 0; i < arrUE.length; i++) {
                    sum = sum + arrUE[i];
                }
                sum = Double.valueOf(df.format(sum));
            } catch (Exception e) {

            }

            int checkMonth = cal.get(Calendar.MONTH);
            Log.i(LOGTAG, "Month cal get : " + checkMonth);
            Log.i(LOGTAG, "Month monthNo : " + Integer.parseInt(getMonth(monthNumber)));
            if (++checkMonth == Integer.parseInt(getMonth(monthNumber))) {

                document.add(new Paragraph("\n\n"));
                document.add(new Paragraph("*Your next month's prediction as at " + getMonth(monthNumber) + "-" + Integer.toString(y) + " is : " + Double.toString(value)));
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("*Your percentage of unwanted expenses as at " + getMonth(monthNumber) + "-" + Integer.toString(y) + " is : " + Double.toString(sum) + "%"));
                document.add(new Paragraph("\n*There should be at least two months expenses to predict your expenses."));
                document.add(new Paragraph("\n*Depends on your unwanted expense selection."));
            }
            //pie chart
            Cursor inc = dataSource.getAllUserData();
            inc.moveToFirst();



            Log.i(LOGTAG, "Month : " + getMonth(monthNumber));

            double exp = dataSource.getTotalOfMonth(getMonth(monthNumber));

            income = (float) inc.getDouble(3);
            Log.i(LOGTAG, "Income : " + Float.toString(income));

            expense = (float) exp;
            Log.i(LOGTAG, "Expense : " + Float.toString(expense));

            income = income - expense;
            DefaultPieDataset piedataset = new DefaultPieDataset();


            piedataset.setValue("Expenditure "+Float.toString(expense), expense);
            piedataset.setValue("Savings " + Float.toString(inc.getFloat(3) - expense), income - expense);

            try {
                AFreeChart piechart = ChartFactory.createPieChart("Income vs Expenditure for " + month + " " + Integer.toString(y), piedataset, true, true, false);
                String filename = DATA_PATH + "piechart/piechart.jpg";

                Bitmap bitmap = Bitmap.createBitmap(525, 500, Bitmap.Config.ARGB_8888);
                RectShape rectArea = new RectShape(0.0, 0.0, 525, 500);
                Canvas canvas = new Canvas(bitmap);
                piechart.draw(canvas, rectArea);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                document.add(image);

            }
            catch (Exception e)
            {
                Log.i("SS :",e.getMessage());
            }

            int itemNoExists = dataSource.checkBillItemCat(getMonth(monthNumber));
            if(itemNoExists!=0) {
                try {
                    ArrayList<String[]> categories = dataSource.getBillCategoriesForMonth(getMonth(monthNumber));

                    DefaultPieDataset categorydataset = new DefaultPieDataset();

                    for (String[] category : categories) {

                            categorydataset.setValue(category[0] + " " + category[1], Double.parseDouble(category[1]));

                    }

                    AFreeChart piechart = ChartFactory.createPieChart("Item Category Wise Spending for " + month + " " + Integer.toString(y), categorydataset, true, true, false);
                    String filename = DATA_PATH + "piechart/piechart1.jpg";

                    Bitmap bitmap = Bitmap.createBitmap(525, 500, Bitmap.Config.ARGB_8888);
                    RectShape rectArea = new RectShape(0.0, 0.0, 525, 500);
                    Canvas canvas = new Canvas(bitmap);
                    piechart.draw(canvas, rectArea);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    document.add(image);


                } catch (Exception e) {
                    Log.i("SS :", e.getMessage());
                }
                try {
                    ArrayList<String> monthlyCategory = dataSource.getItemCategoryForMonth(getMonth(monthNumber));
                    int piechartno = 2;

                    for (String category : monthlyCategory) {


                            ArrayList<String[]> ItemList = dataSource.getItemCategoriesForMonth(getMonth(monthNumber), category);
                            DefaultPieDataset itemdataset = new DefaultPieDataset();

                            for (String[] item : ItemList) {
                                    itemdataset.setValue(item[0] + " " + item[1], Double.parseDouble(item[1]));

                            }


                            AFreeChart piechart = ChartFactory.createPieChart("Item Wise Spending of " + category + " for " + month + " " + Integer.toString(y), itemdataset, true, true, false);
                            String filename = DATA_PATH + "piechart/piechart" + Integer.toString(piechartno) + ".jpg";

                            Bitmap bitmap = Bitmap.createBitmap(525, 500, Bitmap.Config.ARGB_8888);
                            RectShape rectArea = new RectShape(0.0, 0.0, 525, 500);
                            Canvas canvas = new Canvas(bitmap);
                            piechart.draw(canvas, rectArea);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            Image image = Image.getInstance(stream.toByteArray());
                            document.add(image);




                    }
                } catch (Exception e) {
                    Log.i("SS :", e.getMessage());
                }

            }


            //Step 5: Close the document
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        return myFile;
    }

    private String getMonth(int month) {

        String normalMonth = null;
        switch (month) {
            case 0:
                normalMonth = "1";
                break;
            case 1:
                normalMonth = "2";
                break;
            case 2:
                normalMonth = "3";
                break;
            case 3:
                normalMonth = "4";
                break;
            case 4:
                normalMonth = "5";
                break;
            case 5:
                normalMonth = "6";
                break;
            case 6:
                normalMonth = "7";
                break;
            case 7:
                normalMonth = "8";
                break;
            case 8:
                normalMonth = "9";
                break;
            case 9:
                normalMonth = "10";
                break;
            case 10:
                normalMonth = "11";
                break;
            case 11:
                normalMonth = "12";
                break;

        }

        return normalMonth;

    }


    private void initMainMenuButtons() {

        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {
                switch (action) {
                    case MID:

                        Intent intentM = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(intentM);
                        // do stuff and dismiss
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case LEFT:
                        Intent intentL = new Intent(getApplicationContext(), BarChartActivity.class);
                        startActivity(intentL);
//                        finish();
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case RIGHT:
                        Intent intentR = new Intent(getApplicationContext(), PieChartActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_select_statistics_entry_method, menu);
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
