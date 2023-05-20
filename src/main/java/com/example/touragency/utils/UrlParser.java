package com.example.touragency.utils;

import jakarta.servlet.http.HttpServletRequest;

public class UrlParser {
    public static long getUserId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return 0;
        }
        return Long.parseLong(pathInfo.substring(1));
    }

    public static long getTourId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return 0;
        }
        return Long.parseLong(pathInfo.substring(1));
    }

    public static String getAuthType(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "";
        }
        return pathInfo.substring(1);
    }
}
