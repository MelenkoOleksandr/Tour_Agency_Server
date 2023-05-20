package com.example.touragency.utils;

import com.example.touragency.entities.User;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.PrintWriter;

public class ResponseUtils {

    public static void createAuthResponse(HttpServletResponse response, PrintWriter out, User registeredUser) {
        String accessToken = TokenCreator.createAccessToken(registeredUser.getId().toString(), registeredUser.getUserType().toString(), "SECRET_KEY", 15);
        String refreshToken = TokenCreator.createRefreshToken(registeredUser.getId().toString(), registeredUser.getUserType().toString(), "SECRET_KEY", 30);
        response.addCookie(CookieUtils.generateCookie("accessToken", accessToken));

        JSONObject result = new JSONObject();
        result.put("user", JsonParser.stringifyUser(registeredUser));
        result.put("refreshToken", refreshToken);
        out.print(result);
    }
}
