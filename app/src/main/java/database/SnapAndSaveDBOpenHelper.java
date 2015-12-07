package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nuwan on 5/31/2015.
 */
public class SnapAndSaveDBOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "SS";

    private static final String DATABASE_NAME = "snapandsave.db";
    private static final int DATABASE_VERSION = 1;

    //user
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "userId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_INCOME = "income";



    //bill table
    public static final String TABLE_BILLS = "bill";
    public static final String COLUMN_BILL_ID = "billId";
    public static final String COLUMN_BILL_NAME = "billName";
    public static final String COLUMN_BILL_CATEGORY = "billCat";
    public static final String COLUMN_BILL_SUB_CATEGORY = "billSubCat"; //new
    public static final String COLUMN_BILL_DATE = "billDate";
    public static final String COLUMN_BILL_TIME = "billTime";
    public static final String COLUMN_BILL_TOTAL = "billTotal";
    public static final String COLUMN_BILL_USER_ID = "billUserId";

    //bill item table
    public static final String TABLE_BILL_ITEMS = "billItem";
    public static final String COLUMN_BILL_ITEM_ID = "billItemId";
    public static final String COLUMN_BILL_ITEM_NAME = "billItemName";
    public static final String COLUMN_BILL_ITEM_PRICE = "billItemPrice";
    public static final String COLUMN_BILL_ITEM_QUANTITY = "billItemQuantity";
    public static final String COLUMN_BILL_ITEM_AMOUNT = "billItemAmount";
    public static final String COLUMN_BILL_ITEM_BILL_ID = "billItemBillId";
    public static final String COLUMN_BILL_ITEM_CATEGORY = "billItemCategory";

    //unwanted expenses table
    public static final String TABLE_UNWANTED_EXPENSES = "unwanted_expenses";
    public static final String COLUMN_UNWANTED_EXP_ID = "expId";
    public static final String COLUMN_UNWANTED_EXP_USER_ID = "userId";
    public static final String COLUMN_UNWANTED_EXP_WINE_AND_BEVERAGES = "wineAndBeveages";
    public static final String COLUMN_UNWANTED_EXP_ENTERTAINMENT = "entertainment";
    public static final String COLUMN_UNWANTED_EXP_FASTFOOD = "fastfood";


    private static final String TABLE_CREATE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_INCOME + " REAL " +
                    ")";


    private static final String TABLE_CREATE_BILL =
            "CREATE TABLE " + TABLE_BILLS + " (" +
                    COLUMN_BILL_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_BILL_NAME + " TEXT, " +
                    COLUMN_BILL_CATEGORY + " TEXT, " +
                    COLUMN_BILL_SUB_CATEGORY + " TEXT," +
                    COLUMN_BILL_DATE + " TEXT, " +
                    COLUMN_BILL_TIME + " TEXT, " +
                    COLUMN_BILL_TOTAL + " REAL, " +
                    COLUMN_BILL_USER_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_BILL_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";


    private static final String TABLE_CREATE_BILL_ITEM =
            "CREATE TABLE " + TABLE_BILL_ITEMS + " (" +
                    COLUMN_BILL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BILL_ITEM_NAME + " TEXT, " +
                    COLUMN_BILL_ITEM_PRICE + " REAL, " +
                    COLUMN_BILL_ITEM_QUANTITY + " REAL DEFAULT 1, " +
                    COLUMN_BILL_ITEM_AMOUNT + " REAL, " +
                    COLUMN_BILL_ITEM_BILL_ID + " INTEGER, " +
                    COLUMN_BILL_ITEM_CATEGORY + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_BILL_ITEM_BILL_ID + ") REFERENCES " + TABLE_BILLS + "(" + COLUMN_BILL_ID + ") ON DELETE CASCADE" +
                    ")";


    private static final String TABLE_CREATE_UNWANTED_EXPENSES =
            "CREATE TABLE " + TABLE_UNWANTED_EXPENSES + " (" +
                    COLUMN_UNWANTED_EXP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_UNWANTED_EXP_USER_ID + " INTEGER, " +
                    COLUMN_UNWANTED_EXP_WINE_AND_BEVERAGES + " TEXT, " +
                    COLUMN_UNWANTED_EXP_ENTERTAINMENT + " TEXT, " +
                    COLUMN_UNWANTED_EXP_FASTFOOD + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_UNWANTED_EXP_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";



    public SnapAndSaveDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");

        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onOpen(db);

        db.execSQL(TABLE_CREATE_USER);
        Log.i(LOGTAG, "User Table has been created");
        db.execSQL(TABLE_CREATE_UNWANTED_EXPENSES);
        Log.i(LOGTAG, "Unwanted Expenses Table has been created");
        db.execSQL(TABLE_CREATE_BILL);
        Log.i(LOGTAG, "Bill Table has been created");
        db.execSQL(TABLE_CREATE_BILL_ITEM);
        Log.i(LOGTAG, "Bill Item Table has been created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNWANTED_EXPENSES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);

            Log.i(LOGTAG, "Database has been upgraded from " +
                    oldVersion + " to " + newVersion);
//        }
    }
}
