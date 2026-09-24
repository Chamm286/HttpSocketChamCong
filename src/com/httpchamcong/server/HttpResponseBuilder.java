 package com.httpchamcong.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tự BUILD HTTP/1.1 RESPONSE để ghi ra Socket.
 *
 * Format HTTP/1.1 Response:
 *   HTTP/1.1 200 OK                      ← Status line
 *   Server: JavaChamCongServer/1.0       ← Headers
 *   Content-Type: application/json
 *   Content-Length: 45
 *                                        ← Dòng trống (CRLF)
 *   {"msg":"Check-in thành công"}        ← Body
 */
public class HttpResponseBuilder {

    private int statusCode = 200;
    private String statusText = "OK";
    private Map<String, String> headers = new LinkedHashMap<>();
    private String body = "";

    public HttpResponseBuilder() {
        headers.put("Server", "JavaChamCongServer/1.0");
        headers.put("Date", new java.util.Date().toString());
        headers.put("Connection", "close");
    }

    public HttpResponseBuilder status(int code) {
        this.statusCode = code;
        switch (code) {
            case 200: statusText = "OK"; break;
            case 201: statusText = "Created"; break;
            case 400: statusText = "Bad Request"; break;
            case 401: statusText = "Unauthorized"; break;
            case 403: statusText = "Forbidden"; break;
            case 404: statusText = "Not Found"; break;
            case 500: statusText = "Internal Server Error"; break;
            default:  statusText = "Unknown";
        }
        return this;
    }

    public HttpResponseBuilder header(String k, String v) {
        headers.put(k, v);
        return this;
    }

    public HttpResponseBuilder body(String b) {
        this.body = b == null ? "" : b;
        return this;
    }

    public HttpResponseBuilder json(String b) {
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return body(b);
    }

    /** Ghi response ra Socket theo đúng chuẩn HTTP/1.1 */
    public void writeTo(OutputStream out) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        headers.put("Content-Length", String.valueOf(bodyBytes.length));

        StringBuilder sb = new StringBuilder();

        // ===== STATUS LINE =====
        sb.append("HTTP/1.1 ").append(statusCode).append(" ")
          .append(statusText).append("\r\n");

        // ===== HEADERS =====
        for (Map.Entry<String, String> e : headers.entrySet()) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n");
        }

        // ===== DÒNG TRỐNG =====
        sb.append("\r\n");

        // Ghi: header + body
        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        out.write(bodyBytes);
        out.flush();
    }

    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
}
