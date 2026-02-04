package com.fendrixx.iLikeHomes.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DatabaseStorage implements Storage {

    private HikariDataSource dataSource;
    private final String type;
    private final String host, name, user, password;
    private final int port;
    private final boolean debug;
    private final Plugin plugin;

    public DatabaseStorage(Plugin plugin) {
        this.plugin = plugin;
        ConfigurationSection dbSection = plugin.getConfig().getConfigurationSection("database");
        this.type = dbSection != null ? dbSection.getString("type", "SQLITE") : "SQLITE";
        this.host = dbSection != null ? dbSection.getString("host", "localhost") : "localhost";
        this.port = dbSection != null ? dbSection.getInt("port", 3306) : 3306;
        this.name = dbSection != null ? dbSection.getString("name", "ilikehomes") : "ilikehomes";
        this.user = dbSection != null ? dbSection.getString("user", "root") : "root";
        this.password = dbSection != null ? dbSection.getString("password", "") : "";
        this.debug = dbSection != null && dbSection.getBoolean("debug", false);

        setupDataSource();
        setupTable();
    }

    private void setupDataSource() {
        HikariConfig config = new HikariConfig();

        if (type.equalsIgnoreCase("MYSQL")) {
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name + "?useSSL=false");
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            debugLog("&aInitializing HikariCP for MYSQL at " + host + ":" + port);
        } else { // SQLITE
            File dataFolder = new File(plugin.getDataFolder(), ".data");
            if (!dataFolder.exists())
                dataFolder.mkdirs();
            File sqliteFile = new File(dataFolder, "homes.db");
            config.setJdbcUrl("jdbc:sqlite:" + sqliteFile.getAbsolutePath());
            config.setDriverClassName("org.sqlite.JDBC");
            config.setMaximumPoolSize(1);
            debugLog("&aInitializing HikariCP for SQLITE (Pool size 1)");
        }

        config.setMinimumIdle(1);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(5000);
        config.setPoolName("iLikeHomesPool");

        try {
            this.dataSource = new HikariDataSource(config);
            debugLog("&aDatabase connection established successfully.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8[&6i&eLike&6Homes&8] &cFailed to initialize database: " + e.getMessage()));
            if (debug)
                e.printStackTrace();
        }
    }

    private void debugLog(String message) {
        if (debug) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8[&6i&eLike&6Homes&8] &7[DEBUG] " + message));
        }
    }

    private synchronized Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            setupDataSource();
        }
        return dataSource.getConnection();
    }

    private void setupTable() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS ilikehomes_homes (" +
                    "uuid VARCHAR(36) NOT NULL," +
                    "name VARCHAR(255) NOT NULL," +
                    "world VARCHAR(255) NOT NULL," +
                    "x DOUBLE NOT NULL," +
                    "y DOUBLE NOT NULL," +
                    "z DOUBLE NOT NULL," +
                    "yaw FLOAT NOT NULL," +
                    "pitch FLOAT NOT NULL," +
                    "icon VARCHAR(255) DEFAULT 'CHEST'," +
                    "PRIMARY KEY (uuid, name)" +
                    ");";
            stmt.execute(sql);

            try {
                stmt.execute("ALTER TABLE ilikehomes_homes ADD COLUMN icon VARCHAR(255) DEFAULT 'CHEST'");
            } catch (SQLException ignored) {
            }

        } catch (SQLException e) {
            if (debug)
                e.printStackTrace();
        }
    }

    @Override
    public void saveHome(UUID uuid, String name, Location loc, String icon) {
        String sql = "REPLACE INTO ilikehomes_homes (uuid, name, world, x, y, z, yaw, pitch, icon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, name);
            pstmt.setString(3, loc.getWorld().getName());
            pstmt.setDouble(4, loc.getX());
            pstmt.setDouble(5, loc.getY());
            pstmt.setDouble(6, loc.getZ());
            pstmt.setFloat(7, loc.getYaw());
            pstmt.setFloat(8, loc.getPitch());
            pstmt.setString(9, icon != null ? icon.toUpperCase() : "CHEST");
            pstmt.executeUpdate();
            debugLog("&7Home saved: " + name + " for " + uuid);
        } catch (SQLException e) {
            if (debug)
                e.printStackTrace();
        }
    }

    @Override
    public void deleteHome(UUID uuid, String name) {
        String sql = "DELETE FROM ilikehomes_homes WHERE uuid = ? AND name = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            debugLog("&7Home deleted: " + name + " for " + uuid);
        } catch (SQLException e) {
            if (debug)
                e.printStackTrace();
        }
    }

    @Override
    public Location getHomeLocation(UUID uuid, String name) {
        String sql = "SELECT * FROM ilikehomes_homes WHERE uuid = ? AND name = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    World world = Bukkit.getWorld(rs.getString("world"));
                    if (world == null)
                        return null;
                    return new Location(world,
                            rs.getDouble("x"),
                            rs.getDouble("y"),
                            rs.getDouble("z"),
                            rs.getFloat("yaw"),
                            rs.getFloat("pitch"));
                }
            }
        } catch (SQLException e) {
            if (debug)
                e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getHomeIcon(UUID uuid, String name) {
        String sql = "SELECT icon FROM ilikehomes_homes WHERE uuid = ? AND name = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("icon");
                }
            }
        } catch (SQLException e) {
            if (debug)
                e.printStackTrace();
        }
        return "CHEST";
    }

    @Override
    public Set<String> getHomeNames(UUID uuid) {
        Set<String> names = new HashSet<>();
        String sql = "SELECT name FROM ilikehomes_homes WHERE uuid = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            if (debug)
                e.printStackTrace();
        }
        return names;
    }

    @Override
    public void reload() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            dataSource = null;
            debugLog("&aDatabase connection closed.");
        }
    }
}
