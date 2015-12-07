package billprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nuwan on 8/24/2015.
 */
public class KeelsBillImageProcess {

    //LOGTAG
    private static final String LOGTAG = "SS";




    public static final String lang = "eng";

    //Place to store the tess data
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SnapAndSave/";




    public static String[][] keelsImageOcrForFiltering(String path) {

        if (!OpenCVLoader.initDebug()) {
            Log.i(LOGTAG, "OPENCV CRASHED");

        }


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPreferQualityOverSpeed = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        bitmap.setDensity(2);

        Mat large = new Mat();

        Utils.bitmapToMat(bitmap, large);

        Log.i(LOGTAG, "imageProcessing 1");


        //final commented just in case
        Mat small = new Mat();
        Imgproc.cvtColor(large, small, Imgproc.COLOR_BGR2GRAY);

        Log.i(LOGTAG, "imageProcessing 2");

        Mat grad = new Mat();
        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(600, 4));//500, 4
        //changed -- large / small
        Imgproc.morphologyEx(small, grad, Imgproc.MORPH_GRADIENT, morphKernel);

        Log.i(LOGTAG, "imageProcessing 3");

        Mat bw = new Mat();

        Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU); //0.0

        Log.i(LOGTAG, "imageProcessing 4");

        Mat connected = new Mat();
        morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(600, 4)); //9,1
        Imgproc.morphologyEx(bw, connected, Imgproc.MORPH_CLOSE, morphKernel);


        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new MatOfInt4();
        Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));


        List<Mat> subregions = new ArrayList<Mat>();


        int indx = 0;
        for (int i = 0; i < contours.size(); i++) {

            Rect roi = Imgproc.boundingRect(contours.get(i)); //nuwan




//35 //8
            if (roi.area() > 200 && roi.height > 45 && roi.width > 8) {


                Mat contourRegion = new Mat();

                contourRegion = large.submat(roi);





                subregions.add(indx, contourRegion);
                indx++;
            } else {
                continue;
            }


        }


        Collections.reverse(subregions);
        Log.v(LOGTAG, "Subregion List size " + subregions.size());
        String[][] ocrResultArray = new String[indx][12] ;
        int subIndx = 0;

        for (Mat subs : subregions) {

            Mat subLarge = subs.clone();


            Mat subSmall = new Mat();
            Imgproc.cvtColor(subLarge, subSmall, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(subSmall, subSmall, new Size(3, 3), 0); //nuwan


            Log.i(LOGTAG, "imageProcessing 2");

            Mat subGrad = new Mat();
            Mat subMorphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(12, 15));//3,3
            Imgproc.morphologyEx(subSmall, subGrad, Imgproc.MORPH_GRADIENT, subMorphKernel);

            Log.i(LOGTAG, "imageProcessing 3");

            Mat subBw = new Mat();
            Imgproc.threshold(subGrad, subBw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

            Log.i(LOGTAG, "imageProcessing 4");

            Mat subConnected = new Mat();
            morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 15)); //9,1
            Imgproc.morphologyEx(subBw, subConnected, Imgproc.MORPH_CLOSE, morphKernel);


            List<MatOfPoint> subContours = new ArrayList<MatOfPoint>();
            Mat subHierarchy = new MatOfInt4();
            Imgproc.findContours(subConnected, subContours, subHierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));



            for (int j = 0; j < subContours.size(); j++) {

                Rect subRoi = Imgproc.boundingRect(subContours.get(j));


                Mat subContourRegion = new Mat();

                subContourRegion = subLarge.submat(subRoi);



                //final commented just in case
                Imgproc.cvtColor(subContourRegion, subContourRegion, Imgproc.COLOR_BGR2GRAY);


//                Imgproc.adaptiveThreshold(subContourRegion, subContourRegion, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 14);//15 15
//                Mat kernel  = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(1, 1));//3,3
//                Imgproc.morphologyEx(subContourRegion, subContourRegion, Imgproc.MORPH_CLOSE, kernel);
//                Imgproc.GaussianBlur(subContourRegion, subContourRegion, new Size(9, 9), 0);
//                Imgproc.adaptiveThreshold(subContourRegion, subContourRegion, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 75, 3);
//                Core.addWeighted(subContourRegion, 1.5, subContourRegion, -0.5, 0, subContourRegion);

                //final commented just in case
                Imgproc.GaussianBlur(subContourRegion, subContourRegion, new Size(17, 17), 0);
                Imgproc.adaptiveThreshold(subContourRegion, subContourRegion, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 75, 2);
                Core.addWeighted(subContourRegion, 1.5, subContourRegion, -0.5, 0, subContourRegion);
//


                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap subBitmap = Bitmap.createBitmap(subContourRegion.width(), subContourRegion.height(), conf);

                Utils.matToBitmap(subContourRegion, subBitmap);

                subBitmap.setDensity(300); //200

                ocrResultArray[subIndx][j] = ocrMe(subBitmap);



            }
            subIndx++;
        }

        Log.v(LOGTAG, "ocrResultArray List size " + ocrResultArray.length);
        return ocrResultArray;


    }







    public static List<Mat> keelsImageOcrForDisplaying(String path) {
        if (!OpenCVLoader.initDebug()) {
            Log.i(LOGTAG, "OPENCV CRASHED");

        }


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPreferQualityOverSpeed = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        bitmap.setDensity(2);

        Mat large = new Mat();
        Mat rgb = new Mat();
        Utils.bitmapToMat(bitmap, large);

        Log.i(LOGTAG, "imageProcessing 1");


        Mat small = new Mat();
        Imgproc.cvtColor(large, small, Imgproc.COLOR_BGR2GRAY);

        Log.i(LOGTAG, "imageProcessing 2");

        Mat grad = new Mat();
        Mat morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(12, 3));//3,3
        Imgproc.morphologyEx(small, grad, Imgproc.MORPH_GRADIENT, morphKernel);

        Log.i(LOGTAG, "imageProcessing 3");

        Mat bw = new Mat();

        Imgproc.threshold(grad, bw, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU); //0.0

        Log.i(LOGTAG, "imageProcessing 4");

        Mat connected = new Mat();
        morphKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 3)); //9,1
        Imgproc.morphologyEx(bw, connected, Imgproc.MORPH_CLOSE, morphKernel);


        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new MatOfInt4();
        Imgproc.findContours(connected, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));


        List<Mat> subregions = new ArrayList<Mat>();


        int indx = 0;
        for (int i = 0; i < contours.size(); i++) {

            Rect roi = Imgproc.boundingRect(contours.get(i));





            if (roi.area() > 200 && roi.height > 35 && roi.width > 8) {


                Mat contourRegion = new Mat();

                contourRegion = large.submat(roi);

                Imgproc.cvtColor(contourRegion, contourRegion, Imgproc.COLOR_BGR2GRAY);
                Imgproc.adaptiveThreshold(contourRegion, contourRegion, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 14);//15 15
                Mat kernel  = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(1, 1));//3,3
                Imgproc.morphologyEx(contourRegion, contourRegion, Imgproc.MORPH_CLOSE, kernel);
                Imgproc.GaussianBlur(contourRegion, contourRegion, new Size(9, 9), 0);
                Imgproc.adaptiveThreshold(contourRegion, contourRegion, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 75, 3);
                Core.addWeighted(contourRegion, 1.5, contourRegion, -0.5, 0, contourRegion);


                subregions.add(indx, contourRegion);
                indx++;
            } else {
                continue;
            }
        }

        return subregions;
    }



    public static String ocrMe(Bitmap bitmap) {

        Log.v(LOGTAG, "after bitmap processing ");

        Log.v(LOGTAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();

        Log.v(LOGTAG, "Before setDebug");
        baseApi.setDebug(true);
        //baseApi.init(DATA_PATH, lang, TessBaseAPI.OEM_TESSERACT_ONLY);
        Log.v(LOGTAG, "Before init");
//        baseApi.init(DATA_PATH, lang, TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
//        baseApi.init(DATA_PATH , lang, TessBaseAPI.OEM_DEFAULT);
        baseApi.init(DATA_PATH, lang, TessBaseAPI.OEM_TESSERACT_ONLY);
//        baseApi.init(DATA_PATH, lang, TessBaseAPI.OEM_DEFAULT);

        //baseApi.init(DATA_PATH, lang);
        Log.v(LOGTAG, "Before setVariable");
//        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "./:;#()*=,-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
//        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_COLUMN);//nuwan
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_WORD);//nuwan
        //baseApi.init(DATA_PATH, lang, TessBaseAPI.OEM_CUBE_ONLY);
        //baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
//        baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "_-+=!?‘’;'@#$%&«“()<>\"");
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "_-+=!?‘’;'@#$%&«“<>\"(){}");
        //baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        Log.v(LOGTAG, "Before setImage");
        baseApi.setImage(bitmap);
        Log.v(LOGTAG, "Before getUTF8Text");
        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();
        Log.v(LOGTAG, "After baseApi");


//        Log.v(LOGTAG, "OCRED TEXT: " + recognizedText);

//        if ( lang.equalsIgnoreCase("eng") ) {
        //recognizedText = recognizedText.replaceAll("\n", "%");
        //recognizedText =recognizedText.replaceAll("\\n", "\n");
//            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9./#()*-:;,\n]+", " ");
//            recognizedText = recognizedText.replaceAll(" \\. ", " ");
//            recognizedText = recognizedText.replaceAll("\\. ", " ");
//            recognizedText = recognizedText.replaceAll(" \\, ", " ");
        //recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
//            Log.v(LOGTAG, recognizedText);

//        }

//        if (recognizedText.length() < 2) {
//            if (recognizedText.contains("l")||recognizedText.contains("I")) {
//                String newRec = recognizedText.replaceAll("[lI]", "1");
//                Log.v(LOGTAG, "NEW OCRED TEXT: " + newRec);
//                return newRec;
//
//            }
//
//        }





        Log.v(LOGTAG, "OCRED TEXT: " + recognizedText);
        return recognizedText;


    }

}


