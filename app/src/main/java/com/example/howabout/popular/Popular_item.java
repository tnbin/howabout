package com.example.howabout.popular;

public class Popular_item {

    private String image;
    private String place;


    public Popular_item(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    @Override
    public String toString() {
        return "Popular_item{" +
                "image='" + image + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
