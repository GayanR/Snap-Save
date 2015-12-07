package billfiltering;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nuwan on 8/28/2015.
 */
public class CommonAlgoFinal {

    public static String[][] common(String bill) {

        String[] splitString = bill.split("\n");

        List<String> rows = new ArrayList<String>();

        String upperBound[] = {"Ln", "Product", "PRODUCT", "Price", "PRICE", "Qty", "QTY", "Amount", "AMOUNT"};
        String lowerBound[] = {"Sub", "SUB", "Total", "TOTAL", "Net", "NET", "Gross", "Amount", "AMOUNT"};

        String[][] BillDetails;
        int billIndex = 0;
        String merchant = "";
        String time = "";
        String total = "";
        String date = "";

        if (checkMerchantName(splitString[0])) {
            splitString[0] = splitString[0].replaceAll("[.,]", "");
            splitString[0] = splitString[0].trim();
            merchant = splitString[0];
        }

        int k = 0;

        outerloop:
        for (int i = 0; i < splitString.length; i++) {

            if (date.isEmpty()) {
                date = date(splitString[i]);
            }
            if (time.isEmpty()) {
                time = time(splitString[i]);
            }
            if (total.isEmpty()) {
                total = total(splitString[i]);
            }

            for (int up = 0; up < upperBound.length; up++) {

                if (splitString[i].contains(upperBound[up])) {

                    for (int j = i + 1; j < splitString.length; j++) {

                        rows.add(k, splitString[j]);
                        System.out.println("items array :" + splitString[j]);
                        k++;

                        if (date.isEmpty()) {
                            date = date(splitString[j]);
                        }
                        if (time.isEmpty()) {
                            time = time(splitString[j]);
                        }

                    }

                } else {
                    continue;
                }
                break outerloop;
            }
//
        }
        System.out.println("\n\nEnd of first for loop\n\n");

        Collections.reverse(rows);
        List<String> productsTwoRows = new ArrayList<String>();

        outerloop:
        for (int i = 0; i < rows.size(); i++) {

            for (int lower = 0; lower < lowerBound.length; lower++) {

                if (rows.get(i).contains(lowerBound[lower])) {

                    total = null;
                    total = total(rows.get(i));

                    for (int j = i + 1; j < rows.size(); j++) {
//                        System.out.println(rows.get(j));
                        productsTwoRows.add(rows.get(j));
                    }
                    break outerloop;
                }
            }

        }
//        System.out.println("\n");
//        System.out.println("\n");

        Collections.reverse(productsTwoRows);
        String products[][] = new String[productsTwoRows.size() / 2][4];
        System.out.println(productsTwoRows.size() / 2);
        String row1, row2;
        int index = 0;
        for (int i = 0; i < productsTwoRows.size(); i = i + 2) {

//            row1 = productsTwoRows.get(i);
            row1 = formatRow1(productsTwoRows.get(i));
            products[index][0] = row1;
            System.out.println(row1);
//            row1.trim();
//            String splitter1[] = row1.split(" ");
//            Pattern checkNumber = Pattern.compile("\\d+");
//            Matcher matcher;
//            matcher = checkNumber.matcher(splitter1[0]);
//            if (matcher.find()) {
//                StringBuilder sb = new StringBuilder();
//                for (int rowId = 1; rowId < splitter1.length; rowId++) {
//                    sb.append(splitter1[rowId] + " ");
//                }
//                System.out.println(sb.toString());
//                products[index][0] = sb.toString();
//
//            } else {
//                System.out.println(row1);
//                products[index][0] = row1;
//            }

            row2 = formatRow2(productsTwoRows.get(i + 1));
//            row2 = row2.replaceAll(" [xX] ", " ");
//            row2 = row2.replaceAll("[sS]", "5");
//            row2 = row2.replaceAll("[B]", "0");
//            row2 = row2.replaceAll("[M]", "4");
//            row2 = row2.replaceAll("\\. ", "\\.");
//            row2.trim();
//            System.out.println(row2);
            System.out.println(row2);
            String splitter[] = row2.split(" ");
            products[index][1] = splitter[0];
            products[index][2] = splitter[1];
            products[index][3] = splitter[2];
            row1 = null;
            row2 = null;
            index++;

        }
        System.out.println("\n\n");

        BillDetails = new String[products.length + 4][4];
        BillDetails[billIndex][0] = merchant;
        BillDetails[billIndex + 1][0] = date;
        BillDetails[billIndex + 2][0] = time;

        //Rs.15.00 AMOUNT:49/=
        BillDetails[billIndex + 3][0] = total;
        billIndex = billIndex + 4;

        for (int i = 0; i < products.length; i++) {

            for (int j = 0; j < products[i].length; j++) {
                BillDetails[billIndex][j] = products[i][j];

            }
            billIndex++;

        }

        return BillDetails;

    }

    private static String formatRow1(String line) {

        String row1 = line;

        row1 = row1.trim();
        row1 = row1.replaceAll("[^a-zA-Z]", " ");

        Pattern row1Pattern = Pattern.compile("\\d+");
        Matcher matcher;


        String splitter[] = row1.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splitter.length; i++) {

            if (splitter[i].length() >= 3 ) {
                matcher = row1Pattern.matcher(splitter[i]);
                if(matcher.find()){
                    continue;
                }
                sb.append(splitter[i] + " ");
            }
//            System.out.println(sb.toString());
        }

