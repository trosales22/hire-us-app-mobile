package com.trosales.hireusapp.classes.commons;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;

public class AndroidNetworkingShortcuts {
    //Prefetch a request (so that it can return from cache when required at instant)
    public static void prefetchPersonalInfo(String url, String params, String tag, Priority priority){
        AndroidNetworking.get(url)
                .addPathParameter("username_email", params)
                .setTag(tag)
                .setPriority(priority)
                .build()
                .prefetch();
    }
}
