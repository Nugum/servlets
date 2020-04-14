package dao;

import entities.User;

import java.sql.*;

public class UserDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/servlets";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "131997";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByName(String name) {
        User user = null;
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from usr where name=?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return user;
    }
    public User findUser(String name, String password) {
        User user = null;
        Connection connection = null;

        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from usr where name=?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next() && rs.getString("password").equals(password)) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return user;
    }
}

