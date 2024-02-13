package tk.slicecollections.maxteer.database.types;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tk.slicecollections.maxteer.Manager;
import tk.slicecollections.maxteer.database.cache.DataCache;
import tk.slicecollections.maxteer.database.enuns.DataTypes;
import tk.slicecollections.maxteer.database.interfaces.DatabaseInterface;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@SuppressWarnings("all")
public class MySQL implements DatabaseInterface {

    @NonNull
    private String name;

    @NonNull
    private String user;

    @NonNull
    private String password;

    @NonNull
    private String ip;

    private HikariDataSource resource;
    private Connection connection;

    @Override
    public void setupConnection() throws Exception {
        Manager.sendMessageToConsole("§eInicializando a conexão MySQL com o servidor...");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + ip.split(":")[0] + ":" + ip.split(":")[1] + "/" + name);
        config.setUsername(user);
        config.setPassword(password);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(100);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(5000);
        config.setValidationTimeout(3000);
        config.setAutoCommit(true);
        config.setMaxLifetime(1800000);
        this.resource = new HikariDataSource(config);
        DataCache.setupDataCache();

        Manager.sendMessageToConsole("Conexão MySQL realizada com sucesso!");
    }

    public void setupTable(String tableName, String... columns) {
        Statement statement = null;
        StringBuilder builder = new StringBuilder();
        Arrays.stream(columns).forEach(column -> builder.append(column).append(", "));
        try {
            Connection newConnection = openConnection();
            statement = newConnection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" + builder + ");");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addColumn(String tableName, String column) {
        Statement statement = null;
        try {
            Connection newConnection = openConnection();
            statement = newConnection.createStatement();
            statement.executeUpdate("ALTER TABLE IF NOT EXISTS " + tableName + " ADD " + column + ";");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateColumn(String tableName, String column, Object value, String conditions) {
        PreparedStatement statement = null;
        try {
            Connection newConnection = openConnection();
            statement = newConnection.prepareStatement("UPDATE " + tableName + " SET " + column + " = ? WHERE " + conditions + ";");
            statement.setObject(1, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Object getValueColumn(String tableName, String column, String conditions) {
        Statement statement = null;
        ResultSet resultSet = null;
        Object value = null;
        try {
            Connection newConnection = openConnection();
            statement = newConnection.createStatement();
            resultSet = statement.executeQuery("SELECT " + column + " FROM " + tableName + " WHERE " + conditions + ";");
            while (resultSet.next()) {
                value = resultSet.getObject(column);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public Map<String, Object> getValueColumn(String tableName, String conditions, String... columns) {
        Statement statement = null;
        ResultSet resultSet = null;
        Map<String, Object> value = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        Arrays.stream(columns).forEach(column -> builder.append(column).append(", "));
        try {
            Connection newConnection = openConnection();
            statement = newConnection.createStatement();
            resultSet = statement.executeQuery("SELECT " + builder + " FROM " + tableName + " WHERE " + conditions + ";");
            int index = 1;
            while (resultSet.next()) {
                value.put(columns[index - 1], resultSet.getObject(1));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public Map<String, Object> getValueColumn(String tableName, String conditions, List<String> columns) {
        Statement statement = null;
        ResultSet resultSet = null;
        Map<String, Object> value = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        columns.forEach(column -> builder.append(column).append(", "));
        try {
            Connection newConnection = openConnection();
            statement = newConnection.createStatement();
            resultSet = statement.executeQuery("SELECT " + builder + " FROM " + tableName + " WHERE " + conditions + ";");
            int index = 1;
            while (resultSet.next()) {
                value.put(columns.get(index - 1), resultSet.getObject(1));
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public boolean existsColumn(String table, String column) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Connection newConnection = openConnection();
            statement = newConnection.prepareStatement("SELECT " + column + " FROM " + table + ";");
            resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @SneakyThrows
    @Override
    public void destroyConnection() {
        if (this.connection != null && this.connection.isClosed()) {
            this.connection.close();
            this.connection = null;
        }

        if (isConnected()) {
            this.resource.close();
            this.resource = null;
        }
    }

    @Override
    public DataTypes getType() {
        return DataTypes.MYSQL;
    }

    public Connection openConnection() {
        try {
            if (connection == null || connection.isClosed()){
                this.connection = resource.getConnection();
            }

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HikariDataSource getConnection() {
        return this.resource;
    }

    public boolean isConnected() {
        return this.resource != null && this.resource.isRunning();
    }

}
