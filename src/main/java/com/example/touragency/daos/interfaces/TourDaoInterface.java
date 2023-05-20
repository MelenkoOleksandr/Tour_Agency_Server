package com.example.touragency.daos.interfaces;

import com.example.touragency.entities.Tour;

import java.sql.SQLException;
import java.util.List;

public interface TourDaoInterface {
    List<Tour> getAllTours() throws SQLException;
    Tour getTourById(long id) throws SQLException;
    void addTour(long travelAgentId, Tour tour) throws SQLException;
    void updateTour(Tour tour) throws SQLException;
    void deleteTour(long id) throws SQLException;
    void reserveTour(long userId, long tourId, int amount) throws SQLException;
}
