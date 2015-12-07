package differentmethods;

import java.util.Random;

/**
 * Created by Nuwan on 6/5/2015.
 */
public class RandomNumber {

    public static int generateRandomNumber() {
        Random r = new Random();
        int Low = 1000;
        int High = 1000000000;
        int R = r.nextInt(High-Low) + Low;
        return R;
    }
}
