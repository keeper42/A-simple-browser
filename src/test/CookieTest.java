package test;
// Created by LJF on 2017/5/19.

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieTest {

    public static void main(String[] args) {
        HttpServletRequest request = null;
        HttpServletResponse response = null;

        Cookie cookie = new Cookie("cookiename", "cookievalue");
        cookie.setMaxAge(3600);
        cookie.setPath("/");
//        response.addCookie(cookie);

    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if(maxAge > 0){
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    public static Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }

    private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                System.out.println(cookieName);
                cookieMap.put(cookieName, cookie);
            }
        }

        return cookieMap;
    }

}
