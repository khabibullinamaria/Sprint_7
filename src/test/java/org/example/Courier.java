package org.example;

public class Courier extends Login {
    private String firstName;

    public Courier(String login, String password, String firstName) {
        super(login,password);
        this.firstName = firstName;
    }

    public Courier() {
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
