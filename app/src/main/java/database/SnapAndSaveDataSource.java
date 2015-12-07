package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Bill;
import model.BillItem;
import model.UnwantedExp;
import model.User;

/**
 * Created by Nuwan on 5/31/2015.
 */
public class SnapAndSaveDataSource {

    public static final String LOGTAG="SS";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    public SnapAndSaveDataSource(Context context) {
        dbhelper = new SnapAndSaveDBOpenHelper(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database closed");
        dbhelper.close();
    }

    public void createUser(User user) {

        open();

        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_ID, user.getId());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_NAME, user.getName());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_EMAIL, user.getEmail());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_INCOME, user.getIncome());
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_PASSWORD, user.getPassword());

        long id = database.insert(SnapAndSaveDBOpenHelper.TABLE_USER, null, values);
        Log.i(LOGTAG, "ID is " + id);
    }

    //insert user method 2
    public void createUserTwo(long id, String name, String email, double income) {

        open();
        String sql = "INSERT INTO " + SnapAndSaveDBOpenHelper.TABLE_USER + " (" + SnapAndSaveDBOpenHelper.COLUMN_ID + ", " + SnapAndSaveDBOpenHelper.COLUMN_NAME + ", " + SnapAndSaveDBOpenHelper.COLUMN_EMAIL + ", " + SnapAndSaveDBOpenHelper.COLUMN_INCOME + ") " +
                "VALUES (" + id + ", '" + name + "', '" + email + "'," + income + ")";

        database.rawQuery(sql, null);


    }

    public void createBill(Bill bill) {
        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ID, bill.get_billId());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME, bill.get_billName());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_CATEGORY, bill.get_billCat());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_SUB_CATEGORY, bill.get_billSubCat()); //new
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE, bill.get_billDate());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TIME, bill.get_billTime());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL, bill.get_billTotal());
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_REPETITIVE, bill.get_billRepetitive());//new
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_NUMBER_OF_BILLS, bill.get_billNumber());//new
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_USER_ID, bill.get_billUserId());

        database.insert(SnapAndSaveDBOpenHelper.TABLE_BILLS, null, values);

    }

    //method used in Dynamic Product Row Generation Activity to create a bill
    public void createBillTwo(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ID, bill.get_billId());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_USER_ID, bill.get_billUserId());
        database.insert(SnapAndSaveDBOpenHelper.TABLE_BILLS, null, values);
    }

    public BillItem createBillItem(BillItem bill) {
        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME, bill.get_billItemName());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_PRICE, bill.get_billItemPrice());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_QUANTITY, bill.get_billItemQuantity());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT, bill.get_billItemAmount());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID, bill.get_billItemBillId());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_CATEGORY, bill.get_billItemCat());

        long insertid = database.insert(SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS, null, values);
        bill.set_billItemId(insertid);
        return bill;
    }

    //insert bill items -- manual bill entry
    public BillItem createBillItemTwo(BillItem bill) {
        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME, bill.get_billItemName());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT, bill.get_billItemAmount());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID, bill.get_billItemBillId());

        long insertid = database.insert(SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS, null, values);
        Log.i(LOGTAG, "createBillItemTwo " + Long.toString(insertid));

        bill.set_billItemId(insertid);
        return bill;
    }

//    public void createBillItemTwo(String name, double amount, long bid) {
//
//        open();
//        String sql = "INSERT INTO " + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " (" + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME + ", " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT + ", " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + ") " +
//                "VALUES ('" + name + "', " + amount + ")";
//
//        database.rawQuery(sql, null);
//
//
//    }

