 package com.httpchamcong.server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thống kê server — Thread-safe
 */
public class ServerStats {
    private static final AtomicInteger activeClients = new AtomicInteger(0);
    private static final AtomicInteger totalClients  = new AtomicInteger(0);
    private static final AtomicLong    totalRequests = new AtomicLong(0);
    private static volatile long      startTime     = 0;

    public static void serverStarted() {
        startTime = System.currentTimeMillis();
    }

    public static void clientConnected() {
        activeClients.incrementAndGet();
        totalClients.incrementAndGet();
    }

    public static void clientDisconnected() {
        activeClients.decrementAndGet();
    }

    public static void requestHandled() {
        totalRequests.incrementAndGet();
    }

    public static int getActiveClients()  { return activeClients.get(); }
    public static int getTotalClients()   { return totalClients.get(); }
    public static long getTotalRequests() { return totalRequests.get(); }

    public static String getUptime() {
        if (startTime == 0) return "00:00:00";
        long sec = (System.currentTimeMillis() - startTime) / 1000;
        return String.format("%02d:%02d:%02d", sec / 3600, (sec % 3600) / 60, sec % 60);
    }
}
