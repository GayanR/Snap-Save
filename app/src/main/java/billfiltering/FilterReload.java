/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billfiltering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nuwan
 */
public class FilterReload {

    public String[] filter(String bill_lines) {

       
        String s[] = bill_lines.split("\n");// splitting the receipt into lines
        String date = null;
        String time = null;
        String total = null;
        String error = "noerror";
        String result = "";
        String results[] = new String[4];

        Pattern pattern1 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Pattern pattern2 = Pattern.compile("AMOUNT:+\\d{2}\\/\\=");
        Pattern pattern3 = Pattern.compile("\\d{2}-\\d{2}");
        Matcher matcher;
        for (String text : s) {

            matcher = pattern1.matcher(text);
            if (matcher.find()) {
                date = matcher.group(0);
            }

            matcher = pattern3.matcher(text);
            if (matcher.find()) {
                time = matcher.group(0);
                time = time.replaceAll("-",":");
            }

            matcher = pattern2.matcher(text);
            if (matcher.find()) {
                total = matcher.group(0);
                total = total.replaceAll("AMOUNT:", "");
            }
        }

        try {
            if (!date.equals(null) && !time.equals(null) && !total.equals(null)) {
                System.out.println("success");
//                System.out.println(total);
//                System.out.println(date);
//                System.out.println(time);
//                System.out.println(error);
                results[0] = error;
                results[1] = date;
                results[2] = time;
                results[3] = total;

                System.out.println(Arrays.deepToString(results));

            }
        } catch (Exception e) {
            error = error.replaceAll("noerror", "error");
            System.out.println("failed");
//            System.out.println(total);
//            System.out.println(date);
//            System.out.println(time);
//            System.out.println(error);

            results[0] = error;
            results[1] = date;
            results[2] = time;
            results[3] = total;

            System.out.println(Arrays.deepToString(results));

        }
        System.out.println("\n");


        return results;


    }

}
