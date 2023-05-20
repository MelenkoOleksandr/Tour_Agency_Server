package com.example.touragency.entities;

public enum TourType {
    VACATION("VACATION"),
    EXCURSION("EXCURSION"),
    SHOPPING("SHOPPING");

    private final String name;

    TourType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TourType getTourTypeByName(String name) {
        for (TourType tourType : TourType.values()) {
            if (tourType.getName().equals(name)) {
                return tourType;
            }
        }
        return null;
    }
}