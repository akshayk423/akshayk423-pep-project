package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MessageDAO {
    public Message insertMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());

            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }


    public Message getMessageById(int message_id){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return message;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Message deleteMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ps.executeUpdate();
   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessage(int message_id, String new_message) {
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, new_message);
            ps.setInt(2, message_id);
            ps.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return getMessageById(message_id);
    }

    public List<Message> getMessagesFromAccountId(int account_id) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,account_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
