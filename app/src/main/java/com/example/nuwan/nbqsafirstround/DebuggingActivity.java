package com.example.nuwan.nbqsafirstround;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import database.SnapAndSaveDataSource;
import differentmethods.SharedPreference;
import model.User;
import prediction.PredictionMish;
import unwantedexp.UnwantedExp;


public class DebuggingActivity extends AppCompatActivity {

    SnapAndSaveDataSource dataSource;

    //Flag to check registered user using preferences
    private static final String REGISTERED = "registered";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String INCOME = "income";

    //logtag
    private static final String LOGTAG = "SS";


    //Place to store the photos and tess data
    public static final String DATA_PATH_REPORTS = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/reports/";

    //Place to store the photos and tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugging);

        dataSource = new SnapAndSaveDataSource(this);
        dataSource.open();
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

    public void viewUser(View view) {
        Log.i(LOGTAG, "viewUser 1");

        Cursor res = dataSource.getAllUserData();

        if (res.getCount() == 0) {
            Log.i(LOGTAG, "viewUser 2");
            Long id = Long.parseLong(SharedPreference.getDefaults(REGISTERED, this));
            String name = SharedPreference.getDefaults(NAME, this);
            String email = SharedPreference.getDefaults(EMAIL, this);
            String income = SharedPreference.getDefaults(INCOME, this);

            double salary = Double.parseDouble(income);

            dataSource.open();

            User user = new User(id, name, email, salary);

//            dataSource.createUserTwo(id, name, email, salary);
            dataSource.createUser(user);

        }
        else {
            StringBuffer buffer = new StringBuffer("User ID\t\t\t\tName\t\t\t\tEmail\t\t\tIncome\n\n");
            Log.i(LOGTAG, "viewUser 3");
            while (res.moveToNext()) {
                buffer.append(
                        res.getString(0) + "\t\t\t" +
                                res.getString(1) + "\t\t\t\t" +
                                res.getString(2) + "\t\t\t" +
                                res.getString(3) + "\n\n");
            }
            Log.i(LOGTAG, "viewUser 4");
            showMessage("User Details ", buffer.toString());
        }

    }

    public void viewBill(View view) {

        Log.i(LOGTAG, "view bill");
        Cursor res = dataSource.getAllBillData();

        if (res.getCount() == 0) {
            showMessage("Error", "No Bill");

        }
        else {
            StringBuffer buffer = new StringBuffer("Bill ID\t\t\t\tName\t\t\tSub Category\t\t\t\t\t\tDate\t\t\t\t\tTime\t\t\tTotal\t\t\tUser ID\n\n");
            while (res.moveToNext()) {
                buffer.append(
                        res.getString(0) + "\t\t" +
                                res.getString(1) + "\t\t" +
                                res.getString(3) + "\t\t" +
                                res.getString(4) + "\t\t" +
                                res.getString(5) + "\t\t" +
                                res.getString(6) + "\t\t" +
                                res.getString(7) + "\n\n");
            }

            showMessage("Bill Details ", buffer.toString());
        }

    }

    public void viewBillItem(View view) {

        Log.i(LOGTAG, "view bill items");
        Cursor res = dataSource.getAllBillItemData();
        Log.i(LOGTAG, "view bill items after Cursor");

        if (res.getCount() == 0) {
            showMessage("Error", "No Items Inserted");

        }
        else {
            DecimalFormat price_amount_df = new DecimalFormat("#.##");
            DecimalFormat qty_df = new DecimalFormat("0.###");
            StringBuffer buffer = new StringBuffer("ID\t\tPrice\t\tQuantity\t\tAmount\t\t\t\tBill ID\t\t\tName\n\n");
            while (res.moveToNext()) {
                buffer.append(
                        res.getString(0) + "\t\t" +
                                price_amount_df.format(Double.parseDouble(res.getString(2))) + "\t\t\t" +
                                qty_df.format(Double.parseDouble(res.getString(3))) + "\t\t\t" +
                                price_amount_df.format(Double.parseDouble(res.getString(4))) + "\t\t\t\t" +
                                res.getString(5) + "\t\t" +
                                res.getString(1) + "\n\n");

            }


            showMessage("Bill Items\n\n", buffer.toString());
        }

    }

    public void viewBillItemTwo(View view) {

        Log.i(LOGTAG, "view bill items");
        Cursor res = dataSource.getAllBillItemData();
        Log.i(LOGTAG, "view bill items after Cursor");

        if (res.getCount() == 0) {
            showMessage("Error", "No Items Inserted");

        }
        else {
            StringBuffer buffer = new StringBuffer("ID\t\tName\t\tAmount\n\n");
            while (res.moveToNext()) {
                buffer.append(
                        res.getString(0) + "\t\t" +
                                res.getString(1) + "\t\t" +
                                res.getString(2) + "\t\t" +
                                res.getString(3) + "\t\t" +
                                res.getString(4) + "\t\t" +
                                res.getString(5) + "\t\t" +
                                res.getString(6) + "\n\n");


            }


            showMessage("Bill Items\n\n", buffer.toString());
        }

    }




