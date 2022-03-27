package com.example.loginappintercorp.model;

public class Client {

    private String name;
    private String lastName;
    private int age;
    private String dateBirth;

    public Client(String name, String lastName, int age, String dateBirth) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.dateBirth = dateBirth;
    }

    public Client() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }
}
