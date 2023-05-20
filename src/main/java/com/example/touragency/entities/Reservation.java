package com.example.touragency.entities;

public class Reservation {
    private Tour tour;
    private int amount;

    public Reservation(Tour tour, int amount) {
        this.tour = tour;
        this.amount = amount;
    }

    public Tour getTour() {
        return tour;
    }

    public int getAmount() {
        return amount;
    }

}
