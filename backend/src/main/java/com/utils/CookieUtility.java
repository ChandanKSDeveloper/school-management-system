package com.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// todo :  cookie.setSecure(false) ->  cookie.setSecure(true)
public class CookieUtility {

    // Create a secure cookie
    public static void createCookie(HttpServletResponse resp, String name, String value, int maxAge ){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);// Set to false for local development without HTTPS
        cookie.setPath("/api/");
        cookie.setMaxAge(maxAge);
        resp.addCookie(cookie);
    }

    // Expire/delete a cookie
    public static void expireCookie(HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Immediately expire
        response.addCookie(cookie);
    }

    // Get cookie value by name
    public static String getCookieValue(HttpServletRequest req, String name){
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
