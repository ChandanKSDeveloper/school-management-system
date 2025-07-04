package com.dao;

import com.database.DBConnection;
import com.model.StudentModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private final String table = DBConnection.getProps("STUDENT_TABLE");


    public boolean createStudent(StudentModel student) {
        String sql = "INSERT INTO " + table + " (first_name, last_name, father_name, email, dob, mobile_no, gender, district, city, state, nationality, photo, class, photo_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, student.getFirst_name());
            ps.setString(2, student.getLast_name());
            ps.setString(3, student.getFather_name());
            ps.setString(4, student.getEmail());
            ps.setDate(5, student.getDob());
            ps.setString(6, student.getMobile_no());
            ps.setString(7, student.getGender());
            ps.setString(8, student.getDistrict());
            ps.setString(9, student.getCity());
            ps.setString(10, student.getState());
            ps.setString(11, student.getNationality());
            ps.setString(12, student.getPhoto());
            ps.setInt(13, student.getClassroom());
            ps.setString(14,student.getPhoto_id());

            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error on inserting student form data : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean updateStudent(int id, StudentModel student) {
        if (id <= 0) throw new IllegalArgumentException("Invalid student ID");
        if (student == null) throw new IllegalArgumentException("Student cannot be null");

        String sql = "UPDATE " + table + " SET first_name = ?, last_name = ?, father_name = ?, email = ?, dob = ?, mobile_no = ?, gender = ?, district = ?, city = ?, state = ?, nationality = ?, photo = ?, class = ? WHERE id = ?;";
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, student.getFirst_name());
            ps.setString(2, student.getLast_name());
            ps.setString(3, student.getFather_name());
            ps.setString(4, student.getEmail());
            ps.setDate(5, student.getDob());
            ps.setString(6, student.getMobile_no());
            ps.setString(7, student.getGender());
            ps.setString(8, student.getDistrict());
            ps.setString(9, student.getCity());
            ps.setString(10, student.getState());
            ps.setString(11, student.getNationality());
            ps.setString(12, student.getPhoto());
            ps.setInt(13, student.getClassroom());
            ps.setInt(14, id);

            return  ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error on updating student form data : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("error on deleting student data : " + e.getMessage());
        }
    }

    public StudentModel getStudentById(int id) {
        String sql = "SELECT * FROM " + table + " WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); // change to setInt from setLong
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRowToStudent(rs);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("error on getting student by id : " + e.getMessage());
        }
        return null;
    }

    public int getStudentId(String email){
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

    public List<StudentModel> getAllStudents() {
        List<StudentModel> students = new ArrayList<>();
        String sql = "SELECT * FROM " + table + ";";

        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }

            return students;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while retrieving all student : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Pagination support
    public List<StudentModel> getStudents(int page, int size) throws SQLException {
        String sql = "SELECT * FROM "+table+" ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);  // LIMIT - number of records per page
            stmt.setInt(2, (page - 1) * size); // OFFSET - number of records to skip

            List<StudentModel> students = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRowToStudent(rs));
                }
            }
            return students;

            /*usage
                StudentDao studentDao = new StudentDao();

             // Get first page with 10 records per page
                List<StudentModel> page1 = studentDao.getStudents(1, 10);

              // Get second page with 10 records per page
                List<StudentModel> page2 = studentDao.getStudents(2, 10);
              */
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean studentExist(String email){
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
//            throw new RuntimeException(e);
        }

        return false;

    }

    public String getPhoto_id(int id){
        String sql = "SELECT photo_id FROM " + table + " WHERE id = ?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("photo_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private StudentModel mapRowToStudent(ResultSet rs) throws SQLException {
        return new StudentModel(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("father_name"),
                rs.getString("email"),
                rs.getDate("dob"),
                rs.getString("mobile_no"),
                rs.getString("gender"),
                rs.getString("district"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("nationality"),
                rs.getString("photo"),
                rs.getInt("class")

        );
    }

    private int getTotalStudentCount(){
        String sql = "SELECT COUNT(*) FROM " + table;
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ){
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }





//    private boolean checkId(int id, StudentModel student){
//        if (id <= 0) throw new IllegalArgumentException("Invalid student ID");
//        if (student == null) throw new IllegalArgumentException("Student cannot be null");
//
//    }
}
