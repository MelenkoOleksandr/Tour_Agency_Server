package com.example.touragency.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    private Long id;
    private Long travelAgentId;
    private TourType type;
    private String title;
    private String description;
    private String image;
    private int price;
    private int availablePlaces;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isHot;
    private int hotDiscount;
    private String departurePlace;
    private String country;

    public Tour(long travelAgentId, TourType type, String title, String description, String image, int price, int availablePlaces, LocalDate startDate, LocalDate endDate, boolean isHot, int hotDiscount, String departurePlace, String country) {
        this.travelAgentId = travelAgentId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.availablePlaces = availablePlaces;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isHot = isHot;
        this.hotDiscount = hotDiscount;
        this.departurePlace = departurePlace;
        this.country = country;
    }

    public Tour(TourType type, String title, String description, String image, int price, int availablePlaces, LocalDate startDate, LocalDate endDate, boolean isHot, int hotDiscount, String departurePlace, String country) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.availablePlaces = availablePlaces;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isHot = isHot;
        this.hotDiscount = hotDiscount;
        this.departurePlace = departurePlace;
        this.country = country;
    }
}