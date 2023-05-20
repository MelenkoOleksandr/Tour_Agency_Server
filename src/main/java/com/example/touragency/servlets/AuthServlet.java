package com.example.touragency.servlets;

import com.example.touragency.entities.User;
import com.example.touragency.entities.UserType;
import com.example.touragency.exceptions.AuthException;
import com.example.touragency.services.AuthService;
import com.example.touragency.services.UserService;
import com.example.touragency.utils.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "AuthServlet", value = "/auth/*")
public class AuthServlet extends HttpServlet {
    private final AuthService authService;
    private final UserService userService;
    public AuthServlet() {
        this.authService = new AuthService();
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authType = UrlParser.getAuthType(request);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (authType.equals("login")) {
            handleLogin(request, response, out);
        } else if (authType.equals("register")) {
            handleRegister(request,response, out);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    protected void handleLogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        JSONObject requestData = JsonParser.parseJson(request);
        String email = requestData.getString("email");
        String password = requestData.getString("password");

        try {
            User user = authService.login(email, password);
            ResponseUtils.createAuthResponse(response, out, user);
        } catch (AuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(e.getMessage());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(e.getMessage());
        }
    }

    protected void handleRegister(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        JSONObject requestData = JsonParser.parseJson(request);
        String email = requestData.getString("email");
        String password = requestData.getString("password");
        String name = requestData.getString("name");
        UserType userType = UserType.valueOf(requestData.getString("userType"));

        User user = new User(email, password, userType, name);
        User registeredUser = authService.register(user);
        ResponseUtils.createAuthResponse(response, out, registeredUser);
    }
}
