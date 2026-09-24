package com.httpchamcong.server.controller;

import com.httpchamcong.dao.ChamCongDAO;
import com.httpchamcong.model.ChamCong;
import com.httpchamcong.model.User;
import com.httpchamcong.server.HttpRequestParser;
import com.httpchamcong.server.HttpResponseBuilder;
import com.httpchamcong.util.SessionManager;

import java.util.List;

public class AdminController {

    public HttpResponseBuilder getAll(HttpRequestParser req) {
        String token = req.getBearerToken();
        User u = SessionManager.getUser(token);

        if (u == null) {
            return new HttpResponseBuilder().status(401)
                .json("{\"error\":\"Chua dang nhap\"}");
        }

        if (!"ADMIN".equals(u.getRole())) {
            return new HttpResponseBuilder().status(403)
                .json("{\"error\":\"Khong co quyen truy cap\"}");
        }

        List<ChamCong> list = ChamCongDAO.loadAll();

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toJson());
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");

        return new HttpResponseBuilder().status(200).json(
            "{\"total\":" + list.size() + ",\"data\":" + sb + "}"
        );
    }
}