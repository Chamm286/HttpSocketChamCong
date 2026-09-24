package com.httpchamcong.client;

import com.httpchamcong.util.JsonUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class ClientApp extends Application {

    private final SocketHttpClient http = new SocketHttpClient();
    private Stage primaryStage;
    private String fullName = "";
    private String role = "";

    private VBox contentArea;
    private Label pageTitle;
    private Label pageSubtitle;
    private Label clockLabel;
    private Button activeBtn;

    private TextField userField;
    private PasswordField passField;
    private Label loginError;

    // ===== DARK MODE PALETTE =====
    private static final String C_BG     = "#0a0e1a";
    private static final String C_CARD   = "#131829";
    private static final String C_CARD2  = "#1a2032";
    private static final String C_BORDER = "#1e2538";
    private static final String C_TEXT   = "#e2e8f0";
    private static final String C_MUTED  = "#94a3b8";
    private static final String C_ACCENT = "#14b8a6";
    private static final String C_GREEN  = "#10b981";
    private static final String C_ORANGE = "#f59e0b";
    private static final String C_BLUE   = "#3b82f6";
    private static final String C_PURPLE = "#8b5cf6";
    private static final String C_RED    = "#ef4444";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Hệ thống Chấm công");
        showLogin();
        stage.show();
    }

    // ============================================================
    // LOGIN
    // ============================================================
    private void showLogin() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0e1a, #0f172a, #042f2e);");

        VBox card = new VBox(0);
        card.setMaxWidth(440);
        card.setMaxHeight(680);
        card.setStyle(
            "-fx-background-color: " + C_CARD + "; " +
            "-fx-background-radius: 20; " +
            "-fx-border-color: " + C_BORDER + "; " +
            "-fx-border-radius: 20; " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 50, 0, 0, 20);"
        );

        // Header
        VBox header = new VBox(12);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(48, 40, 25, 40));

        StackPane logoCircle = new StackPane();
        logoCircle.setPrefSize(84, 84);
        logoCircle.setMaxSize(84, 84);
        logoCircle.setStyle("-fx-background-color: linear-gradient(to bottom right, #0d9488, #14b8a6); -fx-background-radius: 42;");

        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        logoIcon.setIconSize(38);
        logoIcon.setIconColor(Color.WHITE);
        logoCircle.getChildren().add(logoIcon);

        Label title = new Label("Chào mừng trở lại!");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(C_TEXT));
        title.setPadding(new Insets(14, 0, 0, 0));

        Label subtitle = new Label("Đăng nhập để bắt đầu chấm công");
        subtitle.setFont(Font.font("Segoe UI", 13.5));
        subtitle.setTextFill(Color.web(C_MUTED));

        // ⭐ IP Server info
        HBox serverBox = new HBox(8);
        serverBox.setAlignment(Pos.CENTER);
        serverBox.setPadding(new Insets(8, 16, 8, 16));
        serverBox.setStyle("-fx-background-color: #0d2a2a; -fx-background-radius: 16; -fx-border-color: #0d9488; -fx-border-radius: 16; -fx-border-width: 1;");

        FontIcon serverIcn = new FontIcon(FontAwesomeSolid.SERVER);
        serverIcn.setIconSize(12);
        serverIcn.setIconColor(Color.web(C_ACCENT));

        Label serverInfo = new Label("Server: " + SocketHttpClient.getServerInfo());
        serverInfo.setFont(Font.font("Consolas", FontWeight.BOLD, 11.5));
        serverInfo.setTextFill(Color.web(C_ACCENT));

        serverBox.getChildren().addAll(serverIcn, serverInfo);

        header.getChildren().addAll(logoCircle, title, subtitle, serverBox);

        // Form
        VBox form = new VBox(16);
        form.setPadding(new Insets(10, 45, 30, 45));

        Label userLabel = new Label("TÀI KHOẢN");
        userLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        userLabel.setTextFill(Color.web(C_MUTED));

        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 16, 0, 16));
        userBox.setPrefHeight(48);
        userBox.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-background-radius: 10; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 10; -fx-border-width: 1.5;");

        FontIcon userIcon = new FontIcon(FontAwesomeSolid.USER);
        userIcon.setIconSize(14);
        userIcon.setIconColor(Color.web(C_MUTED));

        userField = new TextField("nhanvien1");
        userField.setFont(Font.font("Segoe UI", 14));
        userField.setStyle("-fx-background-color: transparent; -fx-text-fill: " + C_TEXT + "; -fx-prompt-text-fill: " + C_MUTED + ";");
        HBox.setHgrow(userField, Priority.ALWAYS);

        userBox.getChildren().addAll(userIcon, userField);

        Label passLabel = new Label("MẬT KHẨU");
        passLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        passLabel.setTextFill(Color.web(C_MUTED));

        HBox passBox = new HBox(10);
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.setPadding(new Insets(0, 16, 0, 16));
        passBox.setPrefHeight(48);
        passBox.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-background-radius: 10; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 10; -fx-border-width: 1.5;");

        FontIcon passIcon = new FontIcon(FontAwesomeSolid.LOCK);
        passIcon.setIconSize(14);
        passIcon.setIconColor(Color.web(C_MUTED));

        passField = new PasswordField();
        passField.setText("Nhanvien1@");
        passField.setFont(Font.font("Segoe UI", 14));
        passField.setStyle("-fx-background-color: transparent; -fx-text-fill: " + C_TEXT + "; -fx-prompt-text-fill: " + C_MUTED + ";");
        HBox.setHgrow(passField, Priority.ALWAYS);

        passBox.getChildren().addAll(passIcon, passField);

        Button loginBtn = new Button("ĐĂNG NHẬP");
        loginBtn.setPrefHeight(50);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #0d9488, #14b8a6); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #0f766e, #0d9488); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #0d9488, #14b8a6); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;"));
        loginBtn.setOnAction(e -> doLogin());

        loginError = new Label("");
        loginError.setTextFill(Color.web(C_RED));
        loginError.setFont(Font.font("Segoe UI", 12));
        loginError.setWrapText(true);

        form.getChildren().addAll(userLabel, userBox, passLabel, passBox, loginBtn, loginError);

        // Footer
        VBox footer = new VBox(8);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(22, 40, 32, 40));
        footer.setStyle("-fx-background-color: #0d1424; -fx-background-radius: 0 0 20 20; -fx-border-color: " + C_BORDER + "; -fx-border-width: 1 0 0 0;");

        Label h1 = new Label("TÀI KHOẢN DEMO");
        h1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        h1.setTextFill(Color.web(C_MUTED));

        HBox h2Box = new HBox(6);
        h2Box.setAlignment(Pos.CENTER);
        FontIcon uIcn = new FontIcon(FontAwesomeSolid.USER);
        uIcn.setIconSize(11);
        uIcn.setIconColor(Color.web(C_MUTED));
        Label h2 = new Label("nhanvien1  /  Nhanvien1@");
        h2.setFont(Font.font("Consolas", 11.5));
        h2.setTextFill(Color.web(C_MUTED));
        h2Box.getChildren().addAll(uIcn, h2);

        HBox h3Box = new HBox(6);
        h3Box.setAlignment(Pos.CENTER);
        FontIcon cIcn = new FontIcon(FontAwesomeSolid.CROWN);
        cIcn.setIconSize(11);
        cIcn.setIconColor(Color.web(C_ORANGE));
        Label h3 = new Label("chamnee    /  Chamnee@2025");
        h3.setFont(Font.font("Consolas", 11.5));
        h3.setTextFill(Color.web(C_MUTED));
        h3Box.getChildren().addAll(cIcn, h3);

        footer.getChildren().addAll(h1, h2Box, h3Box);

        card.getChildren().addAll(header, form, footer);
        root.getChildren().add(card);

        primaryStage.setScene(new Scene(root, 1000, 760));
    }

    // ============================================================
    // DASHBOARD
    // ============================================================
    private void showMain(String name, String r) {
        this.fullName = name;
        this.role = r;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + C_BG + ";");

        // ===== SIDEBAR =====
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-background-color: #0d1424; -fx-border-color: " + C_BORDER + "; -fx-border-width: 0 1 0 0;");

        HBox logoBox = new HBox(12);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(26, 22, 26, 22));

        StackPane logoCircle = new StackPane();
        logoCircle.setPrefSize(42, 42);
        logoCircle.setMaxSize(42, 42);
        logoCircle.setStyle("-fx-background-color: linear-gradient(to bottom right, #0d9488, #14b8a6); -fx-background-radius: 21;");
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        logoIcon.setIconSize(20);
        logoIcon.setIconColor(Color.WHITE);
        logoCircle.getChildren().add(logoIcon);

        VBox logoText = new VBox(1);
        Label lt1 = new Label("CHẤM CÔNG");
        lt1.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        lt1.setTextFill(Color.web(C_TEXT));
        Label lt2 = new Label("Online System");
        lt2.setFont(Font.font("Segoe UI", 10));
        lt2.setTextFill(Color.web(C_MUTED));
        logoText.getChildren().addAll(lt1, lt2);

        logoBox.getChildren().addAll(logoCircle, logoText);

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: " + C_BORDER + ";");

        VBox menu = new VBox(4);
        menu.setPadding(new Insets(16, 12, 16, 12));

        Label menuTitle = new Label("MENU CHÍNH");
        menuTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        menuTitle.setTextFill(Color.web(C_MUTED));
        menuTitle.setPadding(new Insets(0, 0, 10, 14));

        Button homeBtn = sbBtn(FontAwesomeSolid.HOME, "Trang chủ");
        Button inBtn = sbBtn(FontAwesomeSolid.CHECK_CIRCLE, "Chấm công vào");
        Button outBtn = sbBtn(FontAwesomeSolid.CLOCK, "Chấm công ra");
        Button histBtn = sbBtn(FontAwesomeSolid.LIST_ALT, "Lịch sử");
        Button statBtn = sbBtn(FontAwesomeSolid.CHART_BAR, "Thống kê");

        menu.getChildren().addAll(menuTitle, homeBtn, inBtn, outBtn, histBtn, statBtn);

        if ("ADMIN".equals(role)) {
            Label adminT = new Label("QUẢN TRỊ");
            adminT.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
            adminT.setTextFill(Color.web(C_MUTED));
            adminT.setPadding(new Insets(16, 0, 10, 14));

            Button adminBtn = sbBtn(FontAwesomeSolid.CROWN, "Quản lý tất cả");
            menu.getChildren().addAll(adminT, adminBtn);
            adminBtn.setOnAction(e -> { setActive(adminBtn); showAdmin(); });
        }

        homeBtn.setOnAction(e -> { setActive(homeBtn); showHome(); });
        inBtn.setOnAction(e -> { setActive(inBtn); showCheckIn(); });
        outBtn.setOnAction(e -> { setActive(outBtn); showCheckOut(); });
        histBtn.setOnAction(e -> { setActive(histBtn); showHistory(); });
        statBtn.setOnAction(e -> { setActive(statBtn); showStats(); });

        VBox.setVgrow(menu, Priority.ALWAYS);

        VBox userBox = new VBox(10);
        userBox.setPadding(new Insets(16, 22, 22, 22));
        userBox.setStyle("-fx-border-color: " + C_BORDER + "; -fx-border-width: 1 0 0 0;");

        HBox ua = new HBox(10);
        ua.setAlignment(Pos.CENTER_LEFT);
        StackPane avCircle = new StackPane();
        avCircle.setPrefSize(38, 38);
        avCircle.setMaxSize(38, 38);
        avCircle.setStyle("-fx-background-color: " + C_ACCENT + "; -fx-background-radius: 19;");
        Label avText = new Label(name.substring(0, 1).toUpperCase());
        avText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        avText.setTextFill(Color.WHITE);
        avCircle.getChildren().add(avText);

        VBox ui = new VBox(0);
        Label un = new Label(name);
        un.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        un.setTextFill(Color.web(C_TEXT));
        Label ur = new Label(role);
        ur.setFont(Font.font("Consolas", 9));
        ur.setTextFill(Color.web(C_MUTED));
        ui.getChildren().addAll(un, ur);
        ua.getChildren().addAll(avCircle, ui);

        Button logoutBtn = new Button("  Đăng xuất");
        FontIcon outIcn = new FontIcon(FontAwesomeSolid.SIGN_OUT_ALT);
        outIcn.setIconSize(13);
        outIcn.setIconColor(Color.web(C_RED));
        logoutBtn.setGraphic(outIcn);
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setPrefHeight(38);
        logoutBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        logoutBtn.setStyle("-fx-background-color: " + C_CARD + "; -fx-text-fill: #f87171; -fx-background-radius: 8; -fx-cursor: hand; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 8;");
        logoutBtn.setOnAction(e -> { http.setToken(null); showLogin(); });

        userBox.getChildren().addAll(ua, logoutBtn);
        sidebar.getChildren().addAll(logoBox, divider, menu, userBox);

        // ===== HEADER =====
        HBox header = new HBox(15);
        header.setPadding(new Insets(20, 32, 20, 32));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + C_CARD + "; -fx-border-color: " + C_BORDER + "; -fx-border-width: 0 0 1 0;");

        VBox headerText = new VBox(3);
        pageTitle = new Label("Trang chủ");
        pageTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        pageTitle.setTextFill(Color.web(C_TEXT));

        pageSubtitle = new Label("Tổng quan");
        pageSubtitle.setFont(Font.font("Segoe UI", 13));
        pageSubtitle.setTextFill(Color.web(C_MUTED));
        headerText.getChildren().addAll(pageTitle, pageSubtitle);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox clockBox = new HBox(8);
        clockBox.setAlignment(Pos.CENTER);
        clockBox.setPadding(new Insets(9, 18, 9, 18));
        clockBox.setStyle("-fx-background-color: #0d2a2a; -fx-background-radius: 20; -fx-border-color: #0d9488; -fx-border-radius: 20; -fx-border-width: 1;");

        FontIcon clockIcn = new FontIcon(FontAwesomeSolid.CLOCK);
        clockIcn.setIconSize(13);
        clockIcn.setIconColor(Color.web(C_ACCENT));

        clockLabel = new Label("--:--:--");
        clockLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 15));
        clockLabel.setTextFill(Color.web(C_ACCENT));

        clockBox.getChildren().addAll(clockIcn, clockLabel);

        header.getChildren().addAll(headerText, sp, clockBox);

        // ===== CONTENT =====
        contentArea = new VBox(28);
        contentArea.setPadding(new Insets(32, 40, 32, 40));

        ScrollPane scroll = new ScrollPane(contentArea);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + C_BG + "; -fx-background-color: " + C_BG + ";");

        root.setLeft(sidebar);
        root.setTop(header);
        root.setCenter(scroll);

        setActive(homeBtn);
        showHome();
        startClock();

        primaryStage.setScene(new Scene(root, 1400, 850));
    }

    private Button sbBtn(FontAwesomeSolid icon, String text) {
        Button btn = new Button("  " + text);
        FontIcon icn = new FontIcon(icon);
        icn.setIconSize(14);
        icn.setIconColor(Color.web(C_MUTED));
        btn.setGraphic(icn);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(44);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("Segoe UI", 13.5));
        btn.setPadding(new Insets(0, 0, 0, 16));
        setInactive(btn);
        btn.setOnMouseEntered(e -> {
            if (!btn.equals(activeBtn)) {
                setHover(btn);
                icn.setIconColor(Color.web(C_TEXT));
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.equals(activeBtn)) {
                setInactive(btn);
                icn.setIconColor(Color.web(C_MUTED));
            }
        });
        return btn;
    }

    private void setInactive(Button btn) {
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + C_MUTED + "; -fx-background-radius: 8; -fx-cursor: hand;");
    }

    private void setHover(Button btn) {
        btn.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-text-fill: " + C_TEXT + "; -fx-background-radius: 8; -fx-cursor: hand;");
    }

    private void setActive(Button btn) {
        if (activeBtn != null) {
            setInactive(activeBtn);
            if (activeBtn.getGraphic() instanceof FontIcon) {
                ((FontIcon) activeBtn.getGraphic()).setIconColor(Color.web(C_MUTED));
            }
        }
        if (btn == null) { activeBtn = null; return; }
        btn.setStyle("-fx-background-color: " + C_ACCENT + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
        if (btn.getGraphic() instanceof FontIcon) {
            ((FontIcon) btn.getGraphic()).setIconColor(Color.WHITE);
        }
        activeBtn = btn;
    }

    // ============================================================
    // HOME — Layout cân đối
    // ============================================================
    private void showHome() {
        pageTitle.setText("Trang chủ");
        pageSubtitle.setText("Tổng quan hệ thống chấm công");
        contentArea.getChildren().clear();

        // Welcome card
        HBox welcome = new HBox(20);
        welcome.setPadding(new Insets(32, 36, 32, 36));
        welcome.setAlignment(Pos.CENTER_LEFT);
        welcome.setMaxWidth(Double.MAX_VALUE);
        welcome.setStyle("-fx-background-color: linear-gradient(to right, #0d9488, #14b8a6); -fx-background-radius: 18;");

        VBox info = new VBox(10);
        Label greet = new Label("Xin chào, " + fullName + "! 👋");
        greet.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        greet.setTextFill(Color.WHITE);

        Label date = new Label(getTodayString());
        date.setFont(Font.font("Segoe UI", 15));
        date.setTextFill(Color.web("#ccfbf1"));

        info.getChildren().addAll(greet, date);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox statusBox = new HBox(8);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setPadding(new Insets(12, 24, 12, 24));
        statusBox.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 24;");

        FontIcon dot = new FontIcon(FontAwesomeSolid.CIRCLE);
        dot.setIconSize(11);
        dot.setIconColor(Color.web("#4ade80"));

        Label statusLbl = new Label("ONLINE");
        statusLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        statusLbl.setTextFill(Color.WHITE);

        statusBox.getChildren().addAll(dot, statusLbl);

        welcome.getChildren().addAll(info, sp, statusBox);

        // Stats
        Label st = new Label("Thống kê nhanh");
        st.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        st.setTextFill(Color.web(C_TEXT));

        HBox stats = new HBox(20);
        stats.setMaxWidth(Double.MAX_VALUE);

        VBox s1 = miniCard(FontAwesomeSolid.CALENDAR_DAY, "Hôm nay", getTodayShort(), C_BLUE);
        VBox s2 = miniCard(FontAwesomeSolid.CHECK_CIRCLE, "Check-in", "Sẵn sàng", C_GREEN);
        VBox s3 = miniCard(FontAwesomeSolid.CLOCK, "Check-out", "Chưa check-out", C_ORANGE);
        VBox s4 = miniCard(FontAwesomeSolid.LIST_ALT, "Lịch sử", "Xem chi tiết", C_PURPLE);

        HBox.setHgrow(s1, Priority.ALWAYS);
        HBox.setHgrow(s2, Priority.ALWAYS);
        HBox.setHgrow(s3, Priority.ALWAYS);
        HBox.setHgrow(s4, Priority.ALWAYS);

        stats.getChildren().addAll(s1, s2, s3, s4);

        // Features
        Label ft = new Label("Chức năng chính");
        ft.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        ft.setTextFill(Color.web(C_TEXT));

        HBox feats = new HBox(20);
        feats.setMaxWidth(Double.MAX_VALUE);

        VBox f1 = bigCard(FontAwesomeSolid.CHECK_CIRCLE, "CHECK-IN", "Bắt đầu làm việc", C_GREEN, () -> { setActive(null); showCheckIn(); });
        VBox f2 = bigCard(FontAwesomeSolid.CLOCK, "CHECK-OUT", "Kết thúc làm việc", C_ORANGE, () -> { setActive(null); showCheckOut(); });
        VBox f3 = bigCard(FontAwesomeSolid.LIST_ALT, "LỊCH SỬ", "Xem chấm công", C_BLUE, () -> { setActive(null); showHistory(); });

        HBox.setHgrow(f1, Priority.ALWAYS);
        HBox.setHgrow(f2, Priority.ALWAYS);
        HBox.setHgrow(f3, Priority.ALWAYS);

        feats.getChildren().addAll(f1, f2, f3);

        contentArea.getChildren().addAll(welcome, st, stats, ft, feats);
    }

    private VBox miniCard(FontAwesomeSolid icon, String title, String value, String color) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(24, 26, 24, 26));
        card.setMinWidth(220);
        card.setPrefHeight(130);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 14; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 14; -fx-border-width: 1;");

        HBox top = new HBox(12);
        top.setAlignment(Pos.CENTER_LEFT);

        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(38, 38);
        iconCircle.setMaxSize(38, 38);
        iconCircle.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 19;");

        FontIcon icn = new FontIcon(icon);
        icn.setIconSize(18);
        icn.setIconColor(Color.web(color));
        iconCircle.getChildren().add(icn);

        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        t.setTextFill(Color.web(C_MUTED));

        top.getChildren().addAll(iconCircle, t);

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        v.setTextFill(Color.web(color));

        card.getChildren().addAll(top, v);
        return card;
    }

    private VBox bigCard(FontAwesomeSolid icon, String title, String desc, String color, Runnable onClick) {
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(260);
        card.setPrefHeight(180);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 16; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 16; -fx-border-width: 1; -fx-cursor: hand;");

        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(68, 68);
        iconCircle.setMaxSize(68, 68);
        iconCircle.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 34;");

        FontIcon icn = new FontIcon(icon);
        icn.setIconSize(30);
        icn.setIconColor(Color.web(color));
        iconCircle.getChildren().add(icn);

        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        t.setTextFill(Color.web(color));
        Label d = new Label(desc);
        d.setFont(Font.font("Segoe UI", 13));
        d.setTextFill(Color.web(C_MUTED));

        card.getChildren().addAll(iconCircle, t, d);
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-background-radius: 16; -fx-border-color: " + color + "; -fx-border-radius: 16; -fx-border-width: 1; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, " + color + "55, 24, 0, 0, 0);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 16; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 16; -fx-border-width: 1; -fx-cursor: hand;"));
        card.setOnMouseClicked(e -> onClick.run());
        return card;
    }

    // ============================================================
    // CHECK-IN / CHECK-OUT
    // ============================================================
    private void showCheckIn() {
        pageTitle.setText("Chấm công vào");
        pageSubtitle.setText("Xác nhận giờ vào làm việc");
        contentArea.getChildren().clear();

        VBox wrapper = new VBox();
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.getChildren().add(
            buildActionCard(FontAwesomeSolid.CHECK_CIRCLE, "Chấm công vào",
                "Bấm nút bên dưới để ghi nhận giờ vào làm việc.\nThời gian sẽ do máy chủ quyết định.",
                "XÁC NHẬN CHECK-IN",
                "linear-gradient(to right, #10b981, #059669)",
                C_GREEN, "checkin")
        );
        contentArea.getChildren().add(wrapper);
    }

    private void showCheckOut() {
        pageTitle.setText("Chấm công ra");
        pageSubtitle.setText("Xác nhận giờ ra về");
        contentArea.getChildren().clear();

        VBox wrapper = new VBox();
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.getChildren().add(
            buildActionCard(FontAwesomeSolid.CLOCK, "Chấm công ra",
                "Bấm nút bên dưới để ghi nhận giờ ra về.\nBạn phải check-in trước đó.",
                "XÁC NHẬN CHECK-OUT",
                "linear-gradient(to right, #f59e0b, #d97706)",
                C_ORANGE, "checkout")
        );
        contentArea.getChildren().add(wrapper);
    }

    private VBox buildActionCard(FontAwesomeSolid icon, String titleStr, String descStr,
                                  String btnStr, String btnColor, String accentColor,
                                  String api) {
        VBox card = new VBox(24);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(560);
        card.setMaxWidth(560);
        card.setPadding(new Insets(50));
        card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 18; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 18; -fx-border-width: 1;");

        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(110, 110);
        iconCircle.setMaxSize(110, 110);
        iconCircle.setStyle("-fx-background-color: " + accentColor + "22; -fx-background-radius: 55;");

        FontIcon icn = new FontIcon(icon);
        icn.setIconSize(50);
        icn.setIconColor(Color.web(accentColor));
        iconCircle.getChildren().add(icn);

        Label title = new Label(titleStr);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(C_TEXT));

        Label desc = new Label(descStr);
        desc.setFont(Font.font("Segoe UI", 14));
        desc.setTextFill(Color.web(C_MUTED));
        desc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label clock = new Label("--:--:--");
        clock.setFont(Font.font("Consolas", FontWeight.BOLD, 60));
        clock.setTextFill(Color.web(accentColor));

        Thread t = new Thread(() -> {
            while (true) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
                Platform.runLater(() -> {
                    java.time.LocalTime now = java.time.LocalTime.now();
                    clock.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
                });
            }
        });
        t.setDaemon(true);
        t.start();

        Button btn = new Button(btnStr);
        btn.setPrefHeight(58);
        btn.setPrefWidth(360);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btn.setStyle("-fx-background-color: " + btnColor + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand;");

        Label result = new Label("");
        result.setFont(Font.font("Consolas", 14));
        result.setWrapText(true);
        result.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        btn.setOnAction(e -> doCheck(api, result));

        card.getChildren().addAll(iconCircle, title, desc, clock, btn, result);
        return card;
    }

    private void doCheck(String action, Label result) {
        result.setText("⏳ Đang xử lý...");
        result.setTextFill(Color.web(C_MUTED));
        new Thread(() -> {
            try {
                String res = http.post("/api/" + action, "{}");
                String msg = JsonUtil.getString(res, "msg");
                String err = JsonUtil.getString(res, "error");
                String time = JsonUtil.getString(res, "time");
                Platform.runLater(() -> {
                    if (msg != null) {
                        result.setText("✅ " + msg + (time != null ? " — " + time : ""));
                        result.setTextFill(Color.web(C_GREEN));
                    } else {
                        result.setText("❌ " + (err != null ? err : res));
                        result.setTextFill(Color.web(C_RED));
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    result.setText("❌ Lỗi: " + ex.getMessage());
                    result.setTextFill(Color.web(C_RED));
                });
            }
        }).start();
    }

    // ============================================================
    // HISTORY
    // ============================================================
    public static class HistRow {
        public String date, in, out, total, status;
        public HistRow(String d, String i, String o, String t, String s) {
            date = d; in = i; out = o; total = t; status = s;
        }
    }

    private void showHistory() {
        pageTitle.setText("Lịch sử chấm công");
        pageSubtitle.setText("Danh sách chấm công của bạn");
        contentArea.getChildren().clear();

        VBox card = new VBox(20);
        card.setPadding(new Insets(28));
        card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 16; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 16; -fx-border-width: 1;");

        HBox hdr = new HBox(15);
        hdr.setAlignment(Pos.CENTER_LEFT);

        FontIcon tIcn = new FontIcon(FontAwesomeSolid.LIST_ALT);
        tIcn.setIconSize(18);
        tIcn.setIconColor(Color.web(C_ACCENT));

        Label title = new Label("  Danh sách chấm công");
        title.setGraphic(tIcn);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(C_TEXT));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button refresh = new Button("  Làm mới");
        FontIcon rIcn = new FontIcon(FontAwesomeSolid.SYNC_ALT);
        rIcn.setIconSize(13);
        rIcn.setIconColor(Color.web(C_ACCENT));
        refresh.setGraphic(rIcn);
        refresh.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        refresh.setStyle("-fx-background-color: #0d2a2a; -fx-text-fill: " + C_ACCENT + "; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20 10 20; -fx-border-color: #0d9488; -fx-border-radius: 8; -fx-border-width: 1;");
        refresh.setOnAction(e -> showHistory());

        hdr.getChildren().addAll(title, sp, refresh);

        TableView<HistRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(540);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setStyle("-fx-background-color: " + C_BG + "; -fx-control-inner-background: " + C_BG + "; -fx-table-cell-border-color: " + C_BORDER + ";");

        TableColumn<HistRow, String> c1 = new TableColumn<>("📅 Ngày");
        c1.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().date));
        c1.setPrefWidth(180);

        TableColumn<HistRow, String> c2 = new TableColumn<>("⏰ Giờ vào");
        c2.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().in));
        c2.setPrefWidth(160);

        TableColumn<HistRow, String> c3 = new TableColumn<>("🕐 Giờ ra");
        c3.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().out));
        c3.setPrefWidth(160);

        TableColumn<HistRow, String> c4 = new TableColumn<>("⏱️ Tổng giờ");
        c4.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().total));
        c4.setPrefWidth(200);

        TableColumn<HistRow, String> c5 = new TableColumn<>("📊 Trạng thái");
        c5.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().status));
        c5.setPrefWidth(220);
        c5.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    if (item.contains("Đủ giờ")) setStyle("-fx-text-fill: " + C_GREEN + "; -fx-font-weight: bold;");
                    else if (item.contains("Chưa")) setStyle("-fx-text-fill: " + C_ORANGE + "; -fx-font-weight: bold;");
                    else if (item.contains("muộn")) setStyle("-fx-text-fill: " + C_RED + "; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().addAll(c1, c2, c3, c4, c5);

        Label loading = new Label("⏳ Đang tải dữ liệu...");
        loading.setFont(Font.font("Segoe UI", 13));
        loading.setTextFill(Color.web(C_MUTED));

        card.getChildren().addAll(hdr, loading, table);
        contentArea.getChildren().add(card);

        new Thread(() -> {
            try {
                String res = http.get("/api/history");
                ObservableList<HistRow> rows = parseHistory(res);
                Platform.runLater(() -> {
                    card.getChildren().remove(loading);
                    table.setItems(rows);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> loading.setText("❌ Lỗi: " + ex.getMessage()));
            }
        }).start();
    }

    // ============================================================
    // STATS
    // ============================================================
    private void showStats() {
        pageTitle.setText("Thống kê");
        pageSubtitle.setText("Số liệu chấm công của bạn");
        contentArea.getChildren().clear();

        VBox card = new VBox(24);
        card.setPadding(new Insets(32));
        card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 16; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 16; -fx-border-width: 1;");

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        FontIcon icn = new FontIcon(FontAwesomeSolid.CHART_BAR);
        icn.setIconSize(18);
        icn.setIconColor(Color.web(C_PURPLE));
        Label title = new Label("  Thống kê cá nhân");
        title.setGraphic(icn);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web(C_TEXT));
        titleRow.getChildren().add(title);

        HBox row = new HBox(20);
        row.setMaxWidth(Double.MAX_VALUE);

        VBox s1 = statBig(FontAwesomeSolid.CALENDAR_DAY, "Tổng ngày", "...", C_BLUE);
        VBox s2 = statBig(FontAwesomeSolid.CHECK_CIRCLE, "Check-in", "...", C_GREEN);
        VBox s3 = statBig(FontAwesomeSolid.CLOCK, "Check-out", "...", C_ORANGE);
        VBox s4 = statBig(FontAwesomeSolid.EXCLAMATION_TRIANGLE, "Đi muộn", "...", C_RED);

        HBox.setHgrow(s1, Priority.ALWAYS);
        HBox.setHgrow(s2, Priority.ALWAYS);
        HBox.setHgrow(s3, Priority.ALWAYS);
        HBox.setHgrow(s4, Priority.ALWAYS);

        row.getChildren().addAll(s1, s2, s3, s4);

        card.getChildren().addAll(titleRow, row);
        contentArea.getChildren().add(card);

        new Thread(() -> {
            try {
                String res = http.get("/api/history");
                ObservableList<HistRow> rows = parseHistory(res);
                Platform.runLater(() -> {
                    int totalDays = rows.size(), checkins = 0, checkouts = 0, late = 0;
                    for (HistRow r : rows) {
                        if (!"—".equals(r.in)) checkins++;
                        if (!"—".equals(r.out)) checkouts++;
                        if (r.status.contains("muộn")) late++;
                    }
                    row.getChildren().clear();
                    VBox a = statBig(FontAwesomeSolid.CALENDAR_DAY, "Tổng ngày", String.valueOf(totalDays), C_BLUE);
                    VBox b = statBig(FontAwesomeSolid.CHECK_CIRCLE, "Check-in", String.valueOf(checkins), C_GREEN);
                    VBox c = statBig(FontAwesomeSolid.CLOCK, "Check-out", String.valueOf(checkouts), C_ORANGE);
                    VBox d = statBig(FontAwesomeSolid.EXCLAMATION_TRIANGLE, "Đi muộn", String.valueOf(late), C_RED);
                    HBox.setHgrow(a, Priority.ALWAYS);
                    HBox.setHgrow(b, Priority.ALWAYS);
                    HBox.setHgrow(c, Priority.ALWAYS);
                    HBox.setHgrow(d, Priority.ALWAYS);
                    row.getChildren().addAll(a, b, c, d);
                });
            } catch (Exception ex) { }
        }).start();
    }

    private VBox statBig(FontAwesomeSolid icon, String title, String value, String color) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(26));
        card.setMinWidth(220);
        card.setPrefHeight(140);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: " + C_BG + "; -fx-background-radius: 12; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");

        HBox top = new HBox(12);
        top.setAlignment(Pos.CENTER_LEFT);

        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(38, 38);
        iconCircle.setMaxSize(38, 38);
        iconCircle.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 19;");

        FontIcon icn = new FontIcon(icon);
        icn.setIconSize(18);
        icn.setIconColor(Color.web(color));
        iconCircle.getChildren().add(icn);

        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13.5));
        t.setTextFill(Color.web(C_MUTED));
        top.getChildren().addAll(iconCircle, t);

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        v.setTextFill(Color.web(color));

        card.getChildren().addAll(top, v);
        return card;
    }

    // ============================================================
    // ADMIN
    // ============================================================
    public static class AdminRow {
        public String username, fullName, date, in, out, total, status;
        public AdminRow(String u, String f, String d, String i, String o, String t, String s) {
            username = u; fullName = f; date = d; in = i; out = o; total = t; status = s;
        }
    }

    private void showAdmin() {
        pageTitle.setText("Quản lý tất cả");
        pageSubtitle.setText("Xem chấm công của tất cả nhân viên");
        contentArea.getChildren().clear();

        VBox card = new VBox(20);
        card.setPadding(new Insets(28));
        card.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 16; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 16; -fx-border-width: 1;");

        HBox hdr = new HBox(15);
        hdr.setAlignment(Pos.CENTER_LEFT);

        FontIcon tIcn = new FontIcon(FontAwesomeSolid.CROWN);
        tIcn.setIconSize(18);
        tIcn.setIconColor(Color.web(C_ORANGE));

        Label title = new Label("  Bảng chấm công toàn công ty");
        title.setGraphic(tIcn);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(C_TEXT));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label countLabel = new Label("⏳ Đang tải...");
        countLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        countLabel.setTextFill(Color.web(C_ACCENT));
        countLabel.setStyle("-fx-background-color: #0d2a2a; -fx-background-radius: 20; -fx-padding: 8 18 8 18; -fx-border-color: #0d9488; -fx-border-radius: 20; -fx-border-width: 1;");

        Button refresh = new Button("  Làm mới");
        FontIcon rIcn = new FontIcon(FontAwesomeSolid.SYNC_ALT);
        rIcn.setIconSize(13);
        rIcn.setIconColor(Color.web(C_ACCENT));
        refresh.setGraphic(rIcn);
        refresh.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        refresh.setStyle("-fx-background-color: #0d2a2a; -fx-text-fill: " + C_ACCENT + "; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20 10 20; -fx-border-color: #0d9488; -fx-border-radius: 8; -fx-border-width: 1;");
        refresh.setOnAction(e -> showAdmin());

        hdr.getChildren().addAll(title, sp, countLabel, refresh);

        TextField search = new TextField();
        search.setPromptText("🔍  Tìm theo username hoặc họ tên...");
        search.setPrefHeight(46);
        search.setFont(Font.font("Segoe UI", 13));
        search.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-text-fill: " + C_TEXT + "; -fx-prompt-text-fill: " + C_MUTED + "; -fx-background-radius: 10; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-padding: 0 16 0 16;");

        TableView<AdminRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(540);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setStyle("-fx-background-color: " + C_BG + "; -fx-control-inner-background: " + C_BG + "; -fx-table-cell-border-color: " + C_BORDER + ";");

        TableColumn<AdminRow, String> c0 = new TableColumn<>("👤 Username");
        c0.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().username));
        c0.setPrefWidth(120);

        TableColumn<AdminRow, String> c1 = new TableColumn<>("📛 Họ và tên");
        c1.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().fullName));
        c1.setPrefWidth(210);

        TableColumn<AdminRow, String> c2 = new TableColumn<>("📅 Ngày");
        c2.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().date));
        c2.setPrefWidth(120);

        TableColumn<AdminRow, String> c3 = new TableColumn<>("⏰ Giờ vào");
        c3.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().in));
        c3.setPrefWidth(110);

        TableColumn<AdminRow, String> c4 = new TableColumn<>("🕐 Giờ ra");
        c4.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().out));
        c4.setPrefWidth(110);

        TableColumn<AdminRow, String> c5 = new TableColumn<>("⏱️ Tổng giờ");
        c5.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().total));
        c5.setPrefWidth(140);

        TableColumn<AdminRow, String> c6 = new TableColumn<>("📊 Trạng thái");
        c6.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().status));
        c6.setPrefWidth(170);
        c6.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    if (item.contains("Đủ giờ")) setStyle("-fx-text-fill: " + C_GREEN + "; -fx-font-weight: bold;");
                    else if (item.contains("Chưa")) setStyle("-fx-text-fill: " + C_ORANGE + "; -fx-font-weight: bold;");
                    else if (item.contains("muộn")) setStyle("-fx-text-fill: " + C_RED + "; -fx-font-weight: bold;");
                    else if (item.contains("Về sớm")) setStyle("-fx-text-fill: " + C_ORANGE + "; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().addAll(c0, c1, c2, c3, c4, c5, c6);

        card.getChildren().addAll(hdr, search, table);
        contentArea.getChildren().add(card);

        new Thread(() -> {
            try {
                String res = http.get("/api/admin/all");
                ObservableList<AdminRow> rows = parseAdmin(res);
                Platform.runLater(() -> {
                    table.setItems(rows);
                    countLabel.setText("📊 " + rows.size() + " bản ghi");
                    search.textProperty().addListener((obs, old, val) -> {
                        if (val == null || val.isEmpty()) {
                            table.setItems(rows);
                        } else {
                            String key = val.toLowerCase();
                            ObservableList<AdminRow> filtered = FXCollections.observableArrayList();
                            for (AdminRow r : rows) {
                                if (r.username.toLowerCase().contains(key) ||
                                    r.fullName.toLowerCase().contains(key)) {
                                    filtered.add(r);
                                }
                            }
                            table.setItems(filtered);
                        }
                    });
                });
            } catch (Exception ex) {
                Platform.runLater(() -> countLabel.setText("❌ " + ex.getMessage()));
            }
        }).start();
    }

    // ============================================================
    // PARSE
    // ============================================================
    private ObservableList<HistRow> parseHistory(String json) {
        ObservableList<HistRow> rows = FXCollections.observableArrayList();
        if (json == null || json.isEmpty()) return rows;
        int dStart = json.indexOf("\"data\"");
        if (dStart < 0) return rows;
        int aStart = json.indexOf('[', dStart);
        int aEnd = json.lastIndexOf(']');
        if (aStart < 0 || aEnd < 0) return rows;
        String arr = json.substring(aStart + 1, aEnd);
        String[] items = arr.split("\\},\\s*\\{");
        for (String item : items) {
            item = item.replace("{", "").replace("}", "");
            String d = extract(item, "date");
            String i = extract(item, "checkIn");
            String o = extract(item, "checkOut");
            if (d == null || d.isEmpty()) continue;
            if (i == null || i.isEmpty()) i = "—";
            if (o == null || o.isEmpty()) o = "—";
            String total = "—";
            String status = "Đã chấm công";
            if (!"—".equals(i) && !"—".equals(o)) {
                try {
                    String[] ip = i.split(":");
                    String[] op = o.split(":");
                    int iMin = Integer.parseInt(ip[0]) * 60 + Integer.parseInt(ip[1]);
                    int oMin = Integer.parseInt(op[0]) * 60 + Integer.parseInt(op[1]);
                    int diff = oMin - iMin;
                    total = String.format("%d giờ %02d phút", diff / 60, diff % 60);
                    if (iMin > 8 * 60) status = "⚠️ Đi muộn";
                    else if (oMin < 17 * 60) status = "⚠️ Về sớm";
                    else status = "✅ Đủ giờ";
                } catch (Exception e) { total = "—"; }
            } else if (!"—".equals(i)) {
                status = "⏳ Chưa check-out";
            } else {
                status = "❌ Chưa chấm công";
            }
            rows.add(new HistRow(d, i, o, total, status));
        }
        return rows;
    }

    private ObservableList<AdminRow> parseAdmin(String json) {
        ObservableList<AdminRow> rows = FXCollections.observableArrayList();
        if (json == null || json.isEmpty()) return rows;
        int dStart = json.indexOf("\"data\"");
        if (dStart < 0) return rows;
        int aStart = json.indexOf('[', dStart);
        int aEnd = json.lastIndexOf(']');
        if (aStart < 0 || aEnd < 0) return rows;
        String arr = json.substring(aStart + 1, aEnd);
        String[] items = arr.split("\\},\\s*\\{");
        for (String item : items) {
            item = item.replace("{", "").replace("}", "");
            String u = extract(item, "username");
            String f = extract(item, "fullName");
            String d = extract(item, "date");
            String i = extract(item, "checkIn");
            String o = extract(item, "checkOut");
            if (d == null || d.isEmpty()) continue;
            if (f == null || f.isEmpty()) f = u;
            if (i == null || i.isEmpty()) i = "—";
            if (o == null || o.isEmpty()) o = "—";
            String total = "—";
            String status = "Đã chấm công";
            if (!"—".equals(i) && !"—".equals(o)) {
                try {
                    String[] ip = i.split(":");
                    String[] op = o.split(":");
                    int iMin = Integer.parseInt(ip[0]) * 60 + Integer.parseInt(ip[1]);
                    int oMin = Integer.parseInt(op[0]) * 60 + Integer.parseInt(op[1]);
                    int diff = oMin - iMin;
                    total = String.format("%d giờ %02d phút", diff / 60, diff % 60);
                    if (iMin > 8 * 60) status = "⚠️ Đi muộn";
                    else if (oMin < 17 * 60) status = "⚠️ Về sớm";
                    else status = "✅ Đủ giờ";
                } catch (Exception e) { total = "—"; }
            } else if (!"—".equals(i)) {
                status = "⏳ Chưa check-out";
            } else {
                status = "❌ Chưa chấm công";
            }
            rows.add(new AdminRow(u, f, d, i, o, total, status));
        }
        return rows;
    }

    private String extract(String json, String key) {
        String pattern = "\"" + key + "\"";
        int i = json.indexOf(pattern);
        if (i < 0) return "";
        int colon = json.indexOf(':', i + pattern.length());
        if (colon < 0) return "";
        int start = json.indexOf('"', colon + 1);
        if (start < 0) return "";
        int end = json.indexOf('"', start + 1);
        if (end < 0) return "";
        return json.substring(start + 1, end);
    }

    // ============================================================
    // UTILS
    // ============================================================
    private String getTodayString() {
        java.time.LocalDate now = java.time.LocalDate.now();
        String[] days = {"Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật"};
        String d = days[now.getDayOfWeek().getValue() - 1];
        return "📅  " + d + ", ngày " + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
    }

    private String getTodayShort() {
        java.time.LocalDate now = java.time.LocalDate.now();
        return now.getDayOfMonth() + "/" + now.getMonthValue();
    }

    private void startClock() {
        Thread t = new Thread(() -> {
            while (true) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
                Platform.runLater(() -> {
                    java.time.LocalTime now = java.time.LocalTime.now();
                    if (clockLabel != null) {
                        clockLabel.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
                    }
                });
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void doLogin() {
        new Thread(() -> {
            try {
                String body = "{\"username\":\"" + userField.getText() +
                              "\",\"password\":\"" + passField.getText() + "\"}";
                String res = http.post("/api/login", body);
                String token = JsonUtil.getString(res, "token");
                if (token != null) {
                    http.setToken(token);
                    String r = JsonUtil.getString(res, "role");
                    String name = JsonUtil.getString(res, "fullName");
                    Platform.runLater(() -> showMain(name, r));
                } else {
                    String err = JsonUtil.getString(res, "error");
                    Platform.runLater(() -> loginError.setText("❌ " + (err != null ? err : "Đăng nhập thất bại")));
                }
            } catch (Exception ex) {
                Platform.runLater(() -> loginError.setText("❌ Lỗi kết nối: " + ex.getMessage()));
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}