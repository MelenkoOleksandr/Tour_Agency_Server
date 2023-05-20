package com.example.touragency.services;

import com.example.touragency.daos.implementations.TourDao;
import com.example.touragency.entities.Tour;

import java.sql.SQLException;
import java.util.List;

public class TourService {
    private final TourDao tourDao;

    public TourService() {
        this.tourDao = new TourDao();
    }

    public void deleteTour(long id) throws SQLException {
        tourDao.deleteTour(id);
    }

    public void addTour(long travelAgentId, Tour tour) throws SQLException {
        tourDao.addTour(travelAgentId, tour);
    }

    public void updateTour(Tour tour) throws SQLException {
        tourDao.updateTour(tour);
    }

    public List<Tour> getAllTours() throws SQLException {
        return tourDao.getAllTours();
    }

    public Tour getTourById(long id) throws SQLException {
        return tourDao.getTourById(id);
    }
}
