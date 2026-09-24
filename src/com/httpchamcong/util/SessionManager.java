 package com.httpchamcong.util;

import com.httpchamcong.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, User> sessions = new ConcurrentHashMap<>();

    public static void createSession(String token, User user) {
        sessions.put(token, user);
    }

    public static User getUser(String token) {
        if (token == null) return null;
        return sessions.get(token);
    }

    public static void remove(String token) {
        if (token != null) sessions.remove(token);
    }
}
