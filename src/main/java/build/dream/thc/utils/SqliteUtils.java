package build.dream.thc.utils;

import build.dream.thc.domains.Token;

import java.sql.*;

public class SqliteUtils {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int insertToken(Token token) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite://e:/thc.db");
            preparedStatement = connection.prepareStatement("INSERT INTO token(access_token, token_type, expires_in, scope, fetch_time) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, token.getAccessToken());
            preparedStatement.setString(2, token.getTokenType());
            preparedStatement.setLong(3, token.getExpiresIn());
            preparedStatement.setString(4, token.getScope());
            preparedStatement.setDate(5, token.getFetchTime());
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            DatabaseUtils.closeStatement(preparedStatement);
            DatabaseUtils.closeConnection(connection);
            throw new RuntimeException(e);
        }
    }

    public static Token findToken() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Token token = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite://e:/thc.db");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from token");
            if (resultSet.next()) {
                token.setAccessToken(resultSet.getString("access_token"));
                token.setTokenType(resultSet.getString("token_type"));
                token.setExpiresIn(resultSet.getLong("expires_in"));
                token.setScope(resultSet.getString("scope"));
                token.setFetchTime(resultSet.getDate("fetch_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeResultSet(resultSet);
            DatabaseUtils.closeStatement(statement);
            DatabaseUtils.closeConnection(connection);
        }
        return token;
    }

    public static int deleteToken() {
        return 0;
    }
}
