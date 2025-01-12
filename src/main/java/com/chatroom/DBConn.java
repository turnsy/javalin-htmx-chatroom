package com.chatroom;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;

public class DBConn {
    private final Connection connection;

    public DBConn() {
        Dotenv dotenv = Dotenv.load();
        try {
            this.connection = DriverManager.getConnection(
                    dotenv.get("DB_CONN_STRING"),
                    dotenv.get("DB_USER"),
                    dotenv.get("DB_PASS")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<DBMessage> getMessages(int limit) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(String.format("select * from messages limit %s;", limit));
        ResultSet rs = stmt.executeQuery();

        ArrayList<DBMessage> output = new ArrayList<>();
        int idx = 0;
        while (rs.next() && idx < limit) {
            DBMessage currentMessage = new DBMessage();
            currentMessage.setId(rs.getInt(1));
            currentMessage.setContent(rs.getString(2));
            currentMessage.setUsername(rs.getString(3));
            currentMessage.setCreatedAt(rs.getTimestamp(4));
            currentMessage.setUpdatedAt(rs.getTimestamp(5));
            output.add(currentMessage);
            idx++;
        }
        return output;
    }

    public void addMessage(String username, String content) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(String.format("insert into messages (username, content) values ('%s', '%s');", username, content));
        stmt.execute();
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
