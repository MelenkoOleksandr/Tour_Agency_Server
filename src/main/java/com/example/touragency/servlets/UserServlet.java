package com.example.touragency.servlets;

import com.example.touragency.entities.User;
import com.example.touragency.services.UserService;
import com.example.touragency.utils.AccessUtils;
import com.example.touragency.utils.JsonParser;
import com.example.touragency.utils.StatusUtils;
import com.example.touragency.utils.UrlParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    private final UserService userService;

    public UserServlet() {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            long userId = UrlParser.getUserId(request);
            String userType = request.getAttribute("userType").toString();

            if (userId == 0) {
                handleGetAllUsers(userType, out);
            } else {
                handleGetUserById(userId, out);
            }
            out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        long userId = UrlParser.getUserId(request);
        long authUserId = (int) request.getAttribute("userId");
        String userType = request.getAttribute("userType").toString();

        if (!AccessUtils.isUserAdmin(userType) && userId != authUserId ) {
            out.print(StatusUtils.ACCESS_DENIED);
            return;
        }

        JSONObject requestData = JsonParser.parseJson(request);
        User user = JsonParser.parseUser(userId, requestData);

        try {
            userService.updateUser(user);
            out.print(StatusUtils.SUCCESS_UPDATE);
        } catch (Exception e) {
            out.print(StatusUtils.FAILED_UPDATE);
            return;
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        long userId = UrlParser.getUserId(request);
        long authUserId = (int) request.getAttribute("userId");
        String userType = request.getAttribute("userType").toString();

        if (!AccessUtils.isUserAdmin(userType) && userId != authUserId ) {
            out.print(StatusUtils.ACCESS_DENIED);
            return;
        }

        try {
            userService.deleteUser(userId);
            out.print(StatusUtils.SUCCESS_DELETE);
        } catch (Exception e) {
            out.print(StatusUtils.FAILED_DELETE);
            return;
        }
        out.flush();
    }

    protected void handleGetAllUsers(String userType, PrintWriter out) {
        if (!AccessUtils.isUserAdmin(userType)) {
            out.print(StatusUtils.ACCESS_DENIED);
            return;
        }

        List<User> users = userService.getAllUsers();
        JSONArray jsonArray = new JSONArray();
        for (User user : users) {
            JSONObject userJson = JsonParser.stringifyUser(user);
            jsonArray.put(userJson);
        }
        out.print(jsonArray);
    }

    protected void handleGetUserById(long userId, PrintWriter out) {
        User user = userService.getUserById(userId);
        if (user == null) {
            out.print(StatusUtils.USER_NOT_FOUND);
            return;
        }
        JSONObject userJson = JsonParser.stringifyUser(user);
        out.print(userJson);
    }
}
