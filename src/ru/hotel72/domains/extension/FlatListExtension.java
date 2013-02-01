package ru.hotel72.domains.extension;

import ru.hotel72.domains.Flat;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 27.01.13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public class FlatListExtension {

    public static boolean Contains(ArrayList<Flat> flats, Flat object){
        for(Flat flat : flats){
            if(flat.id == object.id)
                return true;
        }

        return false;
    }
}