//    public void createBillRepetitive(BillRepetitive bill) {
//        ContentValues values = new ContentValues();
//
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_REPETITIVE_MON, bill.get_billMon());
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_PRICE, bill.get_billItemPrice());
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_QUANTITY, bill.get_billItemQuantity());
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT, bill.get_billItemAmount());
//        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID, bill.get_billItemBillId());
//
//        long insertid = database.insert(SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS, null, values);
//        bill.set_billItemId(insertid);
//        return bill;
//    }


    //query out the user
    public Cursor getAllUserData() {
        open();
        Cursor res = database.rawQuery("Select * from " + SnapAndSaveDBOpenHelper.TABLE_USER, null);
        return res;
    }

    //query out the bill
    public Cursor getAllBillData() {
        open();
        Cursor res = database.rawQuery("Select * from " + SnapAndSaveDBOpenHelper.TABLE_BILLS, null);
        return res;
    }

    //query out the bill items
    public Cursor getAllBillItemData() {
        open();
        Cursor res = database.rawQuery("Select * from " + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS, null);
        return res;
    }

    //query out the unwantedExp
    public Cursor getAllUnwantedExpQ() {
        open();
        Cursor res = database.rawQuery("Select * from " + SnapAndSaveDBOpenHelper.TABLE_UNWANTED_EXPENSES, null);
        return res;
    }


    //update bill name,date,time and total
    public boolean updateBill(Long id, String name, String date, String time, double total) {

        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME, name);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE, date);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TIME, time);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL, total);

        String where = SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + "=" + id;
        int result = database.update(SnapAndSaveDBOpenHelper.TABLE_BILLS, values, where, null);
        return (result == 1);
    }

    //update bill date,time and total (bills with no names)
    public boolean updateBillNoName(Long id, String date, String time, double total) {

        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE, date);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TIME, time);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL, total);

        String where = SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + "=" + id;
        int result = database.update(SnapAndSaveDBOpenHelper.TABLE_BILLS, values, where, null);
        return (result == 1);
    }

    //update bill used in Manual Bill Entry
    public boolean updateBill(Long id, String merchName, String cat, String subCat, String date, double total) {

        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME, merchName);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_CATEGORY, cat);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_SUB_CATEGORY, subCat);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE, date);
        values.put(SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL, total);

        String where = SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + "=" + id;
        int result = database.update(SnapAndSaveDBOpenHelper.TABLE_BILLS, values, where, null);
        return (result == 1);
    }


    //delete certain bill
    public boolean deleteBill(long id) {
        Log.i(LOGTAG, "Database deleteBill");
        String where = SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + "=" + id;
        int result = database.delete(SnapAndSaveDBOpenHelper.TABLE_BILLS, where, null);
        return (result == 1);
    }

    //delete certain bill two
    public void deleteBillTwo(long id) {
        Log.i(LOGTAG, "Database deleteBill two");
        database.execSQL("DELETE FROM " + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " = " + id);

    }

    //deletes bill items with specific id
    public void deleteBillItems(long id) {
        Log.i(LOGTAG, "Database deleteBillItems");
//        String where = SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + "=" + id;
//        int result = database.delete(SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS, where, null);
//        return (result == 1);
        database.execSQL("DELETE FROM " + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + " = " + id);

    }

    //check whether the bill is already there (Manual Bill Entry)
    public boolean checkBill(long id) {

        open();
        Cursor res = database.rawQuery("Select * from " + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " = " + id, null);
        if (res.getCount() != 0) {
            return true;
        }

        return false;

    }

    //deletes bill and items
    public int deleteBillAll() {
        Log.i(LOGTAG, "Database deleteBillAll");
        int count1 = database.delete(SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS, "1", null);
        int count2 = database.delete(SnapAndSaveDBOpenHelper.TABLE_BILLS, "1", null);
        return count1+count2;
    }

    //select to check whether its the same bill
    public boolean checkSameBill(String date, String time) {

        String select = "SELECT * FROM " + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " = '" + date + "' AND " + SnapAndSaveDBOpenHelper.COLUMN_BILL_TIME + " = '" + time + "';";
        Cursor res = database.rawQuery(select, null);
        if (res.getCount() != 0)
            return true;
        else
            return false;
    }
    
    
    
    
    //Mishal's query starts from here

    //for calendar
    public ArrayList<String> getDailyExpenses()
    {
        open();
        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ")," + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + "," + SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + ";", null);

        //cr.moveToFirst();
        ArrayList<String> item = new ArrayList<String>();
        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
