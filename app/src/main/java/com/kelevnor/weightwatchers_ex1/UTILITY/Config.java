package com.kelevnor.weightwatchers_ex1.UTILITY;

/**
 * Created by kelevnor on 2/7/18.
 */

public class Config {
    //Base Url for RESTful operation PullData
    public static String BASE_URL = "https://www.weightwatchers.com/";

    //PullData result
    public static int RESULT_PULLDATA_FAIL = 0;
    public static int RESULT_PULLDATA_SUCCESS = 1;

    //Generic Internet Fail Result
    public static int RESULT_INTERNET_FAIL = 2;

    //Generic Storage Permission and Internet Results
    public static int RESULT_EXT_STORAGE_SUCCESS = 3;
    public static int RESULT_EXT_STORAGE_SUCCESS_INTERNET_FAIL = 4;
    public static int RESULT_EXT_STORAGE_FAIL = 5;
    public static int RESULT_EXT_STORAGE_INTERNET_FAIL = 6;

    public static int RW_STORAGE_PERMISSION =  1111;

}