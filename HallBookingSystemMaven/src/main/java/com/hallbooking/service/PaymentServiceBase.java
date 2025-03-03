package com.hallbooking.service;

import java.util.Scanner;

public abstract class PaymentServiceBase {
    public abstract void makePayment(Scanner scanner, int userId);
}
