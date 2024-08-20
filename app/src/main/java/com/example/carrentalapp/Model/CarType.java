package com.example.carrentalapp.Model;

public class CarType {
    private String cartype;
    private int carCount;

    // Constructors
    public CarType() {
    }

    public CarType(String cartype, int carCount) {
        this.cartype = cartype;
        this.carCount = carCount;
    }

    // Getters and Setters
    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }
}
