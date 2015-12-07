package prediction;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;

import database.SnapAndSaveDataSource;

/**
 * Created by Nuwan on 9/2/2015.
 */
public class PredictionMish {


    //logtag
    private static final String LOGTAG = "SS";



    double predResult;

    SnapAndSaveDataSource dataSource;

    public double prediction(Context context) {


        dataSource = new SnapAndSaveDataSource(context);
        dataSource.open();
        double[] y;

        y = dataSource.getAllTotalMonthsExpense();


        int noOfMonths = y.length;
        double[] x = new double[noOfMonths];
        double income = dataSource.getUserIncome();
        x[0] = income;
        Log.i(LOGTAG, "Income 0 : " + x[0]);

//        if()

        //assuming the income could fluctuate +- 5000
        for (int i = 1; i < x.length; i = i + 2) {

            x[i] = income + 5000;
            Log.i(LOGTAG, "Income 1 : " + x[i]);
            if (x.length > 2) {
                x[i + 1] = income - 5000;
                Log.i(LOGTAG, "Income 2 : " + x[i + 1]);
            }

        }

        //12-08-2015||500.0


//        double[] x = {55000, 60000, 50000};
//        double[] y = {5000, 12000, 6000};

//        System.out.println("Expected output from Excel: y = 9.4763 + 4.1939x");

        RegressionModel model = new LinearRegressionModel(x, y);
        model.compute();
        double[] coefficients = model.getCoefficients();

        Log.i(LOGTAG, "PredFore " + coefficients[0] + " " + coefficients[1]);
//        CharSequence toastmsg =  "PredFore " + coefficients[0] + " " + coefficients[1];
//        int duration = Toast.LENGTH_LONG;
//        Toast toast = Toast.makeText(context, toastmsg, duration);
//        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.show();
//        System.out.printf("Actual output from our code: y = %.4f + %.4fx", coefficients[0], coefficients[1]);


        // prediction eq  predExpense [y] = coefficient[0] + coefficent[1]*[current months income]
        predResult = coefficients[0] + coefficients[1] * x[x.length-1];
//        Log.i(LOGTAG,"PredictedOutput "+ predResult);

        Log.i(LOGTAG, "Your Prediction is : " + Double.toString(predResult));
//        CharSequence toastmsg = "Your Prediction is : " + Double.toString(predResult);
//        int duration = Toast.LENGTH_LONG;
//        Toast toast = Toast.makeText(context, toastmsg, duration);
//        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.show();

        return predResult;
    }
}
