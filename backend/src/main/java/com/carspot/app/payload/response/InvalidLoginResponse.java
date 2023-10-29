package com.carspot.app.payload.response;

public class InvalidLoginResponse {
    private String emailAddress;
    private String password;

    public InvalidLoginResponse() {
        this.emailAddress = "Invalid username or password";
        this.password = "Invalid username or password";
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
