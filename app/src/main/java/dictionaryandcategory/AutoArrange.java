package dictionaryandcategory;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Nuwan on 10/28/2015.
 */
public class AutoArrange {


    public static String[][] autoCorrect(String[][] BillDetails) {

        Hashtable<String, String> NameCat = Dictionary.wordList();

        String[][] finalList = new String[BillDetails.length-4][2];

        int index = 0;
        for (int x = 4; x < BillDetails.length; x++) {
            boolean check = false;//check flag is initialized as false
            for (Map.Entry<String, String> entry : NameCat.entrySet()) {

                String key = entry.getKey().trim();//get item name and remove spaces
                String value = entry.getValue();//get ite category

                int dis = 999;
                try {
                    dis = distance(BillDetails[x][0].toLowerCase(), key.toLowerCase());
                } catch (Exception e) {
                    continue;
                }
                switch (dis) {
                    case 0:
                        finalList[index][0] = key;//put item name in to finalList first column
                        finalList[index][1] = value;//put item name in to finalList second column when distance is 0
                        check = true;//check flag is marked as true if it is saved in double array
                        break;
                    case 1:
                        finalList[index][0] = key;
                        finalList[index][1] = value;
                        check = true;
                        break;
                    case 2:
                        finalList[index][0] = key;
                        finalList[index][1] = value;
                        check = true;
                        break;


                }
            }
            if (check == false && BillDetails[x][0] != null) {

                finalList[index][0] = BillDetails[x][0];//put item name in to finalList first column if distance is greater than 3
                finalList[index][1] = "Other";//put "Other in to finalList first second if distance is greater than 3

            }
            index++;
            check = false;//check flag is returned to falce for the next item name to be compared

        }

        return finalList;
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


}
