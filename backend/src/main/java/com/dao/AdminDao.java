package com.dao;

import com.database.DBConnection;
import com.model.AdminModel;
import com.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    private final String table = DBConnection.getProps("ADMIN_TABLE");

    public boolean register(AdminModel adminModel) {

        if(isAdminExist(adminModel.getUsername())){
            return false;
        }
        String sql = "INSERT INTO " + table + "(email, username, password, profile_image) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, adminModel.getEmail());
            ps.setString(2, adminModel.getUsername());
            ps.setString(3, adminModel.getPassword());
            ps.setString(4, adminModel.getProfile_image());

            return ps.executeUpdate() > 0;
        } catch (RuntimeException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public boolean validateAdmin(String username, String rawPassword) {
        String sql = "SELECT password FROM " + table + " WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setString(1, username);
             ResultSet rs = ps.executeQuery();
             if (rs.next()) {
                String hashed = rs.getString("password");
                return PasswordUtil.verifyPassword(rawPassword, hashed);
             }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
//            throw new RuntimeException("error while validating admin : " + e.getMessage());
        }

        return false;
    }

    public boolean isAdminExist(String username){
        String sql = "SELECT username FROM " + table + " WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
           return rs.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("error while validating admin : " + e.getMessage());
        }
    }

    public boolean changePassword(String username,  String rawPassword){
        String sql = "UPDATE " + table + " SET password = ? WHERE username = ?;";

        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, PasswordUtil.hashPassword(rawPassword));
            ps.setString(2,username);
            return ps.executeUpdate() == 1;
        } catch (RuntimeException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("error while changing password : " + e.getMessage());
        }
    }

    public boolean updateProfile_image(String username, String imageUrl){
        String sql = "UPDATE " + table + " SET profile_image = ? WHERE username = ?";

        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,imageUrl);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (RuntimeException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("error while changing profile image : " + e.getMessage());
        }

    }

    public AdminModel getAdminByUsername(String username){
        String sql = "SELECT email, username, profile_image FROM " + table + " WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AdminModel admin = new AdminModel();
                admin.setEmail(rs.getString("email"));
                admin.setUsername(rs.getString("username"));
                admin.setProfile_image(rs.getString("profile_image"));
                return admin;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
