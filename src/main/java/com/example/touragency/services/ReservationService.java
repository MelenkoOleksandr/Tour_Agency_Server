package com.example.touragency.services;

import com.example.touragency.daos.implementations.TourDao;
import com.example.touragency.entities.Reservation;
import com.example.touragency.entities.Tour;

import java.sql.SQLException;
import java.util.List;

public class ReservationService {
    private final TourDao tourDao;

    public ReservationService() {
        this.tourDao = new TourDao();
    }

    public void reserveTour(long userId, long tourId, int amount) throws SQLException {
        tourDao.reserveTour(userId, tourId, amount);
    }

    public List<Reservation> getReservationsByUserId(long userId) throws SQLException {
       return tourDao.getReservationsByUserId(userId);
    }

    public void cancelReservation(long userId, long tourId) throws SQLException {
        tourDao.cancelReservation(userId, tourId);
    }
}
