package com.example.ninenine;

import com.google.firebase.firestore.Exclude;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author devac
 * @date 09-06-2020
 */
public class FoodItems {
    private String documentId;
    private String Food_Name;
    private String Calories;
    private String Quantity;
    private Date timestamp ;


    public FoodItems(){

    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public FoodItems(String Food_Name, String Calories, String Quantity,Timestamp timestamp){
        this.Food_Name = Food_Name;
        this.Calories = Calories;
        this.Quantity = Quantity;
        this.timestamp= timestamp;
    }

    public String getCalories() {
        return Calories;
    }

    public void setCalories(String Calories) {
        this.Calories = Calories;
    }

    public void setTimestamp(Date timestamp) {
        //timestamp = new Timestamp(date.getTime());
        this.timestamp=timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setFood_Name(String Food_Name) {
        this.Food_Name = Food_Name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getFood_Name() {
        return Food_Name;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }
}

