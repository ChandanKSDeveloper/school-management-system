package com.dao;

import com.database.DBConnection;
import com.model.TeacherModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDao {
    private final String table = DBConnection.getProps("TEACHER_TABLE");


    public boolean createTeacher(TeacherModel teacher) {
        String sql = "INSERT INTO " + table + " (first_name, last_name, subject, email, dob, mobile_no, gender, district, city, state, nationality, photo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, teacher.getFirst_name());
            ps.setString(2, teacher.getLast_name());
            ps.setString(3, teacher.getSubject());
            ps.setString(4, teacher.getEmail());
            ps.setDate(5, teacher.getDob());
            ps.setString(6, teacher.getMobile_no());
            ps.setString(7, teacher.getGender());
            ps.setString(8, teacher.getDistrict());
            ps.setString(9, teacher.getCity());
            ps.setString(10, teacher.getState());
            ps.setString(11, teacher.getNationality());
            ps.setString(12, teacher.getPhoto());

            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error on inserting teacher form data : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean updateTeacher(int id, TeacherModel teacher) {
        String sql = "UPDATE " + table + " SET first_name = ?, last_name = ?, subject = ?, email = ?, dob = ?, mobile_no = ?, gender = ?, district = ?, city = ?, state = ?, nationality = ?, photo = ? WHERE id = ?;";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, teacher.getFirst_name());
            ps.setString(2, teacher.getLast_name());
            ps.setString(3, teacher.getSubject());
            ps.setString(4, teacher.getEmail());
            ps.setDate(5, teacher.getDob());
            ps.setString(6, teacher.getMobile_no());
            ps.setString(7, teacher.getGender());
            ps.setString(8, teacher.getDistrict());
            ps.setString(9, teacher.getCity());
            ps.setString(10, teacher.getState());
            ps.setString(11, teacher.getNationality());
            ps.setString(12, teacher.getPhoto());
            ps.setInt(13,id);

            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error on updating teacher form data : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean deleteTeacher(int id) {
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("error on deleting teacher data : " + e.getMessage());
        }
    }

    public TeacherModel getTeacherById(int id) {
        String sql = "SELECT * FROM " + table + " WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRowToTeacher(rs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("error on getting teacher by id : " + e.getMessage());
        }
        return null;
    }

    public List<TeacherModel> getAllTeachers() {
        List<TeacherModel> teachers = new ArrayList<>();
        String sql = "SELECT * FROM " + table + ";";

        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                teachers.add(mapRowToTeacher(rs));
            }

            return teachers;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while retrieving all teacher : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Pagination support
    public List<TeacherModel> getTeachers(int page, int size) throws SQLException {
        String sql = "SELECT * FROM teacher ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);  // LIMIT - number of records per page
            stmt.setInt(2, (page - 1) * size); // OFFSET - number of records to skip

            List<TeacherModel> teachers = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teachers.add(mapRowToTeacher(rs));
                }
            }
            return teachers;

            /*usage
                TeacherDao teacherDao = new TeacherDao();

             // Get first page with 10 records per page
                List<TeacherModel> page1 = teacherDao.getTeachers(1, 10);

              // Get second page with 10 records per page
                List<TeacherModel> page2 = teacherDao.getTeachers(2, 10);
              */
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean teacherExist(String email){
        String sql = "SELECT COUNT(*) AS count FROM " + table + " WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        }

        return false;

    }

    public int getTeacherId(String email){
        String sql = "SELECT id from " + table + " WHERE email = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }


    private TeacherModel mapRowToTeacher(ResultSet rs) throws SQLException {
        return new TeacherModel(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getDate("dob"),
                rs.getString("mobile_no"),
                rs.getString("gender"),
                rs.getString("district"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("nationality"),
                rs.getString("photo"),
                rs.getString("subject"),
                rs.getString("photo_id")

        );
    }
}
