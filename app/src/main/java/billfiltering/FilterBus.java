/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billfiltering;

import android.util.Log;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nuwan
 */
public class FilterBus {

    public static String[] filter(String bill_lines) {


        String s[] = bill_lines.split("\n");
        String date = null;
        String time = null;
        String total = null;
        String error = "noerror";
        String result = "";
        String results[] = new String[4];

        Pattern pattern1 = Pattern.compile("\\d{2}-\\d{2}-\\d{2}");
        Pattern pattern2 = Pattern.compile("Rs.+\\d{2}.\\d{2}");
        Pattern pattern3 = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
        Pattern pattern4 = Pattern.compile("RS.+\\d{2}.\\d{2}");

        Matcher matcher;
        for (String text : s) {

            if (text.contains("O")) {

                String newRec = text.replaceAll("O", "0");

                matcher = pattern1.matcher(newRec);
                if (matcher.find()) {
                    date = matcher.group(0);
                }

                matcher = pattern3.matcher(newRec);
                if (matcher.find()) {
                    time = matcher.group(0);
                }

            }


            matcher = pattern2.matcher(text);
            if (matcher.find()) {
                total = matcher.group(0);
                total = total.replaceAll("Rs.", "");
            }
            matcher = pattern4.matcher(text);
            if (matcher.find()) {
                total = matcher.group(0);
                total = total.replaceAll("RS.", "");
            }

            matcher = pattern1.matcher(text);
            if (matcher.find()) {
                date = matcher.group(0);
            }

            matcher = pattern3.matcher(text);
            if (matcher.find()) {
                time = matcher.group(0);
            }
        }

        try {
            if (!date.equals(null) && !time.equals(null) && !total.equals(null)) {
                System.out.println("success");
                results[0] = error;
                results[1] = date;
                results[2] = time;
                results[3] = total;

                System.out.println(Arrays.deepToString(results));

            }
        } catch (Exception e) {
            error = error.replaceAll("noerror", "error");
            System.out.println("failed");
            results[0] = error;
            results[1] = date;
            results[2] = time;
            results[3] = total;

        }



        return results;


    }

}
