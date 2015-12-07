package redundant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nuwan on 10/29/2015.
 */
public class Duplicate {

    public static String[][] removeSame(String[][] bill) {

        List<String> store = new ArrayList<String>();

        store.add(bill[0][0]);
        store.add(bill[1][0]);
        store.add(bill[2][0]);
        store.add(bill[3][0]);

        int index = 4;
        boolean check = false;
        for (int x = 4; x < bill.length; x++) {

            for (int y = x + 1; y < bill.length; y++) {

                int dis = distance(bill[x][0].toLowerCase(), bill[y][0].toLowerCase());

                if (dis < 3) {
                    check = true;
                }

            }
            if (check == false) {
                store.add(bill[index][0] + "||" + bill[index][1] + "||" + bill[index][2] + "||" + bill[index][3]);

            }
            check = false;
            index++;
        }

        String[][] resultArr = new String[store.size()][4];

        for (int j = 0; j < store.size(); j++) {

            if (j < 4) {
                resultArr[j][0] = store.get(j);
            } else {
                String arr[] = store.get(j).split("\\|\\|");
                resultArr[j][0] = arr[0];
                resultArr[j][1] = arr[1];
                resultArr[j][2] = arr[2];
                resultArr[j][3] = arr[3];
            }
        }

        return resultArr;
    }

    private static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public static boolean checkRedudant(String[][] BillDetails) {

        if (BillDetails.length == 4) {
            return true;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        double extractedTotal = Double.valueOf(df.format(Double.parseDouble(BillDetails[3][0])));
        System.out.println("EXTRACTED : " + extractedTotal);

        double sumOfProducts = 0;

        for (int i = 4; i < BillDetails.length; i++) {

            sumOfProducts = sumOfProducts + Double.parseDouble(BillDetails[i][3]);
        }

        sumOfProducts = Double.valueOf(df.format(sumOfProducts));
        System.out.println("SUM : " + sumOfProducts);

        if ((sumOfProducts <= (extractedTotal + 1)) && (sumOfProducts >= (extractedTotal - 1))) {
            return true;
        }

        return false;
    }

}
