package com.muhammadyaseen.classifiedapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Upload {
    private String imgName;
    private String imgUrl;
    private String imgPrice;
    private String imgDescription;
    private  String img_City;
    private  String img_Country;
    private  String img_number;

    public Upload() {
    }

    public Upload(String imgName, String imgUrl, String imgPrice, String imgDescription, String img_City, String img_Country, String img_number) {
        this.imgName = imgName;

        this.imgUrl = imgUrl;
        this.imgPrice = imgPrice;
        this.imgDescription = imgDescription;
        this.img_City = img_City;
        this.img_Country = img_Country;
        this.img_number = img_number;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgPrice() {
        return imgPrice;
    }

    public void setImgPrice(String imgPrice) {
        this.imgPrice = imgPrice;
    }

    public String getImgDescription() {
        return imgDescription;
    }

    public void setImgDescription(String imgDescription) {
        this.imgDescription = imgDescription;
    }

    public String getImg_City() {
        return img_City;
    }

    public void setImg_City(String img_City) {
        this.img_City = img_City;
    }

    public String getImg_Country() {
        return img_Country;
    }

    public void setImg_Country(String img_Country) {
        this.img_Country = img_Country;
    }

    public String getImg_number() {
        return img_number;
    }

    public void setImg_number(String img_number) {
        this.img_number = img_number;
    }
}
