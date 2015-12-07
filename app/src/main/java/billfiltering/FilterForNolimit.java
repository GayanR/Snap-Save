/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billfiltering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nuwan
 */
public class FilterForNolimit {

    public static String[][] nolimit(String bill) {

        String[] splitString = bill.split("\n");
        List<String> rows = new ArrayList<String>();

        String[][] BillDetails;
        int billIndex = 0;
        String total ="";
        String date="";

        int k = 0;
        Pattern pattern1 = Pattern.compile("\\d{2}/\\d{2}/\\d{2}");
        Matcher matcher;
        for (int i = 0; i < splitString.length; i++) {

            if (splitString[i].contains("AMOUNT")) {

                for (int j = i + 1; j < splitString.length; j++) {
                    rows.add(k, splitString[j]);
                    //System.out.println("items array :" + splitString[j]);
                    k++;

                    if (splitString[j].contains("TOTAL")||splitString[j].contains("SUB")) {
                        String subtotal[] = splitString[j].split(" ");
                        total = subtotal[subtotal.length - 1];
                        System.out.println("Total captured : " + total);

                    }
                    matcher = pattern1.matcher(splitString[j]);
                    if (matcher.find()){
                        date = matcher.group(0);
                    }
                }
                break;
            } else {
                continue;
            }

        }
        System.out.println("\n\nEnd of first for loop\n\n");

        Collections.reverse(rows);
        List<String> productsTwoRows = new ArrayList<String>();

        for (int i = 0; i < rows.size(); i++) {

            if (rows.get(i).contains("TOTAL")||rows.get(i).contains("SUB")) {

                for (int j = i + 1; j < rows.size(); j++) {

                    productsTwoRows.add(rows.get(j));
                }
            }

        }

        Collections.reverse(productsTwoRows);
        String products[][] = new String[productsTwoRows.size() / 2][4];
        System.out.println(productsTwoRows.size() / 2);
        String row1, row2;
        int index = 0;
        for (int i = 0; i < productsTwoRows.size(); i = i + 2) {
            row1 = productsTwoRows.get(i);
            row2 = productsTwoRows.get(i + 1);
            System.out.println(row1);
            System.out.println(row2);
            String splitter[] = row2.split(" ");
            products[index][0] = row1;
            products[index][1] = splitter[1];
            products[index][2] = splitter[2];
            products[index][3] = splitter[3];
            row1 = null;
            row2 = null;
            index++;

        }
        System.out.println("\n\n");

        BillDetails = new String[products.length + 2][4];
        BillDetails[billIndex][0] = total;
        BillDetails[billIndex+1][0] = date;
        billIndex = billIndex + 2;


        for (int i = 0; i < products.length; i++) {

            for (int j = 0; j < products[i].length; j++) {
                BillDetails[billIndex][j] = products[i][j];

            }
            billIndex++;

        }

        
        return BillDetails;

    }
}
