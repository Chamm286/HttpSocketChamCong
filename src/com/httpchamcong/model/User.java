 package com.httpchamcong.model;

public class User {
    private String username;
    private String password;
    private String fullName;
    private String role;

    public User(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRole()     { return role; }

    public String toJson() {
        return String.format(
            "{\"username\":\"%s\",\"fullName\":\"%s\",\"role\":\"%s\"}",
            username, fullName, role
        );
    }

    @Override
    public String toString() {
        return "User{" + username + ", " + fullName + ", " + role + "}";
    }
}
