package com.httpchamcong.server;

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

public class ServerApp extends Application {

    private final HttpSocketServer server = new HttpSocketServer();

    private Label statusBadge;
    private Label activeClientsVal;
    private Label totalClientsVal;
    private Label totalRequestsVal;
    private Label uptimeVal;
    private Label serverIpLabel;
    private TextArea logArea;
    private Button startBtn;
    private Button stopBtn;

    private TableView<ClientManager.ClientInfo> clientTable;
    private ObservableList<ClientManager.ClientInfo> clientList;

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
    private static final String C_RED    = "#ef4444";

    @Override
    public void start(Stage stage) {
        stage.setTitle("Server Dashboard - HttpSocketChamCong");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + C_BG + ";");

        // ============ HEADER ============
        HBox header = new HBox(15);
        header.setPadding(new Insets(20, 32, 20, 32));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + C_CARD + "; -fx-border-color: " + C_BORDER + "; -fx-border-width: 0 0 1 0;");

        StackPane logoCircle = new StackPane();
        logoCircle.setPrefSize(44, 44);
        logoCircle.setMaxSize(44, 44);
        logoCircle.setStyle("-fx-background-color: linear-gradient(to bottom right, #0d9488, #14b8a6); -fx-background-radius: 22;");
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.SERVER);
        logoIcon.setIconSize(22);
        logoIcon.setIconColor(Color.WHITE);
        logoCircle.getChildren().add(logoIcon);

        VBox titleBox = new VBox(2);
        Label title = new Label("SERVER DASHBOARD");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(C_TEXT));
        Label sub = new Label("HTTP/1.1 over Socket TCP");
        sub.setFont(Font.font("Consolas", 11));
        sub.setTextFill(Color.web(C_MUTED));
        titleBox.getChildren().addAll(title, sub);

        HBox logoBox = new HBox(12, logoCircle, titleBox);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        // Hiển thị IP server
        serverIpLabel = new Label();
        serverIpLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        serverIpLabel.setTextFill(Color.web(C_ACCENT));
        serverIpLabel.setStyle("-fx-background-color: #0d2a2a; -fx-background-radius: 20; -fx-padding: 8 16 8 16; -fx-border-color: #0d9488; -fx-border-radius: 20; -fx-border-width: 1;");

        // Status badge
        statusBadge = new Label("  STOPPED");
        FontIcon stopIcon = new FontIcon(FontAwesomeSolid.STOP_CIRCLE);
        stopIcon.setIconSize(13);
        stopIcon.setIconColor(Color.web(C_RED));
        statusBadge.setGraphic(stopIcon);
        statusBadge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        statusBadge.setTextFill(Color.web(C_RED));
        statusBadge.setStyle("-fx-background-color: rgba(239,68,68,0.15); -fx-background-radius: 20; -fx-padding: 8 18 8 18;");

        header.getChildren().addAll(logoBox, sp, serverIpLabel, statusBadge);

        // ============ LEFT PANEL ============
        VBox leftPanel = new VBox(18);
        leftPanel.setPrefWidth(340);
        leftPanel.setMinWidth(340);
        leftPanel.setPadding(new Insets(25, 0, 25, 25));

        // Control card
        VBox controlCard = new VBox(15);
        controlCard.setPadding(new Insets(24));
        controlCard.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 14; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 14; -fx-border-width: 1;");

        HBox ctrlHeader = new HBox(10);
        ctrlHeader.setAlignment(Pos.CENTER_LEFT);
        FontIcon ctrlIcn = new FontIcon(FontAwesomeSolid.COG);
        ctrlIcn.setIconSize(14);
        ctrlIcn.setIconColor(Color.web(C_ACCENT));
        Label ctrlTitle = new Label("  Điều khiển Server");
        ctrlTitle.setGraphic(ctrlIcn);
        ctrlTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        ctrlTitle.setTextFill(Color.web(C_TEXT));
        ctrlHeader.getChildren().add(ctrlTitle);

        startBtn = new Button("  START SERVER");
        FontIcon playIcn = new FontIcon(FontAwesomeSolid.PLAY);
        playIcn.setIconSize(14);
        playIcn.setIconColor(Color.WHITE);
        startBtn.setGraphic(playIcn);
        startBtn.setMaxWidth(Double.MAX_VALUE);
        startBtn.setPrefHeight(50);
        startBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        startBtn.setStyle("-fx-background-color: linear-gradient(to right, #10b981, #059669); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
        startBtn.setOnAction(e -> startServer());

        stopBtn = new Button("  STOP SERVER");
        FontIcon stopIcn = new FontIcon(FontAwesomeSolid.STOP);
        stopIcn.setIconSize(14);
        stopIcn.setIconColor(Color.web(C_MUTED));
        stopBtn.setGraphic(stopIcn);
        stopBtn.setMaxWidth(Double.MAX_VALUE);
        stopBtn.setPrefHeight(50);
        stopBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        stopBtn.setDisable(true);
        stopBtn.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-text-fill: " + C_MUTED + "; -fx-background-radius: 10; -fx-cursor: not-allowed;");
        stopBtn.setOnAction(e -> stopServer());

        HBox portInfo = new HBox(8);
        portInfo.setAlignment(Pos.CENTER_LEFT);
        FontIcon infoIcn = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        infoIcn.setIconSize(12);
        infoIcn.setIconColor(Color.web(C_MUTED));
        Label portLbl = new Label("Port: 8080  •  Threads: 20");
        portLbl.setFont(Font.font("Consolas", 11));
        portLbl.setTextFill(Color.web(C_MUTED));
        portInfo.getChildren().addAll(infoIcn, portLbl);

        controlCard.getChildren().addAll(ctrlHeader, startBtn, stopBtn, portInfo);

        // Stats card
        VBox statsCard = new VBox(16);
        statsCard.setPadding(new Insets(24));
        statsCard.setStyle("-fx-background-color: " + C_CARD + "; -fx-background-radius: 14; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 14; -fx-border-width: 1;");

        HBox statsHeader = new HBox(10);
        statsHeader.setAlignment(Pos.CENTER_LEFT);
        FontIcon statIcn = new FontIcon(FontAwesomeSolid.CHART_BAR);
        statIcn.setIconSize(14);
        statIcn.setIconColor(Color.web(C_GREEN));
        Label statsTitle = new Label("  Thống kê");
        statsTitle.setGraphic(statIcn);
        statsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        statsTitle.setTextFill(Color.web(C_TEXT));
        statsHeader.getChildren().add(statsTitle);

        activeClientsVal = new Label("0");
        totalClientsVal  = new Label("0");
        totalRequestsVal = new Label("0");
        uptimeVal        = new Label("00:00:00");

        VBox statsList = new VBox(14);
        statsList.getChildren().addAll(
            statRow(FontAwesomeSolid.USERS, "Client online", activeClientsVal, C_GREEN),
            statRow(FontAwesomeSolid.USER_PLUS, "Tổng client", totalClientsVal, C_BLUE),
            statRow(FontAwesomeSolid.EXCHANGE_ALT, "Tổng request", totalRequestsVal, "#8b5cf6"),
            statRow(FontAwesomeSolid.CLOCK, "Uptime", uptimeVal, C_ORANGE)
        );

        statsCard.getChildren().addAll(statsHeader, statsList);

        leftPanel.getChildren().addAll(controlCard, statsCard);

        ScrollPane leftScroll = new ScrollPane(leftPanel);
        leftScroll.setFitToWidth(true);
        leftScroll.setStyle("-fx-background: " + C_BG + "; -fx-background-color: " + C_BG + "; -fx-border-color: transparent;");

        // ============ CENTER — Tabs ============
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setStyle("-fx-background-color: transparent; -fx-tab-min-height: 40px; -fx-tab-max-height: 40px;");

        Tab clientTab = new Tab("   👥 Clients   ");
        clientTab.setContent(buildClientTablePane());

        Tab logTab = new Tab("   📜 Live Log   ");
        logTab.setContent(buildLogPane());

        Tab epTab = new Tab("   🔗 Endpoints   ");
        epTab.setContent(buildEndpointPane());

        tabs.getTabs().addAll(clientTab, logTab, epTab);

        VBox centerPanel = new VBox(tabs);
        centerPanel.setPadding(new Insets(25));
        VBox.setVgrow(tabs, Priority.ALWAYS);

        root.setTop(header);
        root.setLeft(leftScroll);
        root.setCenter(centerPanel);

        server.setLogListener(msg -> Platform.runLater(() -> {
            if (logArea != null) {
                logArea.appendText(msg + "\n");
                logArea.setScrollTop(Double.MAX_VALUE);
            }
        }));

        startStatsTimer();
        updateServerIp();

        Scene scene = new Scene(root, 1400, 850);
        stage.setScene(scene);
        stage.show();

        log("=== Server Dashboard sẵn sàng ===");
        log("Bấm 'START SERVER' để khởi động.");
    }

    // ============================================================
    private VBox buildClientTablePane() {
        VBox box = new VBox(18);
        box.setPadding(new Insets(24));

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        FontIcon icn = new FontIcon(FontAwesomeSolid.USERS);
        icn.setIconSize(16);
        icn.setIconColor(Color.web(C_ACCENT));

        Label title = new Label("  Danh sách Clients đã kết nối");
        title.setGraphic(icn);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        title.setTextFill(Color.web(C_TEXT));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label hint = new Label("  Tự động cập nhật mỗi 1 giây");
        FontIcon hintIcn = new FontIcon(FontAwesomeSolid.SYNC_ALT);
        hintIcn.setIconSize(11);
        hintIcn.setIconColor(Color.web(C_MUTED));
        hint.setGraphic(hintIcn);
        hint.setFont(Font.font("Segoe UI", 11));
        hint.setTextFill(Color.web(C_MUTED));

        headerRow.getChildren().addAll(title, sp, hint);

        clientTable = new TableView<>();
        clientList = FXCollections.observableArrayList();
        clientTable.setItems(clientList);
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clientTable.setPrefHeight(600);
        VBox.setVgrow(clientTable, Priority.ALWAYS);
        clientTable.setStyle("-fx-background-color: " + C_BG + "; -fx-control-inner-background: " + C_BG + "; -fx-table-cell-border-color: " + C_BORDER + ";");

        TableColumn<ClientManager.ClientInfo, String> colUser = new TableColumn<>("👤 Username");
        colUser.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getUsername()));
        colUser.setPrefWidth(150);

        TableColumn<ClientManager.ClientInfo, String> colIP = new TableColumn<>("🌐 IP Address");
        colIP.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getIp()));
        colIP.setPrefWidth(160);

        TableColumn<ClientManager.ClientInfo, String> colPort = new TableColumn<>("🔌 Port");
        colPort.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getPort())));
        colPort.setPrefWidth(100);

        TableColumn<ClientManager.ClientInfo, String> colLogin = new TableColumn<>("⏰ Login Time");
        colLogin.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getLoginTimeStr()));
        colLogin.setPrefWidth(140);

        TableColumn<ClientManager.ClientInfo, String> colLast = new TableColumn<>("⚡ Last Activity");
        colLast.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getLastActivityStr()));
        colLast.setPrefWidth(150);

        TableColumn<ClientManager.ClientInfo, String> colReq = new TableColumn<>("📨 Requests");
        colReq.setCellValueFactory(cd -> new SimpleStringProperty(String.valueOf(cd.getValue().getRequestCount())));
        colReq.setPrefWidth(110);

        TableColumn<ClientManager.ClientInfo, String> colStatus = new TableColumn<>("📊 Status");
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStatus()));
        colStatus.setPrefWidth(120);
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    if ("ONLINE".equals(item)) {
                        setText("● ONLINE");
                        setStyle("-fx-text-fill: " + C_GREEN + "; -fx-font-weight: bold;");
                    } else {
                        setText("○ OFFLINE");
                        setStyle("-fx-text-fill: " + C_MUTED + ";");
                    }
                }
            }
        });

        clientTable.getColumns().addAll(colUser, colIP, colPort, colLogin, colLast, colReq, colStatus);

        Button refreshBtn = new Button("  Làm mới");
        FontIcon rIcn = new FontIcon(FontAwesomeSolid.SYNC_ALT);
        rIcn.setIconSize(12);
        rIcn.setIconColor(Color.web(C_ACCENT));
        refreshBtn.setGraphic(rIcn);
        refreshBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        refreshBtn.setStyle("-fx-background-color: #0d2a2a; -fx-text-fill: " + C_ACCENT + "; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20 10 20; -fx-border-color: #0d9488; -fx-border-radius: 8; -fx-border-width: 1;");
        refreshBtn.setOnAction(e -> refreshClientTable());

        box.getChildren().addAll(headerRow, clientTable, refreshBtn);

        Thread t = new Thread(() -> {
            while (true) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
                Platform.runLater(this::refreshClientTable);
            }
        });
        t.setDaemon(true);
        t.start();

        return box;
    }

    private void refreshClientTable() {
        if (clientList == null) return;
        clientList.setAll(ClientManager.getAll().values());
    }

    private VBox buildLogPane() {
        VBox box = new VBox(18);
        box.setPadding(new Insets(24));

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        FontIcon icn = new FontIcon(FontAwesomeSolid.TERMINAL);
        icn.setIconSize(16);
        icn.setIconColor(Color.web(C_GREEN));

        Label title = new Label("  Live Log — Sự kiện server");
        title.setGraphic(icn);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        title.setTextFill(Color.web(C_TEXT));
        headerRow.getChildren().add(title);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 12.5; -fx-control-inner-background: #050810; -fx-text-fill: #4ade80; -fx-background-radius: 10;");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        box.getChildren().addAll(headerRow, logArea);
        return box;
    }

    private VBox buildEndpointPane() {
        VBox box = new VBox(16);
        box.setPadding(new Insets(24));

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        FontIcon icn = new FontIcon(FontAwesomeSolid.PLUG);
        icn.setIconSize(16);
        icn.setIconColor(Color.web(C_BLUE));

        Label title = new Label("  HTTP Endpoints");
        title.setGraphic(icn);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        title.setTextFill(Color.web(C_TEXT));
        headerRow.getChildren().add(title);

        VBox eps = new VBox(12);
        eps.getChildren().addAll(
            endpointCard("POST", "/api/login",     "Đăng nhập, trả token"),
            endpointCard("POST", "/api/checkin",   "Chấm công vào"),
            endpointCard("POST", "/api/checkout",  "Chấm công ra"),
            endpointCard("GET",  "/api/history",   "Lịch sử chấm công"),
            endpointCard("GET",  "/api/admin/all", "Admin xem tất cả"),
            endpointCard("GET",  "/api/ping",      "Kiểm tra server")
        );

        box.getChildren().addAll(headerRow, eps);
        return box;
    }

    private HBox endpointCard(String method, String path, String desc) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(16, 22, 16, 22));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: " + C_BG + "; -fx-background-radius: 10; -fx-border-color: " + C_BORDER + "; -fx-border-radius: 10; -fx-border-width: 1;");

        Label methodLbl = new Label(method);
        methodLbl.setFont(Font.font("Consolas", FontWeight.BOLD, 11));
        methodLbl.setTextFill(Color.WHITE);
        methodLbl.setStyle(
            ("POST".equals(method) ? "-fx-background-color: #0d9488;" : "-fx-background-color: #3b82f6;") +
            "-fx-background-radius: 6; -fx-padding: 5 14 5 14;"
        );

        Label pathLbl = new Label(path);
        pathLbl.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
        pathLbl.setTextFill(Color.web(C_TEXT));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label descLbl = new Label(desc);
        descLbl.setFont(Font.font("Segoe UI", 12));
        descLbl.setTextFill(Color.web(C_MUTED));

        row.getChildren().addAll(methodLbl, pathLbl, sp, descLbl);
        return row;
    }

    private HBox statRow(FontAwesomeSolid icon, String title, Label valueLbl, String color) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBox = new StackPane();
        iconBox.setPrefSize(34, 34);
        iconBox.setMaxSize(34, 34);
        iconBox.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 17;");

        FontIcon icn = new FontIcon(icon);
        icn.setIconSize(15);
        icn.setIconColor(Color.web(color));
        iconBox.getChildren().add(icn);

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Segoe UI", 12.5));
        titleLbl.setTextFill(Color.web(C_MUTED));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        valueLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        valueLbl.setTextFill(Color.web(color));

        row.getChildren().addAll(iconBox, titleLbl, sp, valueLbl);
        return row;
    }

    private void updateServerIp() {
        try {
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            serverIpLabel.setText("🌐  " + ip + ":8080");
        } catch (Exception e) {
            serverIpLabel.setText("🌐  localhost:8080");
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {
                server.start();
                Platform.runLater(() -> {
                    statusBadge.setText("  RUNNING");
                    FontIcon runIcon = new FontIcon(FontAwesomeSolid.CHECK_CIRCLE);
                    runIcon.setIconSize(13);
                    runIcon.setIconColor(Color.web(C_GREEN));
                    statusBadge.setGraphic(runIcon);
                    statusBadge.setTextFill(Color.web(C_GREEN));
                    statusBadge.setStyle("-fx-background-color: rgba(16,185,129,0.15); -fx-background-radius: 20; -fx-padding: 8 18 8 18;");

                    startBtn.setDisable(true);
                    startBtn.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-text-fill: " + C_MUTED + "; -fx-background-radius: 10; -fx-cursor: not-allowed;");

                    stopBtn.setDisable(false);
                    stopBtn.setStyle("-fx-background-color: linear-gradient(to right, #ef4444, #dc2626); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> log("❌ Lỗi start: " + e.getMessage()));
            }
        }).start();
    }

    private void stopServer() {
        server.stop();
        statusBadge.setText("  STOPPED");
        FontIcon stopIcon = new FontIcon(FontAwesomeSolid.STOP_CIRCLE);
        stopIcon.setIconSize(13);
        stopIcon.setIconColor(Color.web(C_RED));
        statusBadge.setGraphic(stopIcon);
        statusBadge.setTextFill(Color.web(C_RED));
        statusBadge.setStyle("-fx-background-color: rgba(239,68,68,0.15); -fx-background-radius: 20; -fx-padding: 8 18 8 18;");

        startBtn.setDisable(false);
        startBtn.setStyle("-fx-background-color: linear-gradient(to right, #10b981, #059669); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");

        stopBtn.setDisable(true);
        stopBtn.setStyle("-fx-background-color: " + C_CARD2 + "; -fx-text-fill: " + C_MUTED + "; -fx-background-radius: 10; -fx-cursor: not-allowed;");
    }

    private void startStatsTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
                Platform.runLater(() -> {
                    activeClientsVal.setText(String.valueOf(ClientManager.getOnlineCount()));
                    totalClientsVal.setText(String.valueOf(ServerStats.getTotalClients()));
                    totalRequestsVal.setText(String.valueOf(ServerStats.getTotalRequests()));
                    uptimeVal.setText(ServerStats.getUptime());
                });
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void log(String msg) {
        if (logArea != null) {
            logArea.appendText(msg + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}