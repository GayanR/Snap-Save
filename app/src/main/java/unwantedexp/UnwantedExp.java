package unwantedexp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import database.SnapAndSaveDataSource;

/**
 * Created by Nuwan on 9/3/2015.
 */
public class UnwantedExp {

    //logtag
    private static final String LOGTAG = "SS";


    SnapAndSaveDataSource dataSource;

    public double[] calUEForAlcohol(Context context) {


        dataSource = new SnapAndSaveDataSource(context);
        dataSource.open();
        ArrayList<String> CatTotal;

        double total = dataSource.getTotalOfMonth(getMonth());

        CatTotal = dataSource.getExpByCatMonth(getMonth());

        double totAlcohol = 0, totEntertainment = 0, totFastFood = 0;

        for (String catTot : CatTotal) {

            String[] splitStr;
            splitStr = catTot.split("\\|\\|");

            if (splitStr[1].equals("Alcohol and Tobacco")) {
                totAlcohol = Double.parseDouble(splitStr[0]);
            }
            else if (splitStr[1].equals("Entertainment")) {
                totEntertainment = Double.parseDouble(splitStr[0]);
            }
            else if (splitStr[1].equals("Fast Food")) {
                totFastFood = Double.parseDouble(splitStr[0]);
            } else {
                continue;
            }

        }

        double totAPer = 0, totEPer = 0, totFFPer = 0;

        Cursor cus = dataSource.getAllUnwantedExpQ();
        cus.moveToFirst();
        if (cus.getString(2).equals("Yes")) {
            totAPer = (totAlcohol / total) * 100;
        }
        if (cus.getString(3).equals("Yes")) {
            totEPer = (totEntertainment / total) * 100;
        }
        if (cus.getString(4).equals("Yes")) {
            totFFPer = (totFastFood / total) * 100;
        }

        double arr[] = new double[3];

        arr[0] = totAPer;
        arr[1] = totEPer;
        arr[2] = totFFPer;

        return arr;

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
}
