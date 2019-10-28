package com.trosales.hireusapp.classes.constants;

public class EndPoints {
    //dev (just changed the ip address)
    private static final String BASE_URL = "http://192.168.1.12/hire-us/";

    //production
    //private static final String BASE_URL = "https://hireusph.com/";

    private static final String API_URL = BASE_URL + "api/";
    public static final String UPLOADS_URL = BASE_URL + "uploads/";

    public static final String LOGIN_USER_URL = API_URL + "mobile/user_login";
    public static final String LOGIN_TALENT_URL = API_URL + "mobile/talent_login";
    public static final String GET_PERSONAL_INFO_URL = API_URL + "mobile/get_personal_info";
    public static final String GET_TALENT_PERSONAL_INFO_URL = API_URL + "mobile/get_talent_personal_info";
    public static final String GET_ALL_TALENTS_URL = API_URL + "talents/get_all_talents";
    public static final String GET_ALL_CATEGORIES_URL = API_URL + "categories/get_all_categories";
    public static final String GET_SELECTED_TALENT_DETAILS_URL = API_URL + "talents/get_talent_details";
    public static final String GET_UNAVAILABLE_DATES_URL = API_URL + "talents/get_talent_unavailable_dates";
    public static final String GET_BOOKING_LIST_BY_CLIENT_ID_URL = API_URL + "client/get_booking_list_by_client_id";
    public static final String ADD_TO_TEMP_BOOKING_LIST_URL = API_URL + "client/add_to_temp_booking_list";
    public static final String ADD_TO_CLIENT_BOOKING_LIST_URL = API_URL + "client/add_to_client_booking_list";
    public static final String GET_ALREADY_RESERVED_SCHED_URL = API_URL + "client/get_already_reserved_schedule";
    public static final String GET_ALL_PROVINCES_URL = API_URL + "client/get_all_provinces";
    public static final String GET_ALL_CITY_MUNI_URL = API_URL + "client/get_city_muni_by_province_code";
    public static final String GET_ALL_CLIENT_BOOKED_URL = API_URL + "talents/get_all_client_booked";
    public static final String GET_RESERVED_SCHEDULE_OF_TALENT = API_URL + "talents/get_reserved_schedule_talent";
    public static final String START_PAYMENT_URL = API_URL + "payment/start_payment";
}
