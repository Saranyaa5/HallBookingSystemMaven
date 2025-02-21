package com.hallbooking.model;

public class Hall {
    private String hallId;
    private String hallName;
    private int capacity;
    private String amenities;
    private String location;

    public Hall(String hallId, String hallName, int capacity, String amenities, String location) {
        this.hallId = hallId;
        this.hallName = hallName;
        this.capacity = capacity;
        this.amenities = amenities;
        this.location = location;
    }

    public String getHallId() { return hallId; }
    public String getHallName() { return hallName; }
    public int getCapacity() { return capacity; }
    public String getAmenities() { return amenities; }
    public String getLocation() { return location; }
}