        return sb.toString();

    }

    private static String formatRow2(String line) {

        String row2 = line;
        row2 = row2.trim();
        row2 = row2.replaceAll(" [xX] ", " ");

        String splitterForColumnTest[] = row2.split(" ");
        if (splitterForColumnTest.length > 4) {
            row2 = row2.substring(2);
        }
        row2 = row2.replaceAll("\\s+\\.\\s+", " "); // this might affect other fixes - so mind to change
        row2 = row2.replaceAll("\\,", "\\.");
        row2 = row2.replaceAll("[\\*]", "");
        row2 = row2.replaceAll("[sS]", "5");
        row2 = row2.replaceAll("[lI]", "1");
        row2 = row2.replaceAll("[D]", "0");
        row2 = row2.replaceAll("[Oo]", "0");
        row2 = row2.replaceAll("[B]", "0");
        row2 = row2.replaceAll("[M]", "4");
        row2 = row2.replaceAll("[A-Za-z]", "");
        row2 = row2.replaceAll("\\.+\\s+", "\\.");
        row2 = row2.replaceAll("\\s+", " ");
        row2 = row2.replaceAll("\\.\\s+", "\\.");
        row2 = row2.replaceAll("\\s+\\.", "\\.");
        row2 = row2.replaceAll("\\.+", "\\.");
        row2 = row2.replaceAll("\\.0.", "\\.00");
        row2 = row2.replaceAll("\\.[0-9]+\\.", "\\.");
        row2 = row2.replaceAll("\\:", "\\.");
        row2 = row2.trim();


        StringBuilder sb = new StringBuilder();
        double amount, price, quantity;

        String splitter[] = row2.split(" ");
        if (splitter.length == 4) {
            price = Double.parseDouble(splitter[1]);
            quantity = Double.parseDouble(splitter[2]);
            amount = Double.parseDouble(splitter[3]);

        } else {
            price = Double.parseDouble(splitter[0]);
            quantity = Double.parseDouble(splitter[1]);
            amount = Double.parseDouble(splitter[2]);
        }
        try {
            if (splitter[1].contains(".") && splitter[2].contains(".")) {
                DecimalFormat df = new DecimalFormat("#.##");

                price = Double.parseDouble(splitter[1]);
                quantity = Double.parseDouble(splitter[2]);
                amount = Double.valueOf(df.format(price * quantity));

            }
            else if (splitter[1].contains(".") && !splitter[2].contains(".")) {

                DecimalFormat df = new DecimalFormat("#.##");

                price = Double.parseDouble(splitter[1]);
                quantity = Double.parseDouble(splitter[2]);
                amount = Double.valueOf(df.format(price * quantity));

            } else if (splitter[2].contains(".") && !splitter[3].contains(".")) {

                DecimalFormat df = new DecimalFormat("#.##");

                price = Double.parseDouble(splitter[2]);
                quantity = Double.parseDouble(splitter[3]);
                amount = Double.valueOf(df.format(price * quantity));

            }
        } catch (Exception e) {

        }

        sb.append(Double.toString(price) + " " + Double.toString(quantity) + " " + Double.toString(amount));

        return sb.toString();

    }

    private static boolean checkMerchantName(String line) {
        line = line.replaceAll("\\s", "");
        //line = line.replaceAll("[.,:;_+=!?'@#$%&()<>\"]", "");
        line = line.replaceAll("[.,]", "");
        System.out.println("Line is : " + line);
        boolean check = false;

        for (int i = 0; i < line.length(); i++) {
            if (Character.isLetter(line.charAt(i))) {

                check = true;
            } else {
                check = false;
                break;
            }

        }

        return check;

    }

    private static String date(String line) {

        Pattern datePattern = Pattern.compile("(\\d{4}|\\d{2})[/-]\\d{2}[/-](\\d{4}|\\d{2})");
        Matcher matcher;

        String date = "";
        line = line.replaceAll("[sS]", "5");
        line = line.replaceAll("[lI]", "1");
        line = line.replaceAll("[D]", "0");
        line = line.replaceAll("[Oo]", "0");
        line = line.replaceAll("[B]", "0");
        line = line.replaceAll("[M]", "4");

        matcher = datePattern.matcher(line.trim());
        if (matcher.find()) {
            date = matcher.group(0);
            System.out.println("Date captured : " + date);
        }

        return date;

    }

    private static String time(String line) {

//        Pattern timePattern = Pattern.compile("\\d{2}[:-]\\d{2}(\\[:-]\\d{2})?");
        Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}(\\:\\d{2})?");
        Matcher matcher;

        String time = "";
        line = line.replaceAll("[sS]", "5");
        line = line.replaceAll("[lI]", "1");
        line = line.replaceAll("[D]", "0");
        line = line.replaceAll("[Oo]", "0");
        line = line.replaceAll("[B]", "0");
        line = line.replaceAll("[M]", "4");
        line = line.replaceAll("\\s+\\:", "\\:");
        line = line.replaceAll("1\\s+", "\\1");

        matcher = timePattern.matcher(line);
        if (matcher.find()) {

            time = matcher.group(0);
            System.out.println("Time captured : " + time);
        }

        return time;

    }

    private static String total(String line) {

        line = line.trim();
        line = line.replaceAll("\\,", "");
        Pattern totalPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher;
        String total = "";
        String result = "";

        String lowerBound[] = {"Sub", "SUB", "Total", "TOTAL", "Net", "NET", "Amount", "AMOUNT", "RS(LKR)"};

        for (int lower = 0; lower < lowerBound.length; lower++) {

            if (line.contains(lowerBound[lower])) {
                String subtotal[] = line.split(" ");
                total = subtotal[subtotal.length - 1];

                matcher = totalPattern.matcher(total);
                if (matcher.find()) {
                    result = matcher.group(0);
                    System.out.println("Total captured : " + total);
                }

                System.out.println("Total captured : " + total);
                break;
            }

        }

        return result;

    }
}
