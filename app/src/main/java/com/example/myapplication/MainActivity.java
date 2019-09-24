package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private AppLogic appLogic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug();
        appLogic = AppLogic.getInstance(getApplicationContext());
        writeImagesToDB();
        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(startIntent);
            }
        });
        Button guestBtn = (Button) findViewById(R.id.guestBtn);
        guestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), GuestActivity.class);
                startActivity(startIntent);
            }
        });
    }

    public void writeImagesToDB(){
        if(!appLogic.isImagesDbIsEmpty())
            return;

        Drawable d;
        Bitmap image;
        String details;
        for(int i=1;i<18;i++){
            if(i==1){
                details = getResources().getString(R.string.img1Details);
                d = getResources().getDrawable(R.drawable.img1,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
            }
            if(i==2){
                details = getResources().getString(R.string.img2Details);
                d = getResources().getDrawable(R.drawable.img2,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"Jerusalem","Israel");
            }
            if(i==3){
                details = getResources().getString(R.string.img3Details);
                d = getResources().getDrawable(R.drawable.img3,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
            }
            if(i==4){
                details = getResources().getString(R.string.img4Details);
                d = getResources().getDrawable(R.drawable.img4,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"Tel Aviv-Yafo","Israel");
            }
            if(i==5){
                details = getResources().getString(R.string.img5Details);
                d = getResources().getDrawable(R.drawable.img5,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
            }
            if (i == 6) {
                details = getResources().getString(R.string.img6Details);
                d = getResources().getDrawable(R.drawable.img6, this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i, image, details, "All", "Israel");
            }
            if(i==7) {
                details = getResources().getString(R.string.img7Details);
                d = getResources().getDrawable(R.drawable.img7, this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i, image, details, "All", "Israel");
            }
            if(i==8){
                details = getResources().getString(R.string.img8Details);
                d = getResources().getDrawable(R.drawable.img8,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");

            }
            if(i==9){
                details = getResources().getString(R.string.img9Details);
                d = getResources().getDrawable(R.drawable.img9,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"Tel Aviv-Yafo","Israel");
            }
            if(i==10){
                details = getResources().getString(R.string.img10Details);
                d = getResources().getDrawable(R.drawable.img10,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");

            }
            if(i==11){
                details = getResources().getString(R.string.img11Details);
                d = getResources().getDrawable(R.drawable.img11,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
                continue;

            }
            if(i==12){
                details = getResources().getString(R.string.img12Details);
                d = getResources().getDrawable(R.drawable.img12,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");

            }
            if(i==13){
                details = getResources().getString(R.string.img13Details);
                d = getResources().getDrawable(R.drawable.img13,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
            }
            if(i==14){
                details = getResources().getString(R.string.img14Details);
                d = getResources().getDrawable(R.drawable.img14,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
            }
            if(i==15){
                details = getResources().getString(R.string.img15Details);
                d = getResources().getDrawable(R.drawable.img15,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"Givatayim","Israel");
            }
            if(i==16){
                details = getResources().getString(R.string.img16Details);
                d = getResources().getDrawable(R.drawable.img16,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"Givatayim","Israel");

            }
            if(i==17){
                details = getResources().getString(R.string.img17Details);
                d = getResources().getDrawable(R.drawable.img17,this.getTheme());
                image = drawableToBitmap(d);
                appLogic.writeImageToDB(i,image,details,"All","Israel");
            }
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
