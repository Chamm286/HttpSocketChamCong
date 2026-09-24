package com.httpchamcong.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    public static class ClientInfo {
        private String username;
        private String ip;
        private int port;
        private LocalDateTime loginTime;
        private LocalDateTime lastActivity;
        private int requestCount;
        private String status;

        public ClientInfo(String username, String ip, int port) {
            this.username = username;
            this.ip = ip;
            this.port = port;
            this.loginTime = LocalDateTime.now();
            this.lastActivity = LocalDateTime.now();
            this.requestCount = 1;
            this.status = "ONLINE";
        }

        // ===== GETTERS =====
        public String getUsername() { return username; }
        public String getIp() { return ip; }
        public int getPort() { return port; }
        public int getRequestCount() { return requestCount; }
        public String getStatus() { return status; }

        public String getLoginTimeStr() {
            return loginTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        public String getLastActivityStr() {
            return lastActivity.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }

    private static final Map<String, ClientInfo> clients = new ConcurrentHashMap<>();

    public static void addOrUpdate(String username, String ip, int port) {
        ClientInfo c = clients.get(username);
        if (c != null) {
            c.lastActivity = LocalDateTime.now();
            c.requestCount++;
            c.status = "ONLINE";
            c.ip = ip;
            c.port = port;
        } else {
            clients.put(username, new ClientInfo(username, ip, port));
        }
    }

    public static void setOffline(String username) {
        ClientInfo c = clients.get(username);
        if (c != null) c.status = "OFFLINE";
    }

    public static Map<String, ClientInfo> getAll() {
        return clients;
    }

    public static int getOnlineCount() {
        int count = 0;
        for (ClientInfo c : clients.values()) {
            if ("ONLINE".equals(c.status)) count++;
        }
        return count;
    }

    public static void clear() {
        clients.clear();
    }
}