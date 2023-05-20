package com.example.touragency.servlets;

import com.example.touragency.entities.User;
import com.example.touragency.services.AuthService;
import com.example.touragency.services.UserService;
import com.example.touragency.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RefreshTokenServlet", value = "/refreshToken")
public class RefreshTokenServlet extends HttpServlet {

    private final AuthService authService;
    private final UserService userService;
    public RefreshTokenServlet() {
        this.authService = new AuthService();
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        handleRefresh(request, response, out);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        handleLogout(request, response, out);
    }
    protected void handleRefresh(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        JSONObject requestData = JsonParser.parseJson(request);
        String refreshToken = requestData.getString("refreshToken");

        //Validate refresh token
        Jws<Claims> jws = TokenCreator.validateToken(refreshToken, "SECRET_KEY");
        if (jws == null) {
           out.print("Invalid refresh token");
           return;
        }

        try {
            String userType = (String) jws.getBody().get("userType");
            int userId = Integer.parseInt(jws.getBody().getSubject());
            clearAccessCookie(request, response);
            String accessToken = TokenCreator.createAccessToken(String.valueOf(userId), userType, "SECRET_KEY", 1);
            response.addCookie(CookieUtils.generateCookie("accessToken", accessToken));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(e.getMessage());
        }
    }


    protected void handleLogout(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        clearAccessCookie(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void clearAccessCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("accessToken")) {
                    System.out.println(cookie.getName() + " " + cookie.getMaxAge());
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}
