package com.example.touragency.servlets;

import com.example.touragency.entities.Tour;
import com.example.touragency.entities.UserType;
import com.example.touragency.services.TourService;
import com.example.touragency.utils.AccessUtils;
import com.example.touragency.utils.JsonParser;
import com.example.touragency.utils.StatusUtils;
import com.example.touragency.utils.UrlParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TourServlet", value = "/tours/*")
public class TourServlet extends HttpServlet {
    private final TourService tourService;

    public TourServlet() {
        this.tourService = new TourService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Get the tour ID from the URL if no ID is provided, return 0(all tours)
        long tourId = UrlParser.getTourId(request);

        try {
            if (tourId == 0) {
                List<Tour> tours = tourService.getAllTours();
                JSONArray jsonArray = new JSONArray();
                for (Tour tour : tours) {
                    JSONObject tourJson = JsonParser.stringifyTour(tour);
                    jsonArray.put(tourJson);
                }
                out.print(jsonArray);
            } else {
                Tour tour = tourService.getTourById(tourId);
                if (tour == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                JSONObject tourJson = JsonParser.stringifyTour(tour);
                out.print(tourJson);
            }
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject requestBody = JsonParser.parseJson(request);

        long authUserId = (int) request.getAttribute("userId");
        String userType = request.getAttribute("userType").toString();

        if (userType.equals(UserType.CUSTOMER.toString())) {
            out.print(StatusUtils.ACCESS_DENIED);
            return;
        }

        Tour tour = JsonParser.parseNewTour(requestBody);
        try {
            tourService.addTour(authUserId, tour);
            out.print(StatusUtils.SUCCESS_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
            out.print(StatusUtils.FAILED_CREATE);
        }
        out.flush();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject requestData = JsonParser.parseJson(request);

        long tourId = UrlParser.getTourId(request);
        long authUserId = (int) request.getAttribute("userId");
        String userType = request.getAttribute("userType").toString();
        Tour tour = JsonParser.parseTour(tourId, authUserId, requestData);
        System.out.println(authUserId);

        if (!AccessUtils.isUserAdmin(userType) && tour.getTravelAgentId() != authUserId ) {
            out.print(StatusUtils.ACCESS_DENIED);
            return;
        }

        try {
            tourService.updateTour(tour);
            out.print(StatusUtils.SUCCESS_UPDATE);
        } catch (SQLException e) {
            e.printStackTrace();
            out.print(StatusUtils.FAILED_UPDATE);
        }
        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        long tourId = UrlParser.getTourId(request);

        try {
            Tour tour = tourService.getTourById(tourId);
            long authUserId = (int) request.getAttribute("userId");
            String userType = request.getAttribute("userType").toString();

            if (!AccessUtils.isUserAdmin(userType) && tour.getTravelAgentId() != authUserId ) {
                out.print(StatusUtils.ACCESS_DENIED);
                return;
            }

            tourService.deleteTour(tourId);
            out.print(StatusUtils.SUCCESS_DELETE);
        }   catch (SQLException e) {
            e.printStackTrace();
            out.print(StatusUtils.FAILED_DELETE);
        }
        out.flush();
    }
}
