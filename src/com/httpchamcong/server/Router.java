package com.httpchamcong.server;

import com.httpchamcong.model.User;
import com.httpchamcong.server.controller.AdminController;
import com.httpchamcong.server.controller.AuthController;
import com.httpchamcong.server.controller.ChamCongController;
import com.httpchamcong.util.SessionManager;

public class Router {
    private final AuthController auth = new AuthController();
    private final ChamCongController chamCong = new ChamCongController();
    private final AdminController admin = new AdminController();

    public HttpResponseBuilder handle(HttpRequestParser req) {
        String path = req.getPath();

        try {
            switch (path) {
                case "/api/login":     return auth.login(req);
                case "/api/checkin":   return chamCong.checkIn(req);
                case "/api/checkout":  return chamCong.checkOut(req);
                case "/api/history":   return chamCong.history(req);
                case "/api/admin/all": return admin.getAll(req);
                case "/api/ping":
                    return new HttpResponseBuilder().status(200)
                        .json("{\"msg\":\"pong\"}");
                case "/api/logout":
                    String tk = req.getBearerToken();
                    if (tk != null) {
                        User uu = SessionManager.getUser(tk);
                        if (uu != null) ClientManager.setOffline(uu.getUsername());
                        SessionManager.remove(tk);
                    }
                    return new HttpResponseBuilder().status(200)
                        .json("{\"msg\":\"Da dang xuat\"}");
                default:
                    return new HttpResponseBuilder().status(404)
                        .json("{\"error\":\"Endpoint khong ton tai\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponseBuilder().status(500)
                .json("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}