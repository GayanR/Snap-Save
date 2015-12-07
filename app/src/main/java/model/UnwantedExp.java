package model;

/**
 * Created by Nuwan on 9/3/2015.
 */
public class UnwantedExp {


    private long id;
    private String _catEntertainment;
    private String _catFastFood;
    private String _catWineAndBev;

    public UnwantedExp() {

    }

    public UnwantedExp(long id, String _catEntertainment, String _catFastFood, String _catWineAndBev) {
        this.id = id;
        this._catEntertainment = _catEntertainment;
        this._catFastFood = _catFastFood;
        this._catWineAndBev = _catWineAndBev;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String get_catEntertainment() {
        return _catEntertainment;
    }

    public void set_catEntertainment(String _catEntertainment) {
        this._catEntertainment = _catEntertainment;
    }

    public String get_catFastFood() {
        return _catFastFood;
    }

    public void set_catFastFood(String _catFastFood) {
        this._catFastFood = _catFastFood;
    }

    public String get_catWineAndBev() {
        return _catWineAndBev;
    }

    public void set_catWineAndBev(String _catWineAndBev) {
        this._catWineAndBev = _catWineAndBev;
    }
}
