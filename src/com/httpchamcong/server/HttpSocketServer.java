package com.httpchamcong.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HTTP Server — có thể start/stop, không blocking main.
 * UI gọi start() / stop().
 */
public class HttpSocketServer {

    public static final int PORT = 2020;
    public static final int POOL_SIZE = 20;

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private volatile boolean running = false;
    private Thread acceptThread;
    private final Router router = new Router();

    /** Callback để UI cập nhật log */
    public interface LogListener {
        void onLog(String message);
    }

    private LogListener logListener;

    public void setLogListener(LogListener listener) {
        this.logListener = listener;
    }

    private void log(String msg) {
        System.out.println(msg);
        if (logListener != null) logListener.onLog(msg);
    }

    /** Khởi động server — chạy accept loop trong thread riêng */
    public void start() throws IOException {
        if (running) return;

        serverSocket = new ServerSocket(PORT);
        pool = Executors.newFixedThreadPool(POOL_SIZE);
        running = true;
        ServerStats.serverStarted();

        acceptThread = new Thread(this::acceptLoop, "HttpServer-Accept");
        acceptThread.setDaemon(true);
        acceptThread.start();

        log("[SERVER] Started on port " + PORT + " with " + POOL_SIZE + " threads");
    }

    /** Vòng lặp chấp nhận client */
    private void acceptLoop() {
        while (running) {
            try {
                Socket client = serverSocket.accept();
                ServerStats.clientConnected();
                pool.execute(new ClientHandler(client, router, this));
            } catch (IOException e) {
                if (running) log("[ERROR] Accept: " + e.getMessage());
            }
        }
    }

    /** Dừng server */
    public void stop() {
        if (!running) return;
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
        } catch (IOException e) { e.printStackTrace(); }

        if (pool != null) pool.shutdownNow();

        log("[SERVER] Stopped");
    }

    public boolean isRunning() { return running; }

    /** Log từ bên ngoài (ClientHandler gọi) */
    public void logExternal(String msg) { log(msg); }
}