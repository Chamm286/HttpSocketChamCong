 package com.httpchamcong.dao;

import com.httpchamcong.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Người dùng
 * Thao tác bảng `users` trong MySQL
 */
public class UserDAO {

    /** Tìm user theo username + password (đăng nhập) */
    public static User findUser(String username, String password) {
        String sql = "SELECT username, password, full_name, role " +
                     "FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Tìm user theo username */
    public static User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Lấy tất cả users */
    public static List<User> loadAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Thêm user mới */
    public static boolean insert(User u) {
        String sql = "INSERT INTO users (username, password, full_name, role) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Test */
    public static void main(String[] args) {
        System.out.println("=== TEST UserDAO ===");
        System.out.println("\n1. Tất cả users:");
        List<User> users = loadAll();
        for (User u : users) {
            System.out.println("   - " + u);
        }

        System.out.println("\n2. Test đăng nhập 'chamnee' / 'Chamnee@2025':");
        User u = findUser("chamnee", "Chamnee@2025");
        if (u != null) {
            System.out.println("   ✅ Đăng nhập OK: " + u);
        } else {
            System.out.println("   ❌ Sai tài khoản/mật khẩu");
        }

        System.out.println("\n3. Test đăng nhập sai:");
        User wrong = findUser("chamnee", "sai123");
        System.out.println("   Kết quả: " + (wrong == null ? "✅ Từ chối đúng" : "❌ Có lỗi"));
    }
}
