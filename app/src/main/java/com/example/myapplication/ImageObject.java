package com.example.myapplication;

import org.opencv.core.Mat;

public class ImageObject {
    private Mat image;
    private int imageID;

    public ImageObject(Mat i_image, int i_imageID){
        image=i_image;
        imageID=i_imageID;
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
