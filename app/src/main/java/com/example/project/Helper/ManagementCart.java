package com.example.project.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.project.Domain.FoodDomain;
import com.example.project.Interface.ChangeNumberItemsListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    StorageDB storageDB;

    public ManagementCart(Context context) {
        this.context = context;
        //Initialize the Database
        storageDB = new StorageDB(context);
        SQLiteDatabase sqLiteDatabase = storageDB.getWritableDatabase();
    }

    //Insert Food into the database
    public void insertFood(FoodDomain item) {
        //Receive the food item and divided it into names
        String description = item.getDescription();
        int numberInCart = item.getNumberInCart();
        String pic = item.getPic();
        String title = item.getTitle();
        Double fee = item.getFee();

        //Check this insert item is in already in the database or not. Find that one and keep its value in cursor
        Cursor cursor = storageDB.findOneItem(title);

        //if cursor is 0 then its means item is not in the database
        if(cursor.getCount() == 0){
            //Insert this new data into the database
            storageDB.insertData(description, numberInCart, pic, title, fee);
            Toast.makeText(context, "Item Insert", Toast.LENGTH_LONG).show();
        }else{
            //This item is already there so just update it.
            storageDB.updateData(description, numberInCart, pic, title, fee);
            Toast.makeText(context, "Item Update", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<FoodDomain> getListCard() {
        //Get all data from the database
        Cursor cursor = storageDB.getAllData();
        ArrayList<FoodDomain> foodDomains =  new ArrayList<FoodDomain>();
        if(cursor.getCount() == 0){
            //If there is no data just simplely return empty Array
            return foodDomains;
        }else{
            while (cursor.moveToNext()){
                //Get data from the database; number 4 means title value is in the 4th column in the database
                String title = cursor.getString(4);
                String pic = cursor.getString(3);
                String description = cursor.getString(1);
                double fee = Double.parseDouble(cursor.getString(5));
                int numberInCart = Integer.parseInt(cursor.getString(2));
                //Every time make a new foodDomain Object and keep it in the foodDomains array
                FoodDomain foodDomain = new FoodDomain(title, pic, description, fee, numberInCart);
                foodDomains.add(foodDomain);
            }

            return foodDomains;

        }
    }

    public void plusNumberFood(String title, ChangeNumberItemsListener changeNumberItemsListener) {
        //Find that element that we want to update;
        Cursor cursor = storageDB.findOneItem(title);
        int number = 0;
        if(cursor.getCount() == 0){
            //If that item does not exits in the database then just return
            return;
        }else {
            //Although cursor will have only one element but for safety just use the while loop
            while (cursor.moveToNext()){
                //Here columnIndex 2 means in the 3rd column we have numberInCart values in the table.
                number = Integer.parseInt(cursor.getString(2));
            }
            //after getting the number just increase by 1
            storageDB.updateNumberInCart(title, number+1);
            //This function used to notify the recylerview adapter. So that changes of the database can also be applied in the view.
            changeNumberItemsListener.changed();
        }
    }

    public void MinusNumerFood(String title, ChangeNumberItemsListener changeNumberItemsListener) {
        //Find that element that we want to update;
        Cursor cursor = storageDB.findOneItem(title);
        int number = 0;
        if(cursor.getCount() == 0){
            //If that item does not exits in the database then just return
            return;
        }else{
            //Although cursor will have only one element but for safety just use the while loop
            while (cursor.moveToNext()){
                //Here columnIndex 2 means in the 3rd column we have numberInCart values in the table.
                number = Integer.parseInt(cursor.getString(2));
            }
            if(number == 1){
                //remove that item from the database
                storageDB.deleteData(title);
                //This function used to notify the recylerview adapter. So that changes of the database can also be applied in the view.
                changeNumberItemsListener.changed();

            }else{
                //after getting the number just decrease by 1
                storageDB.updateNumberInCart(title, number-1);
                //This function used to notify the recylerview adapter. So that changes of the database can also be applied in the view.
                changeNumberItemsListener.changed();
            }
        }
    }

    public Double getTotalFee() {
        //Get the full food list from the database
        ArrayList<FoodDomain> listFood2 = getListCard();
        double fee = 0;
        //Just calculate the fee
        for (int i = 0; i < listFood2.size(); i++) {
            fee = fee + (listFood2.get(i).getFee() * listFood2.get(i).getNumberInCart());
        }
        return fee;
    }

}
