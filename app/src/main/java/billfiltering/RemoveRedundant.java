package billfiltering;

import java.util.ArrayList;

/**
 * Created by Nuwan on 9/3/2015.
 */
public class RemoveRedundant {


    public ArrayList<String> removeSame(ArrayList<String> arr) {


        ArrayList<String> resultArr = new ArrayList<String>();
        int pass;

        for (int i = 0; i < arr.size(); i++) {

            for (int j = i + 1; j < arr.size(); j++) {

                pass = distance(arr.get(i),arr.get(j));
                if (pass < 1) {
                    continue;
                } else {
                    resultArr.add(arr.get(i));
                }

            }

        }

        return resultArr;
    }


    private int distance(String a, String b) {
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
