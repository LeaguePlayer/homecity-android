package ru.hotel72.domains;

public class Photo {
    public Integer id;
    public String url;
    public Integer data_sort;
    public Integer type_photo;

//    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
//        public Photo createFromParcel(Parcel in) {
//            return new Photo(in);
//        }
//
//        @Override
//        public Photo[] newArray(int i) {
//            return new Photo[i];
//        }
//    };
//
//    public Photo(){}
//
//    private Photo(Parcel in){
//        id = in.readInt();
//        url = in.readString();
//        data_sort = in.readInt();
//        type_photo = in.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(id);
//        parcel.writeString(url);
//        parcel.writeInt(data_sort);
//        parcel.writeInt(type_photo);
//    }
}
