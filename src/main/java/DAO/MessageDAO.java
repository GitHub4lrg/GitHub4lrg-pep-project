package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO is a class that mediates the transformation of data btw the format of obj in Java to rows in a db.
 * table message contains similar values as the Message class:
 * message_id which is int, Pk, auto-increment
 * posted_by which is int, Fk (referenced to account(account_id))
 * message_text which is type varchar(255)
 * time_posted_epoch which is type bigint
 */

public class MessageDAO {
    /**
     * retrieve all messages from message table
     * @return all messages
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            //SQL logic here
            String sql = "select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * retrieve a message from the message table, identified by its ID
     * @return a message identified by ID
     */
    public Message getMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic here
            String sql = "select * from message where(message_id) = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //preparedStatement's setInt method here
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
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

    /**
     * insert a message into the message table
     * the Pk will auto-generate here.
     */
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic here
            String sql = "insert into message(posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //preparedStatement's setString & setInt methods here
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * delete a message from the message table, identified by its ID
     * @return a message identified by ID
     */
    public Message deleteMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic here
            String sql = "delete * from message where(message_id) = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //preparedStatement's setInt method here
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
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

    /**
     * update a message from the message table, identified by its ID
     * @return a message identified by ID
     */
    public Message updateMessageByMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic here
            String sql = "update * from message where(message_id) = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //preparedStatement's setInt method here
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
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

        /**
         * retrieve all messages written by a particular user from ???message table
         * @return all messages
         */
        public List<Message> getAllMessagesByUser(){
            Connection connection = ConnectionUtil.getConnection();
            List<Message> messages = new ArrayList<>();
            try{
                //SQL logic here
                String sql = "select * from message where(posted_by) = (?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    Message message = new Message(rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch"));
                    messages.add(message);
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return messages;
        }

        public Message getMessageByAccountId(int account_id) {
            Connection connection = ConnectionUtil.getConnection();
            try{
                //SQL logic here
                String sql = "select message.message_text from message join account on message.posted_by = account.account_id where account.account_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                //preparedStatement's setInt method here
                preparedStatement.setInt(1, account_id);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    Message messages = new Message(account_id, 
                            rs.getString("message_text"), account_id);
                    return messages;
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
        } 
    
}
