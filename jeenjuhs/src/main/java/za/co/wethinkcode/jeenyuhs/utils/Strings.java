package za.co.wethinkcode.jeenyuhs.utils;

public class Strings {

    public static String getName(String email){
        return email.split( "@" )[ 0 ];
    }
}
