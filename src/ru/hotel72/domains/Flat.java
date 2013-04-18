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
    public Integer cost;
    public int square;
    public String short_desc;
    public String full_desc;
    public String hotel_url;
    public Integer rooms;
    public double[] coords;
    public ArrayList<Photo> photos;
    public ArrayList<String> options;
    public boolean isLiked;
}

