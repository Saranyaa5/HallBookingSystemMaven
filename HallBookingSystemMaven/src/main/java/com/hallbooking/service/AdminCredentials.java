package com.hallbooking.service;


abstract class AdminCredentials {
	 protected static final String ADMIN_USERNAME = "admin";
	    protected static final String ADMIN_PASSWORD = "admin123";

	    
	    protected abstract boolean validateAdmin(String username, String password);
}
