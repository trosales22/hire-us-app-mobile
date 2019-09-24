package com.trosales.hireusapp.classes.constants;

public class EndPoints {
    //use this for dev purposes only
    private static final String API_BASE_URL = "http://192.168.1.11/hire-us/api/";
    public static final String UPLOADS_BASE_URL = "http://192.168.1.11/hire-us/uploads/";

    //use this for prod purposes only
    //private static final String API_BASE_URL = "https://[URL]/apis/";

    public static final String LOGIN_USER_URL = API_BASE_URL + "mobile/user_login";
    public static final String GET_PERSONAL_INFO_URL = API_BASE_URL + "mobile/get_personal_info";
    public static final String GET_ALL_TALENTS_URL = API_BASE_URL + "talents/get_all_talents";
    public static final String GET_ALL_CATEGORIES_URL = API_BASE_URL + "categories/get_all_categories";
    public static final String GET_SELECTED_TALENT_DETAILS_URL = API_BASE_URL + "talents/get_talent_details";
    public static final String GET_UNAVAILABLE_DATES_URL = API_BASE_URL + "talents/get_talent_unavailable_dates";
    public static final String GET_BOOKING_LIST_BY_CLIENT_ID_URL = API_BASE_URL + "client/get_booking_list_by_client_id";
}
