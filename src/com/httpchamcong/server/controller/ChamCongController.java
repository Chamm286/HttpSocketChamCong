package com.httpchamcong.server.controller;

import com.httpchamcong.dao.ChamCongDAO;
import com.httpchamcong.model.ChamCong;
import com.httpchamcong.model.User;
import com.httpchamcong.server.HttpRequestParser;
import com.httpchamcong.server.HttpResponseBuilder;
import com.httpchamcong.util.SessionManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller: Xử lý chấm công
 *   POST /api/checkin   — Check-in hôm nay
 *   POST /api/checkout  — Check-out hôm nay
 *   GET  /api/history   — Xem lịch sử chấm công
 */
public class ChamCongController {

    /** Xác thực token → User */
    private User auth(HttpRequestParser req) {
        String token = req.getBearerToken();
        return SessionManager.getUser(token);
    }

    // ============ CHECK-IN ============
    public HttpResponseBuilder checkIn(HttpRequestParser req) {
        User u = auth(req);
        if (u == null) {
            return new HttpResponseBuilder().status(401)
                .json("{\"error\":\"Chưa đăng nhập\"}");
        }

        // ⭐ Thời gian do SERVER quyết định (chống gian lận)
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        boolean ok = ChamCongDAO.checkIn(u.getUsername(), time);

        if (!ok) {
            return new HttpResponseBuilder().status(400)
                .json("{\"error\":\"Hôm nay đã check-in rồi\"}");
        }

        return new HttpResponseBuilder().status(200).json(
            "{\"msg\":\"Check-in thành công\",\"time\":\"" + time + "\"}"
        );
    }

    // ============ CHECK-OUT ============
    public HttpResponseBuilder checkOut(HttpRequestParser req) {
        User u = auth(req);
        if (u == null) {
            return new HttpResponseBuilder().status(401)
                .json("{\"error\":\"Chưa đăng nhập\"}");
        }

        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        boolean ok = ChamCongDAO.checkOut(u.getUsername(), time);

        if (!ok) {
            return new HttpResponseBuilder().status(400)
                .json("{\"error\":\"Chưa check-in hoặc đã check-out\"}");
        }

        return new HttpResponseBuilder().status(200).json(
            "{\"msg\":\"Check-out thành công\",\"time\":\"" + time + "\"}"
        );
    }

    // ============ HISTORY ============
    public HttpResponseBuilder history(HttpRequestParser req) {
        User u = auth(req);
        if (u == null) {
            return new HttpResponseBuilder().status(401)
                .json("{\"error\":\"Chưa đăng nhập\"}");
        }

        List<ChamCong> list = ChamCongDAO.findByUser(u.getUsername());

        // Build JSON array
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toJson());
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");

        return new HttpResponseBuilder().status(200).json(
            "{\"username\":\"" + u.getUsername() + "\",\"data\":" + sb + "}"
        );
    }
} 
