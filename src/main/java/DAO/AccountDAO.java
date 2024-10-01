package DAO;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public Account insertAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();
        try{
        String sql = "INSERT INTO Account (username, password) VALUES (?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();

        if(rs.next()){
            int generated_account_id = (int) rs.getLong(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
        }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Account verifyAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2,account.getPassword());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account returnedAccount = new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
                return returnedAccount;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Account getAccountById(int accountId){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
           

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account returnedAccount = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}
