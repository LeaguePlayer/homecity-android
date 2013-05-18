package ru.hotel72.domains;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    public Integer id;
    public String url;
    public Integer data_sort;
    public Integer type_photo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeInt(data_sort);
        out.writeInt(type_photo);
        out.writeString(url);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Photo(){}

    private Photo(Parcel in) {
        id = in.readInt();
        data_sort = in.readInt();
        type_photo = in.readInt();
        url = in.readString();
    }
}
