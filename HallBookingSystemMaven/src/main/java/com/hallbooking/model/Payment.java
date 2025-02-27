package com.hallbooking.model;

public class Payment {
    private int paymentId;
    private int bookingId;
    private int userId;
    private double amount;
    private String paymentMode;
    private String status;

    public Payment(int bookingId, int userId, double amount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.status = "PENDING"; 
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public double getAmount() { return amount; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
