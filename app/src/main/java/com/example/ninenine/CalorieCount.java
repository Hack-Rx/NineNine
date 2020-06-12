package com.example.ninenine;
public class CalorieCount {
    private String foodid,fooditem,calories,caloriesfromfat,proteins,serving,brand;

    public CalorieCount() {
    }

    public CalorieCount(String foodid,String fooditem, String calories, String caloriesfromfat, String proteins, String serving, String brand) {
        this.foodid=foodid;
        this.fooditem = fooditem;
        this.calories = calories;
        this.caloriesfromfat = caloriesfromfat;
        this.proteins = proteins;
        this.serving = serving;
        this.brand = brand;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFooditem() {
        return fooditem;
    }

    public void setFooditem(String fooditem) {
        this.fooditem = fooditem;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCaloriesfromfat() {
        return caloriesfromfat;
    }

    public void setCaloriesfromfat(String caloriesfromfat) {
        this.caloriesfromfat = caloriesfromfat;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
