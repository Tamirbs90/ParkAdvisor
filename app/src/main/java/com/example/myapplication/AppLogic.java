package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class AppLogic {
    private static final int PICTURE_SIZE = 650;
    private DataBase database;
    private static AppLogic instace;
    private int matchImageId = 0;
    private VehicleType vehicleType;
    private String currentCity;
    private String currentCountry;
    private String username = "";


    private AppLogic(Context context) {
        database = new DataBase(context);
    }

    public static AppLogic getInstance(Context context) {
        if (instace == null)
            instace = new AppLogic(context);
        return instace;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public int getMatchImageId() {
        return matchImageId;
    }

    public void setMatchImageId(int matchImageId) {
        this.matchImageId = matchImageId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isUserExists(String username) {
        return database.isUserExists(username);
    }

    public boolean isUserValid(String username, String password) {
        return database.isValidUser(username, password);
    }

    public Boolean isSignsInCountryExists(String country) {
        return database.isSignsInCountry(country);
    }

    public void addUserToDB(String username, String firstname, String lastname, String password, String type) {
        database.addUserToDB(username, password, firstname, lastname, type);

    }

    public void writeImageToDB(int id, Bitmap image, String details, String city, String country) {
        database.addImageToDB(id, image, details, city, country);
    }

    public boolean isImagesDbIsEmpty() {
        return database.isImagesDbEmpty();
    }

    public List<ImageObject> readImagesFromDB() {
        return database.readImagesFromDB(currentCity, currentCountry);
    }

    public void matchUserImageToDbImages(Bitmap userImageBitmap) {
        List<ImageObject> dbImages = readImagesFromDB();
        Mat userImageMat = new Mat();
        Mat resizedImageMat= new Mat();
        int numOfGoofMatches;
        int bestNumOfGoodMatches = 15;
        MatOfKeyPoint userImageKps = new MatOfKeyPoint();
        MatOfKeyPoint userImageDes = new MatOfKeyPoint();
        ORB detector = ORB.create();
        Utils.bitmapToMat(userImageBitmap, userImageMat);
        double newHeight= PICTURE_SIZE;
        double newWidth= PICTURE_SIZE;
        Size newSize= new Size(newWidth,newHeight);
        Imgproc.resize(userImageMat,resizedImageMat,newSize);

        detector.detectAndCompute(resizedImageMat, new Mat(), userImageKps, userImageDes);
        for (ImageObject dbImage : dbImages) {
            numOfGoofMatches = compareTwoImages(detector, userImageDes, dbImage);
            if (numOfGoofMatches > bestNumOfGoodMatches) {
                matchImageId = dbImage.getImageID();
                bestNumOfGoodMatches = numOfGoofMatches;
            }
        }
    }

    public int compareTwoImages(ORB detector, MatOfKeyPoint userImageDes, ImageObject dbIamge) {

        Mat dbImageMat = dbIamge.getImage();
        MatOfKeyPoint dbImageKps = new MatOfKeyPoint();
        MatOfKeyPoint dbImageDes = new MatOfKeyPoint();
        detector.detectAndCompute(dbImageMat, new Mat(), dbImageKps, dbImageDes);
        BFMatcher matcher = BFMatcher.create(BFMatcher.BRUTEFORCE_SL2, false);
        List<MatOfDMatch> matches = new ArrayList<>();
        matcher.knnMatch(userImageDes, dbImageDes, matches, 2);
        List<DMatch> goodMatchesList = new ArrayList<>();
        float nndrRatio = 0.6f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.add(m1);
            }
        }

        return goodMatchesList.size();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}





