package differentmethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nuwan on 8/3/2015.
 */
public class DateFormat {

    public static String changeDateFormat(String date) {

        String dddd = date;
        String newDate = "";
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        String[] dateFormats = {"dd/MM/yyyy", "yyyy-MM-dd", "yy-MM-dd"};

        String[] splitter = dddd.split("\\-");
        if (dddd.contains("-") && splitter[2].length() == 4) {
            return dddd;
        }

        for (String formats : dateFormats) {

            SimpleDateFormat sdf = new SimpleDateFormat(formats);
            try {
                newDate = newFormat.format(sdf.parse(dddd.trim()));

            } catch (Exception e) {

            }

        }
        return newDate;
    }

}
