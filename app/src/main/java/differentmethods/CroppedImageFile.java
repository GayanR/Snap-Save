package differentmethods;

import android.os.Environment;

import java.io.File;

/**
 * Created by Nuwan on 8/9/2015.
 */
public class CroppedImageFile {

    //Place to store the photos and tess data
    public static final String DATA_PATH_CROPPED_IMAGES = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/croppedImages";


    public static int count() {

        File file=new File(DATA_PATH_CROPPED_IMAGES);
        File[] list = file.listFiles();
        int count = 0;
        for (File f: list){
            String name = f.getName();
            if (name.endsWith(".jpg"))
                count++;

        }

        return count;
    }


}
