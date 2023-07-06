package repositories;

import Utils.DateHelper;
import Utils.DbHelper;
import models.Chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRepo {
    public static List<Chat> getChats(int senderId, int receiverId) throws Exception {
        ArrayList<Chat> list = new ArrayList<>();

        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM chats WHERE senderId = ? AND receiverId = ? "
                                        + "UNION SELECT * FROM chats WHERE senderId = ? AND receiverId = ? "
                                        + "ORDER BY sendTime");
        stmt.setInt(1, senderId);
        stmt.setInt(2, receiverId);
        stmt.setInt(3, receiverId);
        stmt.setInt(4, senderId);
        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            list.add(parseRes(res));
        }
        return list;
    }

    public static List<Chat> getGroupChats(int senderId, int receiverId) throws Exception {
        ArrayList<Chat> list = new ArrayList<>();

        Connection conn = DbHelper.getConnection();
        PreparedStatement stmt =
                conn.prepareStatement("SELECT * FROM chats "
                        + "ORDER BY sendTime");
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            list.add(parseRes(res));
        }
        return list;
    }

    public static boolean createGroupChat(Chat model) throws Exception{

        Connection conn = DbHelper.getConnection();
        String query = "INSERT INTO groupChats () VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, model.getSenderID());
        stmt.setString(2, model.getMessage());
        stmt.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        stmt.setString(4, model.getPath());

        if (stmt.executeUpdate() != 1)
            throw new Exception("ERR_NO_ROW_CHANGE");

        return true;
    }

    public static boolean createChat(Chat model) throws Exception {
        Connection conn = DbHelper.getConnection();
        String query = "INSERT INTO chats () VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, model.getSenderID());
        stmt.setInt(2, model.getReceiverID());
        stmt.setString(3, model.getMessage());
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        stmt.setString(5, model.getPath());

        if (stmt.executeUpdate() != 1)
            throw new Exception("ERR_NO_ROW_CHANGE");

        return true;
    }

    public static boolean remove(int senderId, int receiverId, Date sendTime) throws Exception {
        String query = "DELETE FROM chats WHERE senderId = ? AND receiverId = ? AND sendTime = ?";
        PreparedStatement stmt = DbHelper.getConnection().prepareStatement(query);
        stmt.setInt(1, senderId);
        stmt.setInt(2, receiverId);
        stmt.setTimestamp(3, java.sql.Timestamp.valueOf(DateHelper.toSqlFormat(sendTime)));

        return stmt.executeUpdate() == 1;
    }

    private static Chat parseRes(ResultSet res) throws Exception {
        int senderId = res.getInt("senderId");
        int receiverId = res.getInt("receiverId");
        String message = res.getString("message");
        Date sendTime = DateHelper.fromSql(res.getString("sendTime"));
        String path = res.getString("path");

        return new Chat(senderId, receiverId, sendTime, message, path);
    }
}
