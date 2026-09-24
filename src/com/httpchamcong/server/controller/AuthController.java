package com.httpchamcong.server.controller;

import com.httpchamcong.dao.UserDAO;
import com.httpchamcong.model.User;
import com.httpchamcong.server.HttpRequestParser;
import com.httpchamcong.server.HttpResponseBuilder;
import com.httpchamcong.util.JsonUtil;
import com.httpchamcong.util.SessionManager;

import java.util.UUID;

/**
 * Controller: Xử lý đăng nhập
 * POST /api/login
 *   Body: {"username":"chamnee","password":"Chamnee@2025"}
 *   Response: {"token":"...","user":{...}}
 */
public class AuthController {

    public HttpResponseBuilder login(HttpRequestParser req) {
        // Kiểm tra HTTP method
        if (!"POST".equalsIgnoreCase(req.getMethod())) {
            return new HttpResponseBuilder().status(400)
                .json("{\"error\":\"Method not allowed\"}");
        }

        // Lấy dữ liệu từ HTTP body (JSON)
        String username = JsonUtil.getString(req.getBody(), "username");
        String password = JsonUtil.getString(req.getBody(), "password");

        if (username == null || password == null) {
            return new HttpResponseBuilder().status(400)
                .json("{\"error\":\"Thiếu username hoặc password\"}");
        }

        // Xác thực với database
        User u = UserDAO.findUser(username, password);
        if (u == null) {
            return new HttpResponseBuilder().status(401)
                .json("{\"error\":\"Sai tài khoản hoặc mật khẩu\"}");
        }

        // Sinh token ngẫu nhiên, lưu session
        String token = UUID.randomUUID().toString();
        SessionManager.createSession(token, u);

        return new HttpResponseBuilder().status(200).json(
            "{\"token\":\"" + token + "\",\"user\":" + u.toJson() + "}"
        );
    }
} 
