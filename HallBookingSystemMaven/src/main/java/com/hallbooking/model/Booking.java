package com.hallbooking.model;

import java.util.Date;

public class Booking {
    private int bookingId;
    private int userId;
    private String hallId;
    private Date bookingDate;

    public Booking(int userId, String hallId, Date bookingDate) {
        this.userId = userId;
        this.hallId = hallId;
        this.bookingDate = bookingDate;
    }

    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public String getHallId() { return hallId; }
    public Date getBookingDate() { return bookingDate; }

	public void setBookingId(int bookingId) {
		this.bookingId=bookingId;
		
	}
	public Booking(int bookingId, int userId, String hallId, Date bookingDate) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.hallId = hallId;
        this.bookingDate = bookingDate;
    }


	
}
