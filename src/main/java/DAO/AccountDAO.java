package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data btw the format of obj in Java to rows in
 * a db.
 * 
 * It contains values as the Account class:
 * account_id which is type int, Pk, auto-incremented
 * username which is unique, varchar(255)
 * password which is varchar(255) & greater than 4 charter long
 */
public class AccountDAO {
    /**
     * retrieve all accounts from the Account table
     * @return all Accounts
     */
    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try{
            String sql = "select * from account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
    * insert account into the account table
    * The account_id should be auto-generated by sql db if it's not provided (set to auto-increment). 
    * Only need to insert a record with columns(username, password)
    */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //sql here. only insert with the username, password columns so db will generate a Pk
            String sql = "insert into account (username, password) values (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //preparedStatement's setString methods here
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic here
            String sql = "select * from account where(account_id) = (?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //preparedStatement's setInt method here
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    } 
}