    public void showMessage(String Title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        alertDialog.getWindow().setLayout(width, height);
    }

    public void deleteBill(View view) {
        dataSource.open();
        int count = dataSource.deleteBillAll();
        if (count > 0) {
            CharSequence text = "All Bill Information Deleted";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

    public void predict(View view) {

        PredictionMish p = new PredictionMish();
        p.prediction(getApplicationContext());
    }


    public void generateMonthReport(View view) {
        try{
            File pdfFolder = new File(DATA_PATH, "/reports");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i(LOGTAG, "Pdf Directory created");
            }

            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            File myFile = new File(DATA_PATH_REPORTS + timeStamp + ".pdf");

            OutputStream output = null;

            output = new FileOutputStream(myFile);


            //Step 1
            Document document = new Document();

            //Step 2
            PdfWriter.getInstance(document, output);


            //Step 3
            document.open();

            //Step 4 Add content


            Calendar cal= Calendar.getInstance();
            Date date1=cal.getTime();
            int m=cal.get(Calendar.MONTH);
            int y=cal.get(Calendar.YEAR);
            DateFormatSymbols dfs=new DateFormatSymbols();
            String[] months=dfs.getMonths();
            String month=months[m];
            SimpleDateFormat sdf=new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd");

            // document.add(new Paragraph("Date: " + sdf.format(date1)));
            Paragraph topic=new Paragraph(month+" Month Expense Report "+Integer.toString(y),new Font(Font.FontFamily.HELVETICA,30.0f,Font.BOLD));
            topic.setAlignment(Paragraph.ALIGN_CENTER);
            //topic.setFont(new Font(Font.FontFamily.COURIER,20.0f,Font.BOLD));

            document.add(topic);
            Cursor res = dataSource.getAllUserData();



            if (res.getCount() != 0) {
                Log.i(LOGTAG, "Get user details for report");
                Long id = Long.parseLong(SharedPreference.getDefaults(REGISTERED, this));
                String name = SharedPreference.getDefaults(NAME, this);
                String salary = SharedPreference.getDefaults(INCOME, this);

                document.add(new Paragraph("\nName : "+name));
                document.add(new Paragraph("Income : "+salary));


            }
            document.add(new Paragraph("Date: " + sdf.format(date1)+"\n\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100f);
            table.setWidths(new int[]{2, 2});
//            PdfPCell cell;
//            cell = new PdfPCell(new Phrase("Table 1"));
//            cell.setColspan(2);
//            table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
//            cell.setRowspan(2);
//            table.addCell(cell);
            table.addCell("Category");
//            table.addCell("Expense of Last Month");
            table.addCell("Expense of this Month");
//            table.addCell("Difference of Months");

            ArrayList<String> CatTotal;
            CatTotal = dataSource.getExpByCatMonth(getMonth());
            double total = dataSource.getTotalOfMonth(getMonth());

            for (String catTot : CatTotal) {

                String[] splitStr;
                splitStr = catTot.split("\\|\\|");
                table.addCell(splitStr[1]);
                table.addCell(splitStr[0]);

            }


            table.addCell("Total");
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
                double[] arrUE = ue.calUEForAlcohol(DebuggingActivity.this);

                DecimalFormat df = new DecimalFormat("#.##");
                for (int i = 0; i < arrUE.length; i++) {
                    sum = sum + arrUE[i];
                }
                sum = Double.valueOf(df.format(sum));
            } catch (Exception e) {

            }

            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Your next month's prediction as at " + sdf.format(date1) + " is : " + Double.toString(value)));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Your sum of unwanted expenses as at " + sdf.format(date1) + " is : " + Double.toString(sum)));
            //Step 5: Close the document
            document.close();

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
            startActivity(intent);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    private String getMonth() {

        Calendar cal = Calendar.getInstance(Locale.US);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(cal.getTime());
        String[] splitStr;
        splitStr = formattedDate.split("\\-");

        Log.i(LOGTAG, "Month : " + splitStr[1]);

        return  splitStr[1];


    }


    public void viewUnwantedExpensesQuestions(View view){
        Intent i;
        i = new Intent(this, UnwantedExpQuestionActivity.class);
        startActivity(i);
    }

    public void testUEDB(View view) {

        Log.i(LOGTAG, "view bill");
        Cursor res = dataSource.getAllUnwantedExpQ();

        if (res.getCount() == 0) {
            showMessage("Error", "No Bill");

        }
        else {
            StringBuffer buffer = new StringBuffer("UE ID\t\t\t\tCus ID\t\t\tWine\t\t\t\t\t\tEnt\t\t\t\t\tFF\n\n");
            while (res.moveToNext()) {
                buffer.append(
                        res.getInt(0) + "\t\t" +
                                res.getString(1) + "\t\t" +
                                res.getString(2) + "\t\t" +
                                res.getString(3) + "\t\t" +
                                res.getString(4) + "\n\n");
            }

            showMessage("UE Details ", buffer.toString());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debugging, menu);
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
