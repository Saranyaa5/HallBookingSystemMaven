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

    public String getHallId()
    { return hallId; }
    
    public void setHallId(String hallId){
    	this.hallId = hallId;
    	}
    
    public String getHallName() {
    	return hallName; 
    	}
    public void setHallName(String hallName) { 
    	this.hallName = hallName;
    	}
    
    public int getCapacity() {
    	return capacity; 
    	}
    public void setCapacity(int capacity) { 
    	this.capacity = capacity;
    	}
    
    public String getAmenities() { 
    	return amenities; 
    	}
    public void setAmenities(String amenities) {
    	this.amenities = amenities; 
    	}
    
    public String getLocation() {
    	return location; 
    	}
    public void setLocation(String location) {
    	this.location = location; 
    	}
}