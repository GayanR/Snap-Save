package model;

/**
 * Created by Nuwan on 6/3/2015.
 */
public class BillItem {

    private long _billItemId;
    private String _billItemName;
    private double _billItemPrice;
    private double _billItemQuantity;
    private double _billItemAmount;
    private long _billItemBillId;
    private String _billItemCat;

    public BillItem() {
    }


    public BillItem(double _billItemAmount, double _billItemPrice, String _billItemName, double _billItemQuantity, long _billItemBillId) {
        this._billItemAmount = _billItemAmount;
        this._billItemPrice = _billItemPrice;
        this._billItemName = _billItemName;
        this._billItemQuantity = _billItemQuantity;
        this._billItemBillId = _billItemBillId;
    }



    public double get_billItemAmount() {
        return _billItemAmount;
    }

    public void set_billItemAmount(double _billItemAmount) {
        this._billItemAmount = _billItemAmount;
    }

    public String get_billItemCat() {
        return _billItemCat;
    }

    public void set_billItemCat(String _billItemCat) {
        this._billItemCat = _billItemCat;
    }

    public long get_billItemId() {
        return _billItemId;
    }

    public void set_billItemId(long _billItemId) {
        this._billItemId = _billItemId;
    }

    public String get_billItemName() {
        return _billItemName;
    }

    public void set_billItemName(String _billItemName) {
        this._billItemName = _billItemName;
    }

    public double get_billItemPrice() {
        return _billItemPrice;
    }

    public void set_billItemPrice(double _billItemPrice) {
        this._billItemPrice = _billItemPrice;
    }

    public double get_billItemQuantity() {
        return _billItemQuantity;
    }

    public void set_billItemQuantity(double _billItemQuantity) {
        this._billItemQuantity = _billItemQuantity;
    }

    public long get_billItemBillId() {
        return _billItemBillId;
    }

    public void set_billItemBillId(long _billItemBillId) {
        this._billItemBillId = _billItemBillId;
    }
}
