package com.httpchamcong.server;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Tự PARSE HTTP/1.1 REQUEST từ Socket InputStream.
 *
 * Format HTTP/1.1 Request:
 *   POST /api/login HTTP/1.1              ← Request line
 *   Host: localhost:8080                  ← Headers
 *   Authorization: Bearer xxx
 *   Content-Length: 42
 *                                         ← Dòng trống (CRLF)
 *   {"username":"abc","password":"123"}   ← Body
 */
public class HttpRequestParser {

    private String method;
    private String path;
    private String version;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params  = new HashMap<>();
    private String body = "";

    public static HttpRequestParser parse(InputStream in) throws IOException {
        HttpRequestParser req = new HttpRequestParser();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));

        // ===== BƯỚC 1: REQUEST LINE =====
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        String[] parts = requestLine.split(" ");
        if (parts.length < 3) return null;

        req.method  = parts[0];
        String fullPath = parts[1];
        req.version = parts[2];

        // Tách query string
        if (fullPath.contains("?")) {
            String[] ps = fullPath.split("\\?", 2);
            req.path = ps[0];
            for (String kv : ps[1].split("&")) {
                String[] p = kv.split("=", 2);
                if (p.length == 2) {
                    req.params.put(urlDecode(p[0]), urlDecode(p[1]));
                }
            }
        } else {
            req.path = fullPath;
        }

        // ===== BƯỚC 2: HEADERS =====
        String line;
        int contentLength = 0;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(':');
            if (idx > 0) {
                String k = line.substring(0, idx).trim();
                String v = line.substring(idx + 1).trim();
                req.headers.put(k, v);

                if (k.equalsIgnoreCase("Content-Length")) {
                    contentLength = Integer.parseInt(v);
                }
            }
        }

        // ===== BƯỚC 3: BODY =====
        if (contentLength > 0) {
            char[] buf = new char[contentLength];
            int read = 0;
            while (read < contentLength) {
                int r = reader.read(buf, read, contentLength - read);
                if (r == -1) break;
                read += r;
            }
            req.body = new String(buf, 0, read);
        }
        return req;
    }

    private static String urlDecode(String s) {
        try { return URLDecoder.decode(s, "UTF-8"); }
        catch (Exception e) { return s; }
    }

    public String getMethod() { return method; }
    public String getPath()   { return path; }
    public String getVersion(){ return version; }
    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getParams()  { return params; }
    public String getBody()   { return body; }

    /** Lấy token từ header Authorization: Bearer xxx */
    public String getBearerToken() {
        String auth = headers.get("Authorization");
        if (auth == null) return null;
        if (auth.startsWith("Bearer ")) return auth.substring(7);
        return auth;
    }

    @Override
    public String toString() {
        return method + " " + path + " " + version;
    }
} 
