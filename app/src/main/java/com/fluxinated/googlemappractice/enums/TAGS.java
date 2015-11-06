package com.fluxinated.googlemappractice.enums;

/**
 * Created by Fluxi on 11/6/2015.
 */
public enum TAGS
{
    ORIGIN("ORIGIN"),
    DESTINATION("DESTINATION");
    private String TAG;
    TAGS(String mtag)
    {
        TAG = mtag;
    }

    public String getTag()
    {
        return TAG;
    }


}