//            String con = Double.toString(cr.getDouble(0)) + "||" + Long.toString(cr.getLong(1));
            String con2 = Double.toString(cr.getDouble(0)) + "||" + cr.getString(1) + "||" + cr.getString(2);

            item.add(con2);
            Log.i("item : ", con2);
        }

        return item;
    }

    //for pie chart
    public ArrayList<String> getExpByCat()
    {
        open();
        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ")," + SnapAndSaveDBOpenHelper.COLUMN_BILL_CATEGORY + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_CATEGORY + ";", null);

        //cr.moveToFirst();
        ArrayList<String> item = new ArrayList<String>();
        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
//            String con = Double.toString(cr.getDouble(0)) + "||" + Long.toString(cr.getLong(1));
            String con2 = Double.toString(cr.getDouble(0)) + "||" + cr.getString(1);

            item.add(con2);
            Log.i("item : ", con2);
        }

        return item;
    }

    //for unwanted expenses
    public ArrayList<String> getExpByCatMonth(String month)
    {
        open();
        Log.i(LOGTAG, month);
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        int y = cal.get(Calendar.YEAR);
        String m = "%-" + month + "-"+y+"%";
//        String m = "%-" + month + "%";
        Log.i(LOGTAG, m);
        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ")," + SnapAndSaveDBOpenHelper.COLUMN_BILL_CATEGORY + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + m + "' Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_CATEGORY + ";", null);

        ArrayList<String> item = new ArrayList<String>();
        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

            String con2 = Double.toString(cr.getDouble(0)) + "||" + cr.getString(1);

            item.add(con2);
            Log.i("item : ", con2);
        }

        return item;
    }


    public ArrayList<String> getDailyExpensesForSpecificMonth(String month)
    {
        open();
        Log.i(LOGTAG, month);
        String m = "%-" + month + "%";
        Log.i(LOGTAG, m);
        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ")," + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + m + "' Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + ";", null);

        //cr.moveToFirst();
        ArrayList<String> item = new ArrayList<String>();
        for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
            String con = Double.toString(cr.getDouble(0)) + "||" + Long.toString(cr.getLong(1));
            String con2 = Double.toString(cr.getDouble(0)) + "||" + cr.getString(1);

            item.add(con2);
            Log.i("item : ", con +"\t "+con2);
        }

        return item;
    }



    //Code Added the day befor NBQSA to work with Calendar and Stuffs
    public ArrayList<String> getCategoryExpense(){
        open();
        Cursor res =database.rawQuery("SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME + "," + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS +";", null);
        ArrayList<String> item = new ArrayList<String>();
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            String con = res.getString(0) + "||" + Double.toString(res.getDouble(1));
            item.add(con);
            Log.i("item : ", con);
        }

        return item;

    }

    //Get all Expenses from 1 day

    public ArrayList<String> getExpenseFromOneDay(){
        open();
        Cursor res = database.rawQuery("SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME + "," + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL +  "," + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + ";", null);
        ArrayList<String> item = new ArrayList<String>();
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            String con = res.getString(0) + "||" + Double.toString(res.getDouble(1));
            item.add(con);
            Log.i("Expenses and Dates ", con);

        }

        return item;

    }






    public double getTotalExpense()
    {
        open();
        Cursor tot = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM " + SnapAndSaveDBOpenHelper.TABLE_BILLS + ";", null);
        tot.moveToFirst();
        double cnt =  tot.getDouble(0);
        return cnt;

    }

    public Cursor getUniqueDays(String month)
    {
        open();
        Log.i(LOGTAG, month);
        String m = "%-" + month + "%";
        Log.i(LOGTAG, m);
        Cursor count = database.rawQuery("SELECT DISTINCT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + m + "';", null);
        return count;

    }

    //get total exp for current month
    public double getTotalOfMonth(String month) {
        open();
        Log.i(LOGTAG, month);
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        int y = cal.get(Calendar.YEAR);
        String m = "%-" + month + "-"+y+"%";
//        String m = "%-" + month + "%";
        Log.i(LOGTAG, m);
        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + m + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        Log.i(LOGTAG, "Expense : " + Double.toString(cnt));
        return cnt;
    }

    public double getTotalJan()
    {
        open();
        String jan ="2015-01%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }

    public double getTotalFeb()
    {
        open();
        String jan ="2015-02%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalMar()
    {
        open();
        String jan ="2015-03%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalApr()
    {
        open();
        String jan ="2015-04%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalMay()
    {
        open();
        String jan ="%05/2015%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalJun()
    {
        open();
        String jan ="2015-06%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalJul()
    {
        open();
        String jan ="2015-07%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalAug()
    {
        open();
        String jan ="2015-08%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalSep()
    {
        open();
        String jan ="2015-09%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalOct()
    {
        open();
        String jan ="2015-10%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalNov()
    {
        open();
        String jan ="2015-11%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }
    public double getTotalDec()
    {
        open();
        String jan ="2015-12%";

        Cursor cr = database.rawQuery("SELECT sum(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + jan + "';", null);
        cr.moveToFirst();
        double cnt =  cr.getDouble(0);
        return cnt;
    }


    //Yasas's sql queries

    public ArrayList<String> getCheapestItemNames()
    {
        open();
        Cursor res = database.rawQuery("Select " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME + " , " + " Min ( " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT + "/" + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_QUANTITY + ") , " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + " from " + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " Group by " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME, null);
        ArrayList<String> item = new ArrayList<String>();
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            String con = res.getString(0) + "||" + Long.toString(res.getLong(2));
            item.add(con);
            Log.i("item : ", con);
        }

        return item;

    }

    public String getLocationOfCheapestItem(long bid)
    {
        open();

        Cursor res = database.rawQuery(" select " + SnapAndSaveDBOpenHelper.COLUMN_BILL_NAME + " from " + SnapAndSaveDBOpenHelper.TABLE_BILLS + " where " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " = " + bid, null);

        res.moveToFirst();
        return res.getString(0);

    }

    // For Prediction
    public double[] getAllTotalMonthsExpense(){
        open();
        Cursor res = database.rawQuery("SELECT SUM (" + SnapAndSaveDBOpenHelper.COLUMN_BILL_TOTAL + ")," + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " Group By substr(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + ",4,2),substr(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + ",7,4);", null);
        double[] item = new double[res.getCount()];
        int i = 0;
        for (res.moveToFirst(); !res.isAfterLast(); res.moveToNext()) {
            item[i] = res.getDouble(0);
            Log.i(LOGTAG, "Total for Month : " + Double.toString(res.getDouble(0)));
            i++;
        }

        return item;

    }

    //query out the user's income -- For Prediction
    public double getUserIncome() {
        open();
        Log.i(LOGTAG, "Database get income");
        Cursor res = database.rawQuery("Select " + SnapAndSaveDBOpenHelper.COLUMN_INCOME + " From " + SnapAndSaveDBOpenHelper.TABLE_USER, null);
        res.moveToFirst();
        Log.i(LOGTAG, "Income is : " + Double.toString(res.getDouble(0)));
        return res.getDouble(0);

    }



    //query for unwanted expenses
    public void createUnwantedExp(UnwantedExp ue) {

        open();

        ContentValues values = new ContentValues();

        values.put(SnapAndSaveDBOpenHelper.COLUMN_UNWANTED_EXP_USER_ID, ue.getId());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_UNWANTED_EXP_ENTERTAINMENT, ue.get_catEntertainment());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_UNWANTED_EXP_FASTFOOD, ue.get_catFastFood());
        values.put(SnapAndSaveDBOpenHelper.COLUMN_UNWANTED_EXP_WINE_AND_BEVERAGES, ue.get_catWineAndBev());

        long id = database.insert(SnapAndSaveDBOpenHelper.TABLE_UNWANTED_EXPENSES, null, values);
        Log.i(LOGTAG, "ID is " + id);
    }

    //query to get user's email account
    public String getUserEmail() {
        open();
        Log.i(LOGTAG, "Database get email");
        Cursor res = database.rawQuery("Select " + SnapAndSaveDBOpenHelper.COLUMN_EMAIL + " From " + SnapAndSaveDBOpenHelper.TABLE_USER, null);
        res.moveToFirst();
        Log.i(LOGTAG, "Email is : " + res.getString(0));
        return res.getString(0);

    }

    //query to get user's username
    public String getUsername() {
        open();
        Log.i(LOGTAG, "Database get name");
        Cursor res = database.rawQuery("Select " + SnapAndSaveDBOpenHelper.COLUMN_NAME + " From " + SnapAndSaveDBOpenHelper.TABLE_USER, null);
        res.moveToFirst();
        Log.i(LOGTAG, "Name is : " + res.getString(0));
        return res.getString(0);

    }


    //query to update users email account
    public boolean updateUserEmail(String email) {
        open();
        ContentValues values = new ContentValues();
        values.put(SnapAndSaveDBOpenHelper.COLUMN_EMAIL, email);
        int result = database.update(SnapAndSaveDBOpenHelper.TABLE_USER, values, null, null);
        return (result == 1);
    }

    //query to update users income
    public boolean updateUserIncome(double income) {
        open();
        ContentValues values = new ContentValues();
        values.put(SnapAndSaveDBOpenHelper.COLUMN_INCOME, income);
        int result = database.update(SnapAndSaveDBOpenHelper.TABLE_USER, values, null, null);
        return (result == 1);
    }

    //query for pie chart -> categorize

    public ArrayList<String[]> getBillCategoriesForMonth(String month) {

        open();
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        int y = cal.get(Calendar.YEAR);
        String m = "%-" + month + "-" + y + "%";
//        String m = "%-" + month + "%";
        Cursor cr = database.rawQuery("SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_CATEGORY + ",SUM(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + " IN (SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + m + "')" + " Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_CATEGORY + ";", null);


        ArrayList<String[]> item = new ArrayList<String[]>();
        Log.i("item : ", Integer.toString(cr.getCount()));
        if (cr.getCount() != 0) {
            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
                String[] item2 = new String[2];
                item2[0] = cr.getString(0);
                item2[1] = cr.getString(1);
                item.add(item2);
                Log.i("item : ", "Category : " + item2[0] + " Amount : " + item2[1]);


            }
        }
        return item;
    }

    public ArrayList<String> getItemCategoryForMonth(String month) {

        open();
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        int y = cal.get(Calendar.YEAR);
        String m = "%-" + month + "-" + y + "%";
//        String m = "%-" + month + "%";
        Cursor cr = database.rawQuery("SELECT DISTINCT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_CATEGORY + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + " IN " +
                "(SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " FROM " + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " +
                SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE '" + m + "');", null);


        ArrayList<String> item = new ArrayList<String>();
        if (cr.getCount() != 0) {
            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {

                String con2 = cr.getString(0);

                item.add(con2);
                Log.i("item : ", con2);
            }
        }
        return item;
    }

    public ArrayList<String[]> getItemCategoriesForMonth(String month, String category) {

        open();
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        int y = cal.get(Calendar.YEAR);
        String m = "%-" + month + "-" + y + "%";

//        String m = "%-" + month + "%";
        Cursor cr = database.rawQuery("SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME + ",SUM(" + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_AMOUNT + ") FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + " IN (SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE " + "'" + m + "')" + "AND " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_CATEGORY + "='" + category + "' Group By " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_NAME + ";", null);


        ArrayList<String[]> item = new ArrayList<String[]>();
        Log.i("item : ", Integer.toString(cr.getCount()));
        if (cr.getCount() != 0) {
            for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
                String[] item2 = new String[2];
                item2[0] = cr.getString(0);
                item2[1] = cr.getString(1);
                item.add(item2);
                Log.i("item : ", "Category : " + item2[0] + " Amount : " + item2[1]);


            }
        }
        return item;
    }

    //query out the bill items bill item cat
    public int checkBillItemCat(String month) {
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        int y = cal.get(Calendar.YEAR);
        String m = "%-" + month + "-"+y+"%";
//        String m = "%-" + month + "%";
        open();
        Cursor res = database.rawQuery("SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_CATEGORY + " FROM "
                + SnapAndSaveDBOpenHelper.TABLE_BILL_ITEMS + " WHERE " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ITEM_BILL_ID + " IN " +
                "(SELECT " + SnapAndSaveDBOpenHelper.COLUMN_BILL_ID + " FROM " + SnapAndSaveDBOpenHelper.TABLE_BILLS + " WHERE " +
                SnapAndSaveDBOpenHelper.COLUMN_BILL_DATE + " LIKE '" + m + "');", null);
        return res.getCount();
    }



}
