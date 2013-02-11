package ru.hotel72.domains;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 21.01.13
 * Time: 23:48
 * To change this template use File | Settings | File Templates.
 */
public class Flat {
    public Integer id;
    public Integer post_id;
    public String street;
    public Double cost;
    public Double square;
    public String short_desc;
    public String full_desc;
    public String hotel_url;
    public Integer rooms;
    public double[] coords;
    public ArrayList<Photo> photos;
    public ArrayList<String> options;

//    public static final Parcelable.Creator<Flat> CREATOR = new Parcelable.Creator<Flat>() {
//        public Flat createFromParcel(Parcel in) {
//            return new Flat(in);
//        }
//
//        @Override
//        public Flat[] newArray(int i) {
//            return new Flat[i];
//        }
//    };
//
//    public Flat(){}
//
//    private Flat(Parcel in) {
//        id = in.readInt();
//        post_id = in.readInt();
//        street = in.readString();
//        cost = in.readDouble();
//        square = in.readDouble();
//        short_desc = in.readString();
//        full_desc = in.readString();
//        hotel_url = in.readString();
//        rooms = in.readInt();
//        coords = in.createDoubleArray();
//        photos = in.createTypedArrayList(Photo.CREATOR);
//        options = in.createStringArrayList();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(id);
//        parcel.writeInt(post_id);
//        parcel.writeString(street);
//        parcel.writeDouble(cost);
//        parcel.writeDouble(square);
//        parcel.writeString(short_desc);
//        parcel.writeString(full_desc);
//        parcel.writeString(hotel_url);
//        parcel.writeInt(rooms);
//        parcel.writeDoubleArray(coords);
//        parcel.writeList(photos);
//        parcel.writeList(options);
//    }
}

