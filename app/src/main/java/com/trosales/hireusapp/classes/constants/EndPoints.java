package com.trosales.hireusapp.classes.constants;

public class EndPoints {
    //use this for dev purposes only
    private static final String BASE_URL = "http://192.168.1.12/hire-us/api/";

    //use this for prod purposes only
    //private static final String BASE_URL = "https://[URL]/apis/";

    public static final String GET_ALL_TALENTS_URL = BASE_URL + "talents/get_all_talents";
    public static final String GET_TALENT_CATEGORIES_URL = BASE_URL + "talents/get_talent_categories";
}
