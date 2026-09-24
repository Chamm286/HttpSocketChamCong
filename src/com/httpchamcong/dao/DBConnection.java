 package com.httpchamcong.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3307/chamcong_db"
        + "?useUnicode=true"
        + "&characterEncoding=UTF-8"
        + "&serverTimezone=Asia/Ho_Chi_Minh"
        + "&useSSL=false"
        + "&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy MySQL Driver!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  TEST KẾT NỐI MYSQL");
        System.out.println("===========================================");
        System.out.println("URL: " + URL);
        System.out.println();

        try (Connection conn = getConnection()) {
            System.out.println("✅ Kết nối MySQL thành công!");
            System.out.println("   Database: " + conn.getCatalog());
            System.out.println("   Driver:   " + conn.getMetaData().getDriverName());
            System.out.println("   Version:  " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
