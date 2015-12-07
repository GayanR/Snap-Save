package model;

/**
 * Created by Nuwan on 6/4/2015.
 */
public class Bill {


    private long _billId;
    private String _billName;
    private String _billCat;
    private String _billSubCat; //new
    private String _billDate;
    private String _billTime;
    private double _billTotal;
//    private int _billRepetitive;
//    private int _billNumber;
    private long _billUserId;

    public Bill() {
    }

    public Bill(long _billId, String _billName, String _billCat, String _billSubCat, String _billDate, String _billTime, double _billTotal, long _billUserId) {
        this._billId = _billId;
        this._billName = _billName;
        this._billCat = _billCat;
        this._billSubCat = _billSubCat;
        this._billDate = _billDate;
        this._billTime = _billTime;
        this._billTotal = _billTotal;
        this._billUserId = _billUserId;
    }

    public double get_billTotal() {
        return _billTotal;
    }

    public void set_billTotal(double _billTotal) {
        this._billTotal = _billTotal;
    }

    public String get_billCat() {
        return _billCat;
    }

    public void set_billCat(String _billCat) {
        this._billCat = _billCat;
    }

    public String get_billDate() {
        return _billDate;
    }

    public void set_billDate(String _billDate) {
        this._billDate = _billDate;
    }

    public String get_billTime() {
        return _billTime;
    }

    public void set_billTime(String _billTime) {
        this._billTime = _billTime;
    }

    public long get_billId() {
        return _billId;
    }

    public void set_billId(long _billId) {
        this._billId = _billId;
    }

    public String get_billName() {
        return _billName;
    }

    public void set_billName(String _billName) {
        this._billName = _billName;
    }

    public long get_billUserId() {
        return _billUserId;
    }

    public void set_billUserId(long _billUserId) {
        this._billUserId = _billUserId;
    }

    public String get_billSubCat() {
        return _billSubCat;
    }

    public void set_billSubCat(String _billSubCat) {
        this._billSubCat = _billSubCat;
    }
}
