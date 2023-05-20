package com.example.touragency.daos.implementations;

import com.example.touragency.connection.PostgresConnectionPool;
import com.example.touragency.daos.interfaces.TourDaoInterface;
import com.example.touragency.entities.Reservation;
import com.example.touragency.entities.Tour;
import com.example.touragency.entities.TourType;
import com.example.touragency.sql.TourQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TourDao implements TourDaoInterface {
    @Override
    public List<Tour> getAllTours() throws SQLException {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.GET_ALL_TOURS);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Tour tour = handleReadTour(rs);
                tours.add(tour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tours;
    }

    @Override
    public Tour getTourById(long id) throws SQLException {
        Tour tour = new Tour();
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.GET_TOUR_BY_ID);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tour = handleReadTour(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tour;
    }

    @Override
    public void addTour(long travelAgentId, Tour tour) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.ADD_TOUR);
            handleSetTour(statement, travelAgentId, tour, false);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTour(Tour tour) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.UPDATE_TOUR);
            handleSetTour(statement, tour.getTravelAgentId(), tour, true);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTour(long id) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.DELETE_TOUR);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reserveTour(long userId, long tourId, int amount) throws SQLException {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.RESERVE_TOUR);
            statement.setLong(1, userId);
            statement.setLong(2, tourId);
            statement.setInt(3, amount);
            statement.executeUpdate();
            int currentAvailablePlaces = getTourById(tourId).getAvailablePlaces();
            statement = connection.prepareStatement(TourQueries.UPDATE_TOUR_AVAILABLE_PLACES);
            statement.setInt(1, currentAvailablePlaces - amount);
            statement.setLong(2, tourId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelReservation(long userId, long tourId) {
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.CANCEL_RESERVATION);
            statement.setLong(1, userId);
            statement.setLong(2, tourId);
            statement.executeUpdate();
            int currentAvailablePlaces = getTourById(tourId).getAvailablePlaces();
            statement = connection.prepareStatement(TourQueries.UPDATE_TOUR_AVAILABLE_PLACES);
            statement.setInt(1, currentAvailablePlaces + getReservationAmount(userId, tourId));
            statement.setLong(2, tourId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getReservationsByUserId(long userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.GET_RESERVATIONS_BY_USER_ID);
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation(handleReadTour(rs), rs.getInt("amount"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    private Tour handleReadTour(ResultSet rs) throws SQLException {
        Tour tour = new Tour();
        tour.setId(rs.getLong("id"));
        tour.setTravelAgentId(rs.getLong("travel_agent_id"));
        tour.setType(TourType.valueOf(rs.getString("tour_type")));
        tour.setTitle(rs.getString("title"));
        tour.setDescription(rs.getString("description"));
        tour.setImage(rs.getString("image"));
        tour.setPrice(rs.getInt("price"));
        tour.setAvailablePlaces(rs.getInt("available_places"));
        tour.setStartDate(rs.getDate("start_date").toLocalDate());
        tour.setEndDate(rs.getDate("end_date").toLocalDate());
        tour.setHot(rs.getBoolean("is_hot"));
        tour.setHotDiscount(rs.getInt("hot_discount"));
        tour.setDeparturePlace(rs.getString("departure_place"));
        tour.setCountry(rs.getString("country"));
        return tour;
    }

    private void handleSetTour(PreparedStatement statement, long travelAgentId, Tour tour, boolean update) throws SQLException {
        statement.setLong(1, travelAgentId);
        statement.setString(2, tour.getType().toString());
        statement.setString(3, tour.getTitle());
        statement.setString(4, tour.getDescription());
        statement.setString(5, tour.getImage());
        statement.setInt(6, tour.getPrice());
        statement.setInt(7, tour.getAvailablePlaces());
        statement.setDate(8, java.sql.Date.valueOf(tour.getStartDate()));
        statement.setDate(9, java.sql.Date.valueOf(tour.getEndDate()));
        statement.setBoolean(10, tour.isHot());
        statement.setInt(11, tour.getHotDiscount());
        statement.setString(12, tour.getDeparturePlace());
        statement.setString(13, tour.getCountry());
        if (update) {
            statement.setLong(14, tour.getId());
        }
    }

    public int getReservationAmount(long userId, long tourId) throws SQLException {
        int amount = 0;
        try (Connection connection = PostgresConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(TourQueries.GET_RESERVATION_AMOUNT);
            statement.setLong(1, userId);
            statement.setLong(2, tourId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }
}
