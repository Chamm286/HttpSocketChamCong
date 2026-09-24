# 🕒 HttpSocketChamCong - Hệ thống Chấm công Trực tuyến

> **Đồ án Lập trình mạng** — Sử dụng giao thức **HTTP/1.1** trên nền **Socket TCP/IP** theo mô hình **Client-Server**, viết bằng ngôn ngữ **Java**.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.5-blue.svg)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 📋 Mục lục

- [Giới thiệu](#-giới-thiệu)
- [Tính năng](#-tính-năng)
- [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Cấu trúc project](#-cấu-trúc-project)
- [Cài đặt](#-cài-đặt)
- [Chạy chương trình](#-chạy-chương-trình)
- [API Endpoints](#-api-endpoints)
- [Tài khoản demo](#-tài-khoản-demo)
- [Demo 2 máy LAN](#-demo-2-máy-lan)
- [Tác giả](#-tác-giả)

---

## 🎯 Giới thiệu

Đây là **đồ án môn Lập trình mạng**, xây dựng **Hệ thống Chấm công trực tuyến** sử dụng:

- ✅ **Giao thức HTTP/1.1** — tự parse request, tự build response (KHÔNG dùng framework)
- ✅ **Socket TCP/IP** — `ServerSocket` + `Socket` thuần Java
- ✅ **Mô hình Client-Server** — Server đa luồng (20 threads)
- ✅ **JavaFX** — giao diện Desktop hiện đại (dark mode + icon chuyên nghiệp)
- ✅ **MySQL** — lưu trữ dữ liệu thật
- ✅ **JDBC** — kết nối database

### ⚠️ KHÔNG PHẢI WEB

Đây là **ứng dụng Desktop** — chạy bằng `java` command, không cần trình duyệt:

- ❌ Không dùng Tomcat / Jetty / Spring Boot
- ❌ Không có HTML / CSS web / JavaScript
- ✅ Tự code HTTP server bằng Java
- ✅ Giao diện JavaFX desktop

---

## ✨ Tính năng

### 👤 Nhân viên (USER)

- 🔐 Đăng nhập với token (Bearer authentication)
- ✅ **Chấm công vào** (Check-in) — thời gian do server ghi
- 🕐 **Chấm công ra** (Check-out)
- 📋 Xem **lịch sử chấm công** cá nhân
- 📊 Xem **thống kê** (tổng ngày, số lần check-in/out, đi muộn)

### 👑 Quản trị viên (ADMIN)

- 🎁 Tất cả quyền của nhân viên
- 👥 Xem **bảng chấm công toàn công ty**
- 🔍 **Tìm kiếm** theo username hoặc họ tên
- 📊 **Bảng TableView** có cột: Username, Họ tên, Ngày, Giờ vào, Giờ ra, Tổng giờ, Trạng thái

### 🖥️ Server Dashboard

- ▶️ **START / STOP** server
- 📊 **Thống kê realtime** (client online, tổng request, uptime)
- 👥 **Bảng Clients** — IP, Port, Login time, Last activity, Requests, Status
- 📜 **Live Log** — log sự kiện realtime
- 🔗 **Danh sách Endpoints**

---

## 🏗️ Kiến trúc hệ thống

```
┌──────────────────────────┐              ┌──────────────────────────┐
│  CLIENT (JavaFX)         │              │  SERVER (JavaFX)         │
│  Nhân viên               │              │  Admin                   │
│                          │              │                          │
│  ┌────────────────┐      │   HTTP/1.1   │  ┌────────────────┐      │
│  │ Login          │      │  Socket TCP  │  │ Server Control │      │
│  │ Check-In/Out   │ ─────┼─────────────▶│  │ Dashboard      │      │
│  │ History/Stats  │      │   Port 2020  │  │ Live Logs      │      │
│  └────────────────┘      │◀─────┬───────│  │ Online Clients │      │
│                          │      │       │  └────────────────┘      │
└──────────────────────────┘      │       └──────────────────────────┘
                                  │                     │
                                  │                     │ JDBC
                                  │                     ▼
                                  │          ┌──────────────────────┐
                                  │          │  MySQL Database      │
                                  │          │  chamcong_db         │
                                  │          │    - users           │
                                  │          │    - chamcong        │
                                  │          └──────────────────────┘
                                  │
                          Socket TCP/IP port 2020
```

### Luồng hoạt động

1. **Client** mở `Socket` kết nối tới `Server` (port 2020)
2. Client **tự build HTTP request** thủ công: `POST /api/checkin HTTP/1.1` + Headers + Body
3. **Server** `ServerSocket.accept()` → tạo Thread mới (ExecutorService)
4. Server **tự parse HTTP request**: Request Line → Headers → Body
5. Server xử lý → **tự build HTTP response** → ghi ra socket
6. Client đọc HTTP response → hiển thị kết quả

---

## 🛠️ Công nghệ sử dụng

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|----------|
| **Java** | 17+ | Ngôn ngữ chính |
| **JavaFX** | 21.0.5 | Giao diện Desktop |
| **Ikonli** | 12.3.1 | Icon Font Awesome |
| **MySQL** | 8.0+ | Database |
| **MySQL Connector/J** | 8.4.0+ | JDBC driver |
| **XAMPP** | 8.2+ | Chạy MySQL local |

---

## 📁 Cấu trúc project

```
HttpSocketChamCong/
│
├── .vscode/
│   ├── settings.json              ← Cấu hình Java cho VS Code
│   └── launch.json                ← Cấu hình Run Server/Client
│
├── database/
│   └── chamcong_db.sql            ← Script tạo database + dữ liệu mẫu
│
├── lib/
│   ├── mysql-connector-j-*.jar    ← JDBC driver
│   ├── ikonli-*.jar                ← Icon FontAwesome
│   └── javafx-sdk-21.0.5/         ← JavaFX SDK
│
├── src/
│   └── com/httpchamcong/
│       │
│       ├── model/                  ← Model classes
│       │   ├── User.java
│       │   └── ChamCong.java
│       │
│       ├── dao/                    ← Data Access Object
│       │   ├── DBConnection.java
│       │   ├── UserDAO.java
│       │   └── ChamCongDAO.java
│       │
│       ├── util/                   ← Tiện ích
│       │   ├── JsonUtil.java
│       │   └── SessionManager.java
│       │
│       ├── server/                 ← HTTP Server (Socket TCP)
│       │   ├── HttpSocketServer.java       ← ServerSocket chính
│       │   ├── ClientHandler.java          ← Mỗi client = 1 thread
│       │   ├── HttpRequestParser.java      ← Tự parse HTTP request
│       │   ├── HttpResponseBuilder.java    ← Tự build HTTP response
│       │   ├── Router.java                 ← Điều hướng URL
│       │   ├── ServerStats.java            ← Thống kê
│       │   ├── ClientManager.java          ← Quản lý clients
│       │   ├── ServerApp.java              ← Giao diện Server Dashboard
│       │   │
│       │   └── controller/
│       │       ├── AuthController.java
│       │       ├── ChamCongController.java
│       │       └── AdminController.java
│       │
│       └── client/                 ← HTTP Client
│           ├── SocketHttpClient.java       ← Client tự build HTTP request
│           └── ClientApp.java              ← Giao diện Client (JavaFX)
│
├── README.md                       ← File này
├── .gitignore                      ← Bỏ file rác khi push Git
└── LICENSE                         ← (Tùy chọn)
```

---

## ⚙️ Cài đặt

### Bước 1: Cài đặt môi trường

#### 1.1. JDK 17+

```bash
# Kiểm tra
java -version
javac -version
```

Nếu chưa có → tải **JDK 17**: https://adoptium.net/

#### 1.2. XAMPP (MySQL)

Tải: https://www.apachefriends.org/

- Cài đặt XAMPP
- Mở XAMPP Control Panel → **Start MySQL** (port 3307)
- **KHÔNG cần Start Apache/Tomcat**

#### 1.3. VS Code + Extensions

- **Extension Pack for Java** (Microsoft)

### Bước 2: Clone project

```bash
git clone https://github.com/<username>/HttpSocketChamCong.git
cd HttpSocketChamCong
```

### Bước 3: Tải thư viện (lib/)

Tải và copy vào `lib/`:

| Thư viện | Link | File cần |
|----------|------|----------|
| **MySQL Connector/J** | https://dev.mysql.com/downloads/connector/j/ | `mysql-connector-j-*.jar` |
| **JavaFX SDK 21.0.5** | https://gluonhq.com/products/javafx/ | Thư mục `javafx-sdk-21.0.5/` |
| **Ikonli** | https://repo1.maven.org/maven2/org/kordamp/ikonli/ | 4 file `ikonli-*.jar` |

**Chi tiết Ikonli** — tải 4 file:
- `ikonli-core-12.3.1.jar`
- `ikonli-javafx-12.3.1.jar`
- `ikonli-fontawesome5-pack-12.3.1.jar`
- `ikonli-fontawesome-pack-12.3.1.jar`

### Bước 4: Tạo Database

Mở **phpMyAdmin** (http://localhost/phpmyadmin) hoặc **MySQL Workbench**:

1. Chạy script `database/chamcong_db.sql`
2. Sẽ tạo database `chamcong_db` với 2 bảng:
   - `users` — tài khoản (6 users)
   - `chamcong` — lịch sử chấm công (177 bản ghi)

### Bước 5: Cấu hình kết nối

Mở `src/com/httpchamcong/dao/DBConnection.java`:

```java
private static final String URL =
    "jdbc:mysql://localhost:3307/chamcong_db"  // ⚠️ Port 3307 (XAMPP)
    + "?useUnicode=true"
    + "&characterEncoding=UTF-8"
    + "&serverTimezone=Asia/Ho_Chi_Minh"
    + "&useSSL=false"
    + "&allowPublicKeyRetrieval=true";

private static final String USER = "root";
private static final String PASSWORD = "";   // XAMPP mặc định rỗng
```

---

## 🚀 Chạy chương trình

### Bước 1: Compile

```bash
# Compile server core
javac -encoding UTF-8 -d bin -cp "lib/mysql-connector-j-26.7.0.jar" ^
    src\com\httpchamcong\model\User.java ^
    src\com\httpchamcong\model\ChamCong.java ^
    src\com\httpchamcong\util\JsonUtil.java ^
    src\com\httpchamcong\util\SessionManager.java ^
    src\com\httpchamcong\dao\DBConnection.java ^
    src\com\httpchamcong\dao\UserDAO.java ^
    src\com\httpchamcong\dao\ChamCongDAO.java ^
    src\com\httpchamcong\server\HttpRequestParser.java ^
    src\com\httpchamcong\server\HttpResponseBuilder.java ^
    src\com\httpchamcong\server\controller\AuthController.java ^
    src\com\httpchamcong\server\controller\ChamCongController.java ^
    src\com\httpchamcong\server\controller\AdminController.java ^
    src\com\httpchamcong\server\Router.java ^
    src\com\httpchamcong\server\ClientHandler.java ^
    src\com\httpchamcong\server\HttpSocketServer.java ^
    src\com\httpchamcong\server\ServerStats.java ^
    src\com\httpchamcong\server\ClientManager.java

# Compile UI (JavaFX + Ikonli)
javac -encoding UTF-8 -d bin --module-path "lib/javafx-sdk-21.0.5/lib" --add-modules javafx.controls,javafx.fxml ^
    -cp "bin;lib/mysql-connector-j-26.7.0.jar;lib/ikonli-core-12.3.1.jar;lib/ikonli-javafx-12.3.1.jar;lib/ikonli-fontawesome5-pack-12.3.1.jar;lib/ikonli-fontawesome-pack-12.3.1.jar" ^
    src\com\httpchamcong\client\SocketHttpClient.java ^
    src\com\httpchamcong\client\ClientApp.java ^
    src\com\httpchamcong\server\ServerApp.java
```

### Bước 2: Chạy Server

**Terminal 1:**

```bash
java --module-path "lib/javafx-sdk-21.0.5/lib" --add-modules javafx.controls,javafx.fxml ^
    -cp "bin;lib/mysql-connector-j-26.7.0.jar;lib/ikonli-core-12.3.1.jar;lib/ikonli-javafx-12.3.1.jar;lib/ikonli-fontawesome5-pack-12.3.1.jar;lib/ikonli-fontawesome-pack-12.3.1.jar" ^
    com.httpchamcong.server.ServerApp
```

→ Click **START SERVER** → Status 🟢 RUNNING

### Bước 3: Chạy Client

**Terminal 2 (mở mới):**

```bash
java --module-path "lib/javafx-sdk-21.0.5/lib" --add-modules javafx.controls,javafx.fxml ^
    -cp "bin;lib/mysql-connector-j-26.7.0.jar;lib/ikonli-core-12.3.1.jar;lib/ikonli-javafx-12.3.1.jar;lib/ikonli-fontawesome5-pack-12.3.1.jar;lib/ikonli-fontawesome-pack-12.3.1.jar" ^
    com.httpchamcong.client.ClientApp
```

→ Login → Chấm công

---

## 🌐 API Endpoints

| Method | Path | Mô tả | Auth |
|--------|------|-------|------|
| `POST` | `/api/login` | Đăng nhập, trả token | ❌ |
| `POST` | `/api/checkin` | Chấm công vào | ✅ |
| `POST` | `/api/checkout` | Chấm công ra | ✅ |
| `GET` | `/api/history` | Lịch sử chấm công | ✅ |
| `GET` | `/api/admin/all` | Admin xem tất cả | ✅ ADMIN |
| `GET` | `/api/ping` | Kiểm tra server | ❌ |
| `POST` | `/api/logout` | Đăng xuất | ✅ |

### Ví dụ HTTP Request thủ công

```http
POST /api/login HTTP/1.1
Host: localhost:2020
User-Agent: JavaChamCongClient/1.0
Accept: application/json
Content-Type: application/json; charset=UTF-8
Content-Length: 43
Connection: close

{"username":"chamnee","password":"Chamnee@2025"}
```

### Ví dụ HTTP Response

```http
HTTP/1.1 200 OK
Server: JavaChamCongServer/1.0
Date: Wed Sep 24 22:45:00 ICT 2026
Connection: close
Content-Type: application/json; charset=UTF-8
Content-Length: 145

{"token":"a1b2c3d4-...","user":{"username":"chamnee","fullName":"Nguyễn Thị Bích Trâm","role":"ADMIN"}}
```

---

## 🔑 Tài khoản demo

| Username | Password | Vai trò | Họ tên |
|----------|----------|---------|--------|
| **chamnee** | `Chamnee@2025` | **ADMIN** | Nguyễn Thị Bích Trâm |
| nhanvien1 | `Nhanvien1@` | USER | Nguyễn Văn An |
| nhanvien2 | `Nhanvien2@` | USER | Trần Thị Bình |
| nhanvien3 | `Nhanvien3@` | USER | Lê Hoàng Cường |
| nhanvien4 | `Nhanvien4@` | USER | Phạm Thị Dung |
| nhanvien5 | `Nhanvien5@` | USER | Võ Minh Đức |

---

## 🌐 Demo 2 máy LAN

### Điều kiện

- 2 máy **cùng mạng Wi-Fi (LAN)**
- Máy Server **START SERVER** trước
- Firewall máy Server **mở port 2020**

### Cách làm

#### Máy Server (máy Trâm):

1. Chạy ServerApp → **START SERVER**
2. Nhìn header: `🌐 192.168.1.100:2020` — **copy IP này**
3. Mở firewall port 2020 (hoặc tắt firewall tạm thời)

#### Máy Client (máy Tri):

1. **Copy project** sang máy Tri (USB / Google Drive)
2. Sửa `SocketHttpClient.java`:
   ```java
   // private static final String HOST = "localhost";  ← Comment dòng này
   private static final String HOST = "192.168.1.100";  ← IP máy Trâm
   ```
3. Compile lại:
   ```bash
   javac -encoding UTF-8 -d bin -cp "bin;lib/mysql-connector-j-26.7.0.jar" src\com\httpchamcong\client\SocketHttpClient.java
   ```
4. Chạy Client → Login → chấm công
5. **Server Dashboard** (máy Trâm) → tab **Clients** → thấy Tri với IP `192.168.1.105`

---

## 📸 Screenshots

### Client — Login (Dark Mode)

![Login](docs/screenshots/login.png)

### Client — Trang chủ

![Home](docs/screenshots/home.png)

### Server — Dashboard

![Server](docs/screenshots/server.png)

### Server — Clients

![Clients](docs/screenshots/clients.png)

---

## 🎓 Điểm nổi bật

Đồ án thể hiện các kiến thức **Lập trình mạng**:

- ✅ **Socket TCP/IP** — `ServerSocket`, `Socket`
- ✅ **Giao thức HTTP/1.1** — tự parse request, tự build response
- ✅ **Đa luồng** — `ExecutorService` (20 threads)
- ✅ **RESTful API** — routing theo URL
- ✅ **Authentication** — token Bearer
- ✅ **Database thật** — MySQL + JDBC
- ✅ **JavaFX CSS** — giao diện dark mode đẹp
- ✅ **Mô hình Client-Server** đầy đủ

---

## 📝 License

MIT License — xem file [LICENSE](LICENSE).

---

## 👤 Tác giả

**Nguyễn Thị Bích Trâm - 24IT277**
**Trần Văn Tri - 24IT283**
- 🎓 Sinh viên ngành Công nghệ Thông tin
- 📚 Môn: Lập trình mạng
- 📅 Năm học: 2026-2027

---

## 🙏 Cảm ơn

- Thầy ThS. Nguyễn Thanh Cẩm - Giảng viên hướng dẫn môn Lập trình mạng
- Cộng đồng Java Việt Nam

---

## 📞 Liên hệ

Nếu có thắc mắc → tạo [Issue](https://github.com/<username>/HttpSocketChamCong/issues) hoặc liên hệ trực tiếp.
