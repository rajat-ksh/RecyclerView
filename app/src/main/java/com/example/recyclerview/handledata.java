package com.example.recyclerview;

import androidx.annotation.Nullable;

public class handledata {
    private String mImageURL;
    private String mName;
    private String mAge;
    private String mAddress;
    private String mGender;
    private String mLat;
    private String mLong;


    public handledata(String ImageURL,String Name,String Age,String add,String gender,String lat,String Long){
        mImageURL=ImageURL;
        mName=Name;
        mAddress=add;
        mAge=Age;
        mGender=gender;
        mLat=lat;
        mLong=Long;

    }

    public String getImageURL(){
        return mImageURL;
    }

    public String getAddress(){
        return mAddress;
    }

    public String getNamme(){
        return mName;
    }

    public  String getmGender(){return mGender;}

    public String getmAge(){
        return mAge;
    }

    public  String getmLat(){return mLat;}

    public  String getmLong(){return mLong;}

}
