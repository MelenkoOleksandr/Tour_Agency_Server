package com.example.touragency.servlets;

import com.example.touragency.entities.Reservation;
import com.example.touragency.entities.Tour;
import com.example.touragency.services.ReservationService;
import com.example.touragency.utils.JsonParser;
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

@WebServlet(name = "ReservationServlet", value = "/reservations/*")
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService;

    public ReservationServlet() {
        this.reservationService = new ReservationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        long authUserId = (int) request.getAttribute("userId");

        try {
            List<Reservation> reservations = reservationService.getReservationsByUserId(authUserId);
            JSONArray jsonArray = new JSONArray();
            for (Reservation reservation : reservations) {
                JSONObject tourJson = JsonParser.stringifyReservation(reservation);
                jsonArray.put(tourJson);
            }
            out.print(jsonArray);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JSONObject reservationJson = JsonParser.parseJson(request);

        long authUserId = (int) request.getAttribute("userId");
        long tourId = UrlParser.getTourId(request);
        int amount = reservationJson.getInt("amount");

        try {
            reservationService.reserveTour(authUserId, tourId, amount);
            out.print("Reservation successful");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        long authUserId = (int) request.getAttribute("userId");
        long tourId = UrlParser.getTourId(request);

        try {
            reservationService.cancelReservation(authUserId, tourId);
            out.print("Reservation cancelled");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
