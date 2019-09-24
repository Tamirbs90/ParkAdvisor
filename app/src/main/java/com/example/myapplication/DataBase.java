package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    private static String DbName= "app.db";
    private static String table1Name= "IMAGES_TABLE";
    private static String signNameField= "SIGN_NAME";
    private static String idField= "ID";
    private static  String cityField= "CITY";
    private static  String countryField= "COUNTRY";
    private static String blobField= "ARR";
    private static  String table2Name= "USERS_TABLE";
    private static String userNameField= "USERNAME";
    private static String passwordField= "PASSWORD";
    private static String firstNameField= "FIRST_NAME";
    private static String lastNameField= "LAST_NAME";
    private static String vehicleTypeField= "VEHICLE_TYPE";

    public DataBase(Context context) {
        super(context,DbName,null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+table1Name+
                " ("+idField+" INTEGER PRIMARY KEY, " +
                ""+signNameField+ " VARCHAR(255), "+
                ""+ cityField+ " VARCHAR(255), "+
                ""+ countryField+ " VARCHAR(255), "+
                ""+blobField+ " BLOB);");

        db.execSQL("create table "+table2Name+
                " ("+userNameField+" VARCHAR(255) PRIMARY KEY, " +
                ""+passwordField+ " VARCHAR(255), "+
                ""+ firstNameField+ " VARCHAR(255), "+
                ""+lastNameField+ " VARCHAR(255)," +
                ""+vehicleTypeField+" VARCHAR(255))");
    }


    public Boolean isSignsInCountry(String country){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from " + table1Name+ " where  COUNTRY= '"+country+"'",null);
        if(cursor==null || cursor.getCount()==0)
            return false;
        return true;
    }

    public List<ImageObject> readImagesFromDB(String i_currentCity, String i_currentCountry)
    {
        List<ImageObject> imageObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        i_currentCity  = i_currentCity.replace("'","");;
        i_currentCountry = i_currentCountry.replace("'","");;
        Cursor cursor = db.rawQuery("select * from " + table1Name+ " where (CITY= '"+i_currentCity+"' or CITY='All') and COUNTRY= '"+i_currentCountry+"'",
                null);
        byte[] blob;
        ImageObject imageObject;
        int id;
        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    blob = cursor.getBlob(cursor.getColumnIndex("ARR"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                    Mat image = new Mat();
                    Utils.bitmapToMat(bitmap, image);
                    id = cursor.getInt(cursor.getColumnIndex("ID"));
                    imageObject = new ImageObject(image, id);
                    imageObjects.add(imageObject);
                    cursor.moveToNext();
                }
            }
        }

        return imageObjects;
    }

    public boolean isImagesDbEmpty(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from "+table1Name,null);
        if(cursor==null || cursor.getCount()==0)
            return true;
        return false;
    }

    public void addImageToDB(int id, Bitmap bitmap, String details, String city, String country){
        SQLiteDatabase db= this.getWritableDatabase();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Mat bitmapMat= new Mat();
        Mat resizedImageMat= new Mat();
        Utils.bitmapToMat(bitmap,bitmapMat);
        Size newSize= new Size(650,650);
        Imgproc.resize(bitmapMat,resizedImageMat,newSize);
        Bitmap resizedBitmap=Bitmap.createBitmap(resizedImageMat.width(),
                resizedImageMat.height(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resizedImageMat,resizedBitmap);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] byteArr = bos.toByteArray();
        ContentValues contentToInsert= new ContentValues();
        contentToInsert.put(idField,id);
        contentToInsert.put(signNameField,details);
        contentToInsert.put(cityField, city);
        contentToInsert.put(countryField, country);
        contentToInsert.put(blobField,byteArr);
        db.insert(table1Name,null, contentToInsert);
    }

    public void addUserToDB(String username, String password, String fname, String lname, String type){
        username = username.toLowerCase();
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentToInsert= new ContentValues();
        contentToInsert.put(userNameField,username);
        contentToInsert.put(passwordField,password);
        contentToInsert.put(firstNameField,fname);
        contentToInsert.put(lastNameField,lname);
        contentToInsert.put(vehicleTypeField,type);
        db.insert(table2Name,null,contentToInsert);
    }

    public Boolean isUserExists(String username){
        username = username.toLowerCase();
        SQLiteDatabase db= this.getReadableDatabase();
        boolean result=true;
        Cursor cursor= db.rawQuery("select * from "+table2Name+" where "+userNameField+" ='"+username+"'",null);
        if(cursor==null || cursor.getCount()==0)
            result=false;
        cursor.close();
        db.close();
        return result;
    }

    public Boolean isValidUser(String username,String password){
        username = username.toLowerCase();
        SQLiteDatabase db= this.getReadableDatabase();
        boolean result=true;
        Cursor cursor= db.rawQuery("select * from USERS_TABLE where USERNAME='"+username+"' and PASSWORD='"+password+"'", null);

        if(cursor==null || cursor.getCount()==0)
            result=false;
        else
        {
            cursor.moveToFirst();
        }

        cursor.close();
        db.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
