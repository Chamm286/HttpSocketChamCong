package com.httpchamcong.server;

import com.httpchamcong.model.User;
import com.httpchamcong.util.SessionManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Router router;
    private final HttpSocketServer server;

    public ClientHandler(Socket socket, Router router, HttpSocketServer server) {
        this.socket = socket;
        this.router = router;
        this.server = server;
    }

    @Override
    public void run() {
        String clientInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        String threadName = Thread.currentThread().getName();

        server.logExternal("[" + threadName + "] + Client: " + clientInfo);

        try (Socket s = socket) {
            InputStream in = s.getInputStream();
            OutputStream out = s.getOutputStream();

            HttpRequestParser req = HttpRequestParser.parse(in);
            if (req == null) return;

            ServerStats.requestHandled();
            server.logExternal("[" + threadName + "]   " + req);

            // Cập nhật ClientManager nếu có token
            String token = req.getBearerToken();
            if (token != null) {
                User u = SessionManager.getUser(token);
                if (u != null) {
                    ClientManager.addOrUpdate(
                        u.getUsername(),
                        socket.getInetAddress().getHostAddress(),
                        socket.getPort()
                    );
                }
            }

            HttpResponseBuilder res = router.handle(req);
            res.writeTo(out);

        } catch (Exception e) {
            server.logExternal("[!] Loi client " + clientInfo + ": " + e.getMessage());
        } finally {
            ServerStats.clientDisconnected();
            server.logExternal("[" + threadName + "] - Dong: " + clientInfo);
        }
    }
}