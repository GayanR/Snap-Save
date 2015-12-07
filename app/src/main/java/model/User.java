package model;

/**
 * Created by Nuwan on 5/31/2015.
 */
public class User {

    private long id;
    private String name;
    private String email;
    private double income;

//    private String password;


    public User() {

    }

    public User(long id, String name, String email, double income) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.income = income;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
