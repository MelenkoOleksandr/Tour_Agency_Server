package com.example.touragency.utils;

import com.example.touragency.entities.*;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

public class JsonParser {
    public static JSONObject stringifyUser(User user) {
        JSONObject userJson = new JSONObject();
        userJson.put("id", user.getId());
        userJson.put("email", user.getEmail());
        userJson.put("name", user.getName());
        userJson.put("userType", user.getUserType().toString());
        userJson.put("surname", user.getSurname());
        userJson.put("phone", user.getPhone());
        userJson.put("img", user.getImg());
        return userJson;
    }

    public static JSONObject stringifyTour(Tour tour) {
        JSONObject tourJson = new JSONObject();
        tourJson.put("id", tour.getId());
        tourJson.put("travelAgentId", tour.getTravelAgentId());
        tourJson.put("type", tour.getType().getName());
        tourJson.put("title", tour.getTitle());
        tourJson.put("description", tour.getDescription());
        tourJson.put("image", tour.getImage());
        tourJson.put("price", tour.getPrice());
        tourJson.put("availablePlaces", tour.getAvailablePlaces());
        tourJson.put("startDate", tour.getStartDate());
        tourJson.put("endDate", tour.getEndDate());
        tourJson.put("isHot", tour.isHot());
        tourJson.put("hotDiscount", tour.getHotDiscount());
        tourJson.put("departurePlace", tour.getDeparturePlace());
        tourJson.put("country", tour.getCountry());
        return tourJson;
    }

    public static JSONObject stringifyReservation(Reservation reservation) {
        JSONObject tourJson = stringifyTour(reservation.getTour());
        JSONObject reservationJson = new JSONObject();
        reservationJson.put("tour", tourJson);
        reservationJson.put("amount", reservation.getAmount());

        return reservationJson;
    }

    public static JSONObject parseJson(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return new JSONObject(sb.toString());
    }

    public static User parseUser(long userId, JSONObject requestData) {
        User user = new User(
                userId,
                requestData.getString("email"),
                requestData.getString("img"),
                requestData.getString("name"),
                requestData.getString("surname"),
                requestData.getString("phone")
        );

        return user;
    }

    public static Tour parseTour(long tourId, long travelAgentId, JSONObject requestData) {
        Tour tour = new Tour(
                tourId,
                travelAgentId,
                TourType.valueOf(requestData.getString("type")),
                requestData.getString("title"),
                requestData.getString("description"),
                requestData.getString("image"),
                requestData.getInt("price"),
                requestData.getInt("availablePlaces"),
                LocalDate.parse(requestData.getString("startDate")),
                LocalDate.parse(requestData.getString("endDate")),
                requestData.getBoolean("isHot"),
                requestData.getInt("hotDiscount"),
                requestData.getString("departurePlace"),
                requestData.getString("country")
        );

        return tour;
    }

  public static Tour parseNewTour(JSONObject requestData) {
    Tour tour = new Tour(
            TourType.valueOf(requestData.getString("type")),
            requestData.getString("title"),
            requestData.getString("description"),
            requestData.getString("image"),
            requestData.getInt("price"),
            requestData.getInt("availablePlaces"),
            LocalDate.parse(requestData.getString("startDate")),
            LocalDate.parse(requestData.getString("endDate")),
            requestData.getBoolean("isHot"),
            requestData.getInt("hotDiscount"),
            requestData.getString("departurePlace"),
            requestData.getString("country")
    );

    return tour;
  }
}
