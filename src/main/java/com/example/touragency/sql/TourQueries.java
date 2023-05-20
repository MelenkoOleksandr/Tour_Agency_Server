package com.example.touragency.sql;

public class TourQueries {
    public static final String GET_ALL_TOURS = "SELECT * FROM tours";
    public static final String GET_TOUR_BY_ID = "SELECT * FROM tours WHERE id = ?";
    public static final String ADD_TOUR = "INSERT INTO tours (travel_agent_id, tour_type, title, description, image, price, available_places, start_date, end_date, is_hot, hot_discount, departure_place, country) VALUES (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
    public static final String UPDATE_TOUR = "UPDATE tours SET travel_agent_id = ?, tour_type = ?, title = ?, description = ?, image = ?, price = ?, available_places = ?, start_date = ?, end_date = ?, is_hot = ?, hot_discount = ?, departure_place = ?, country = ? WHERE id = ?";
    public static final String DELETE_TOUR = "DELETE FROM tours WHERE id = ?";
    public static final String RESERVE_TOUR = "INSERT INTO users_tours (user_id, tour_id, amount) VALUES (? , ? , ?)";
    public static final String UPDATE_TOUR_AVAILABLE_PLACES = "UPDATE tours SET available_places = ? WHERE id = ?";
    public static final String GET_RESERVATIONS_BY_USER_ID = "SELECT * FROM tours INNER JOIN users_tours ON tours.id = users_tours.tour_id WHERE users_tours.user_id = ?";

    public static final String CANCEL_RESERVATION = "DELETE FROM users_tours WHERE user_id = ? AND tour_id = ?";
    public static final String GET_RESERVATION_AMOUNT = "SELECT amount FROM users_tours WHERE user_id = ? AND tour_id = ?";
}
