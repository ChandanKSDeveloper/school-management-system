package com.controller;

import com.dao.TeacherDao;
import com.google.gson.Gson;
import com.model.TeacherModel;
import com.utils.CloudinaryUtil;
import com.utils.TimeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@MultipartConfig
@WebServlet("/api/teacher/*")
public class TeacherController extends HttpServlet {

    private final TeacherDao teacherDao = new TeacherDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "Invalid GET path");
            return;
        }

        if(path.equals("/getTeacher")){
            List<TeacherModel> teachers = teacherDao.getAllTeachers();
            writeJson(resp, HttpServletResponse.SC_OK, "Teachers retrieved successfully", teachers);
        } else if (path.startsWith("/getTeacher/")) { // matches "/123"
            // Extract ID part
            try {
                String idPart = path.substring("/getTeacher/".length());
                int teacherId = Integer.parseInt(idPart);

                TeacherModel teacher = teacherDao.getTeacherById(teacherId);
                if (teacher != null) {
                    writeJson(resp, HttpServletResponse.SC_OK, "Teacher retrieved successfully", teacher);
                } else {
                    writeJson(resp, HttpServletResponse.SC_NOT_FOUND, "Teacher not found with ID: " + teacherId, null);
                }
            } catch (NumberFormatException e) {
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid Teacher ID format", null);
            }
        }  else {
            // Invalid path format
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path format. Use teacher/getTeacher or teacher/getTeacher/{id}", null);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "Invalid POST path");
            return;
        }
        if (path.equals("/add")) {
            addTeacher(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "Invalid PUT path");
            return;
        }

        if (path.equals("/update")) {
            updateTeacher(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "Invalid DELETE path");
            return;
        }

        if (path.equals("/delete")) {
            deleteTeacher(req, resp);
        }
    }

    private void addTeacher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, "Unauthorized. Please login.");
            return;
        }

        System.out.println("adding student");
        if (!req.getContentType().toLowerCase().startsWith("multipart/form-data")) {
            sendError(resp, "Content-type must be multipart/form-data");
            return;
        }

        String first_name = req.getParameter("first_name");
        String last_name = req.getParameter("last_name");
        String email = req.getParameter("email");
        String dobParam = req.getParameter("dob");
        String mobile_no = req.getParameter("mobile_no");
        String gender = req.getParameter("gender");
        String district = req.getParameter("district");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String nationality = req.getParameter("nationality");
        String subject = req.getParameter("subject");
        if (first_name == null || last_name == null || email == null ||
                dobParam == null || mobile_no == null ||
                gender == null || district == null ||
                city == null || state == null ||
                nationality == null
        ) {

            sendError(resp, "all fields are required");
            return;
        }

        if(teacherDao.teacherExist(email)){
            sendError(resp, "Teacher with this email exist. email must be unique for each Teacher");
            return;
        }

        Date dob;
        try{
            LocalDate dobLocal = LocalDate.parse(dobParam);
            dob = TimeUtil.toSqlDate(dobLocal);
        } catch (RuntimeException e) {
            sendError(resp, "Invalid date format for dob. Expected yyyy-MM-dd");
            return;
        }

        Part filePart = req.getPart("photo");
        String imageUrl = null;
        String imageId = null;

        if(filePart != null && filePart.getSize() > 0){
            String fileName = subject + "_" + first_name + "_photo";
            Map result = CloudinaryUtil.uploadImage(filePart.getInputStream(), fileName);
            imageUrl = (String) result.get("secure_url");
            imageId = (String) result.get("public_id");
        }

        TeacherModel teacher = new TeacherModel(first_name, last_name, email, dob, mobile_no, gender, district, city, state, nationality, imageUrl, subject, imageId);

        boolean success = teacherDao.createTeacher(teacher);

        if (!success) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add new student", null);
            return;
        }


        TeacherModel createdTeacher = teacherDao.getTeacherById(teacherDao.getTeacherId(email));

        if(createdTeacher == null){
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Teacher created but could not be retrieved", null);
            return;
        }

        writeJson(resp, HttpServletResponse.SC_CREATED, "New Teacher added successfully", createdTeacher);
    }


    private void updateTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, "Unauthorized. Please login.");
            return;
        }

        if (!req.getContentType().toLowerCase().startsWith("multipart/form-data")) {
            sendError(resp, "Content-type must be multipart/form-data");
            return;
        }

        int teacherId = Integer.parseInt(req.getParameter("id"));
        System.out.println(teacherId);
        TeacherModel existing = teacherDao.getTeacherById(teacherId);
        if (existing == null) {
            sendError(resp, "Teacher with ID " + teacherId + " not found");
            return;
        }

        String firstName = Optional.ofNullable(req.getParameter("first_name")).orElse(existing.getFirst_name());
        String lastName = Optional.ofNullable(req.getParameter("last_name")).orElse(existing.getLast_name());
        String subject = Optional.ofNullable(req.getParameter("subject")).orElse(existing.getSubject());
        String email = Optional.ofNullable(req.getParameter("email")).orElse(existing.getEmail());
        String mobileNo = Optional.ofNullable(req.getParameter("mobile_no")).orElse(existing.getMobile_no());
        String gender = Optional.ofNullable(req.getParameter("gender")).orElse(existing.getGender());
        String district = Optional.ofNullable(req.getParameter("district")).orElse(existing.getDistrict());
        String city = Optional.ofNullable(req.getParameter("city")).orElse(existing.getCity());
        String state = Optional.ofNullable(req.getParameter("state")).orElse(existing.getState());
        String nationality = Optional.ofNullable(req.getParameter("nationality")).orElse(existing.getNationality());


        //        handling dob
        Date dob = existing.getDob();
        String dobParam = req.getParameter("dob");
        if (dobParam != null) {
            try {
                LocalDate localDob = LocalDate.parse(dobParam);
                dob = TimeUtil.toSqlDate(localDob);
            } catch (Exception e) {
                sendError(resp, "Invalid DOB format. Use yyyy-MM-dd");
                return;
            }
        }

        //previous photo
        Part filePart = req.getPart("photo");
        String imageUrl = existing.getPhoto();
        String imageId = existing.getPhoto_id();

        if (filePart != null && filePart.getSize() > 0) {

            String fileName = subject + "_" + firstName + "_photo";
            Map result = CloudinaryUtil.uploadImage(filePart.getInputStream(), fileName);
            imageUrl = (String) result.get("secure_url");
            imageId = (String) result.get("public_id");

            // delete old image if new uploaded
            if (existing.getPhoto_id() != null) {
                CloudinaryUtil.deleteImage(existing.getPhoto_id());
            }
        }

        TeacherModel updated = new TeacherModel(firstName, lastName, email, dob, mobileNo, gender, district, city, state, nationality, imageUrl, subject ,imageId);
        boolean success = teacherDao.updateTeacher(teacherId, updated);

        if(!success){
            writeJson(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update teacher details", null);
        }

        TeacherModel updatedTeacher = teacherDao.getTeacherById(teacherDao.getTeacherId(email));
        if (updatedTeacher == null) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Teacher updated but could not be retrieved", null);
            return;
        }
        writeJson(resp, HttpServletResponse.SC_CREATED, "Teacher updated successfully", updatedTeacher);

    }

    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, "Unauthorized");
            return;
        }

        int teacherId = Integer.parseInt(req.getParameter("id"));
        TeacherModel teacher = teacherDao.getTeacherById(teacherId);

        if (teacher == null) {
            sendError(resp, "Teacher not found");
            return;
        }

        if (teacher.getPhoto_id() != null) {
            CloudinaryUtil.deleteImage(teacher.getPhoto_id());
        }

        boolean success = teacherDao.deleteTeacher(teacherId);
        writeJson(resp, success ? 200 : 400, success ? "Teacher record deleted successfully" : "Deletion failed", null);
    }



    private void sendError(HttpServletResponse resp, String msg) throws IOException {
        writeJson(resp, 404, msg, null);
    }

    private void writeJson(HttpServletResponse resp, int status, String message, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", status >= 200 && status < 300 ? "success":"error");
        responseMap.put("message", message);
        if(data != null){
            responseMap.put("data", data);
        }

        new Gson().toJson(responseMap, resp.getWriter());

    }


}
