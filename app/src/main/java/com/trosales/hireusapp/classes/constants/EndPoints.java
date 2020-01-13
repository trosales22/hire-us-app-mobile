package com.trosales.hireusapp.classes.constants;

public class EndPoints {
    //local
    public static final String BASE_URL = "http://192.168.1.20/hire-us/";
    
    //development
    //public static final String BASE_URL = "https://hireusph-dev.tristanrosales.com/";

    //production
    //private static final String BASE_URL = "https://hireusph.com/";

    private static final String API_URL = BASE_URL + "api/";

    public static final String LOGIN_USER_URL = API_URL + "mobile/user_login";
    public static final String LOGIN_TALENT_URL = API_URL + "mobile/talent_login";
    public static final String GET_PERSONAL_INFO_URL = API_URL + "mobile/get_personal_info";
    public static final String GET_TALENT_PERSONAL_INFO_URL = API_URL + "mobile/get_talent_personal_info";
    public static final String GET_ALL_TALENTS_URL = API_URL + "talents/get_all_talents";
    public static final String GET_SELECTED_TALENT_DETAILS_URL = API_URL + "talents/get_talent_details";
    public static final String GET_BOOKING_LIST_BY_CLIENT_ID_URL = API_URL + "client/get_booking_list_by_client_id";
    public static final String ADD_TO_BOOKING_LIST_URL = API_URL + "client/add_to_booking_list";
    public static final String GET_ALL_REGIONS_URL = API_URL + "client/get_all_regions";
    public static final String GET_ALL_PROVINCES_BY_REGION_CODE_URL = API_URL + "client/get_all_provinces_by_region_code";
    public static final String GET_ALL_CITY_MUNI_BY_PROVINCE_CODE_URL = API_URL + "client/get_city_muni_by_province_code";
    public static final String GET_ALL_CLIENT_BOOKED_URL = API_URL + "talents/get_all_client_booked";
    public static final String START_PAYMENT_URL = API_URL + "payment/start_payment";
    public static final String ADD_TALENT_REVIEWS_URL = API_URL + "talents/add_talent_reviews";
    public static final String GET_TALENT_REVIEWS_URL = API_URL + "talents/get_talent_reviews";
}
