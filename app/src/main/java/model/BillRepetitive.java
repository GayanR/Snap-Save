package model;

/**
 * Created by Nuwan on 7/20/2015.
 */
public class BillRepetitive {

    private int _billMon;
    private int _billTue;
    private int _billWed;
    private int _billThu;
    private int _billFri;
    private int _billSat;
    private int _billSun;

    public BillRepetitive() {
    }

    public BillRepetitive(int _billMon, int _billTue, int _billWed, int _billThu, int _billFri, int _billSat, int _billSun) {
        this._billMon = _billMon;
        this._billTue = _billTue;
        this._billWed = _billWed;
        this._billThu = _billThu;
        this._billFri = _billFri;
        this._billSat = _billSat;
        this._billSun = _billSun;
    }

    public int get_billMon() {
        return _billMon;
    }

    public void set_billMon(int _billMon) {
        this._billMon = _billMon;
    }

    public int get_billTue() {
        return _billTue;
    }

    public void set_billTue(int _billTue) {
        this._billTue = _billTue;
    }

    public int get_billWed() {
        return _billWed;
    }

    public void set_billWed(int _billWed) {
        this._billWed = _billWed;
    }

    public int get_billThu() {
        return _billThu;
    }

    public void set_billThu(int _billThu) {
        this._billThu = _billThu;
    }

    public int get_billFri() {
        return _billFri;
    }

    public void set_billFri(int _billFri) {
        this._billFri = _billFri;
    }

    public int get_billSat() {
        return _billSat;
    }

    public void set_billSat(int _billSat) {
        this._billSat = _billSat;
    }

    public int get_billSun() {
        return _billSun;
    }

    public void set_billSun(int _billSun) {
        this._billSun = _billSun;
    }
}
