package com.example.project.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class StorageDB extends SQLiteOpenHelper {
    private Context context;

    private static final String DB_NAME = "Food.db";
    private static final String TABLE_NAME = "Food_Cart";
    private static final int VERSION_NUM = 2;

    private static final String ID = "_id";
    private static final String DESCRIPTION = "Description";
    private static final String NUMBER_IN_CART = "NumberInCart";
    private static final String PIC = "Pic";
    private static final String TITLE = "Title";
    private static final String FEE = "Fee";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DESCRIPTION+" TEXT, "+NUMBER_IN_CART+" INT, "+PIC+" TEXT, "+TITLE+" VARCHAR(255), "+FEE+" DOUBLE ) ;";

    public StorageDB(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION_NUM);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context, "onCreate is called in SQLite", Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }catch (Exception e){
            Toast.makeText(context, "Exception: "+e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    //To upgrade in the database must change the VERSION_NUM value. Otherwise it will not change.
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            Toast.makeText(context, "onUpgrade is called in SQLite", Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(sqLiteDatabase);
        }catch (Exception e){
            Toast.makeText(context, "Exception: "+e, Toast.LENGTH_LONG).show();
        }

    }

    public long insertData(String description, int numberInCart, String pic, String title, double fee){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DESCRIPTION, description);
        contentValues.put(NUMBER_IN_CART, numberInCart);
        contentValues.put(PIC, pic);
        contentValues.put(TITLE, title);
        contentValues.put(FEE, fee);
        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return rowId;
    }

    public Cursor findOneItem(String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String quary = "SELECT * FROM "+TABLE_NAME+" where Title = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(quary, new String[] {title});
        return cursor;
    }

    public boolean updateData(String description, int numberInCart, String pic, String title, double fee){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DESCRIPTION, description);
        contentValues.put(NUMBER_IN_CART, numberInCart);
        contentValues.put(PIC, pic);
        contentValues.put(TITLE, title);
        contentValues.put(FEE, fee);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "Title = ?", new String[]{title});
        return true;
    }

    public int updateNumberInCart(String title, int numberInCart){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMBER_IN_CART, numberInCart);

        int cursor = sqLiteDatabase.update(TABLE_NAME, contentValues, "Title = ?", new String[]{title});
        return cursor;
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String quary = "SELECT * FROM "+TABLE_NAME+" ;";
        Cursor cursor = sqLiteDatabase.rawQuery(quary, null);
        return cursor;
    }

    public int deleteData(String title){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,"Title = ?", new String[] {title});
    }
}
