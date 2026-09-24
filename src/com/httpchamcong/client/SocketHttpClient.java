package com.httpchamcong.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * HTTP Client — tự build HTTP/1.1 request thủ công và gửi qua Socket TCP.
 *
 * ═══════════════════════════════════════════════════════════════
 *  📌 CẤU HÌNH KẾT NỐI — 2 TRƯỜNG HỢP
 * ═══════════════════════════════════════════════════════════════
 *
 *  🅰️ CHẠY TRÊN 1 MÁY (Local — Test tại nhà):
 *     private static final String HOST = "localhost";
 *
 *  🅱️ CHẠY TRÊN 2 MÁY (LAN — Demo ở trường với Tri):
 *     private static final String HOST = "192.168.1.100";  ← IP máy Server
 *
 *  📌 Cách tìm IP máy Server (máy chạy ServerApp):
 *     Mở CMD trên máy Server → chạy: ipconfig
 *     Tìm dòng "IPv4 Address" của card Wi-Fi (VD: 192.168.1.100)
 *     Copy IP đó → paste vào HOST ở máy Client.
 *
 *  📌 Điều kiện 2 máy kết nối:
 *     1. Cùng mạng Wi-Fi (LAN)
 *     2. Máy Server đã START SERVER (port 8080)
 *     3. Firewall máy Server đã mở port 8080 (hoặc tắt firewall tạm thời)
 * ═══════════════════════════════════════════════════════════════
 */
public class SocketHttpClient {

    // ══════════════════════════════════════════════════════════
    // 🅰️ TRƯỜNG HỢP 1: 1 MÁY (Local) — BỎ COMMENT DÒNG NÀY
    // ══════════════════════════════════════════════════════════
    private static final String HOST = "localhost";

    // ══════════════════════════════════════════════════════════
    // 🅱️ TRƯỜNG HỢP 2: 2 MÁY (LAN) — BỎ COMMENT 2 DÒNG NÀY
    //     và comment dòng HOST = "localhost" ở trên
    // ══════════════════════════════════════════════════════════
    // private static final String HOST = "192.168.1.100";   // ← IP máy Trâm
    // private static final String HOST = "192.168.1.105";   // ← IP máy Tri

    private static final int PORT = 2020;

    private String token = null;

    public void setToken(String t) { this.token = t; }
    public String getToken() { return token; }

    /** Trả về thông tin server để hiển thị trên UI */
    public static String getServerInfo() {
        return HOST + ":" + PORT;
    }

    public String get(String path) throws IOException {
        return request("GET", path, null);
    }

    public String post(String path, String jsonBody) throws IOException {
        return request("POST", path, jsonBody);
    }

    /**
     * Gửi HTTP/1.1 request thủ công qua Socket.
     */
    private String request(String method, String path, String body) throws IOException {
        try (Socket socket = new Socket(HOST, PORT)) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // ============ BUILD HTTP/1.1 REQUEST ============
            StringBuilder req = new StringBuilder();

            // ⭐ Request Line
            req.append(method).append(" ").append(path).append(" HTTP/1.1\r\n");

            // ⭐ Headers
            req.append("Host: ").append(HOST).append(":").append(PORT).append("\r\n");
            req.append("User-Agent: JavaChamCongClient/1.0\r\n");
            req.append("Accept: application/json\r\n");

            if (token != null) {
                req.append("Authorization: Bearer ").append(token).append("\r\n");
            }

            if (body != null) {
                byte[] b = body.getBytes(StandardCharsets.UTF_8);
                req.append("Content-Type: application/json; charset=UTF-8\r\n");
                req.append("Content-Length: ").append(b.length).append("\r\n");
            }
            req.append("Connection: close\r\n");
            req.append("\r\n");

            if (body != null) req.append(body);

            // ============ GỬI QUA SOCKET ============
            out.write(req.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();

            // ============ ĐỌC HTTP RESPONSE ============
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));

            String statusLine = reader.readLine();
            System.out.println("<<< " + statusLine);

            int contentLength = 0;
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            char[] buf = new char[contentLength];
            int read = 0;
            while (read < contentLength) {
                int r = reader.read(buf, read, contentLength - read);
                if (r == -1) break;
                read += r;
            }
            return new String(buf, 0, read);
        }
    }
}