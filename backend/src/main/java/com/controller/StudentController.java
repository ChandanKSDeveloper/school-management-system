package com.controller;

import com.dao.StudentDao;
import com.google.gson.Gson;
import com.model.StudentModel;
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
@WebServlet("/api/student/*")
public class StudentController extends HttpServlet {
    private final StudentDao studentDao = new StudentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "Invalid GET path");
            return;
        }

        if (path.equals("/getStudent")) {
            List<StudentModel> students = studentDao.getAllStudents();
            students.stream().map(student -> String.format("firstName : %s, email : %s", student.getFirst_name(), student.getEmail()));
            writeJson(resp, HttpServletResponse.SC_OK, "Students retrieved successfully", students);
        } else if (path.startsWith("/getStudent/")) { // matches "/123"
            // Extract ID part
            try {
                String idPart = path.substring("/getStudent/".length());
                int studentId = Integer.parseInt(idPart);

                StudentModel student = studentDao.getStudentById(studentId);
                if (student != null) {
                    writeJson(resp, HttpServletResponse.SC_OK, "Student retrieved successfully", student);
                } else {
                    writeJson(resp, HttpServletResponse.SC_NOT_FOUND, "Student not found with ID: " + studentId, null);
                }
            } catch (NumberFormatException e) {
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID format", null);
            }
        } else {
            // Invalid path format
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path format. Use /student/getStudent or /student/getStudent/{id}", null);
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
            addStudent(req, resp);
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
            updateStudent(req, resp);
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
            deleteStudent(req, resp);
        }
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // check if admin is logged in or not
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, "Unauthorized. Please login.");
            return;
        }


        System.out.println("Received content-type: " + req.getContentType());

        if (!req.getContentType().toLowerCase().startsWith("multipart/form-data")) {
            sendError(resp, "Content-type must be multipart/form-data");
            return;
        }

        System.out.println("adding student");


        String first_name = req.getParameter("first_name");
        String last_name = req.getParameter("last_name");
        String father_name = req.getParameter("father_name");
        String email = req.getParameter("email");
        String dobParam = req.getParameter("dob");
        String mobile_no = req.getParameter("mobile_no");
        String gender = req.getParameter("gender");
        String district = req.getParameter("district");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String nationality = req.getParameter("nationality");
//        int classroom = 0;
//        System.out.println("fallback classroom value : " + classroom);
//        System.out.println("receiving classroom value : " + req.getParameter("classroom"));
        String classroomstr = req.getParameter("classroom");
//        System.out.println("receiving classroom value : " + classroom);

        if (classroomstr == null || classroomstr.isEmpty()) {
            sendError(resp, "classroom is required");
            return;
        }

        int classroom;
        try {
            classroom = Integer.parseInt((classroomstr));
            if (classroom < 1 || classroom > 12) {
                sendError(resp, "Classroom must be between 1 and 12.");
                return;
            }
        } catch (NumberFormatException e) {
            sendError(resp, "Invalid classroom number format.");
            return;
        }

        System.out.println(first_name);
        System.out.println(last_name);
        System.out.println(father_name);
        System.out.println(email);
        System.out.println(dobParam);
        System.out.println(gender);
        System.out.println(district);
        System.out.println(city);
        System.out.println(state);
        System.out.println(nationality);
        System.out.println(classroom);

//        we got the value

        if (first_name == null || last_name == null ||
                father_name == null || email == null ||
                dobParam == null || mobile_no == null ||
                gender == null || district == null ||
                city == null || state == null ||
                nationality == null
        ) {

            sendError(resp, "all fields are required");
            return;
        }

        if (classroom < 1 || classroom > 12) {
            sendError(resp, "classroom must be greater than 1 and less than 13");
            return;
        }

        // check if student with this email exist ot not
        if (studentDao.studentExist(email)) { // this mean a student with this email exist
            System.out.println("student with this email exist");
            sendError(resp, "student with this email exist. email must be unique for each student");
            return;
        }

        // parsing dob
        Date dob;
        try {
            LocalDate dobLocal = LocalDate.parse(dobParam); // yyyy-mm-dd
            dob = TimeUtil.toSqlDate(dobLocal);
        } catch (RuntimeException e) {
            sendError(resp, "Invalid date format for dob. Expected yyyy-MM-dd");
            return;
        }

        //photo upload
        System.out.println("processing image");
        Part filePart = req.getPart("photo");
        System.out.println("got the image");
        String imageUrl = null;
        String imageId = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = classroom + "_" + first_name + "_" + father_name + "_photo";
            Map result = CloudinaryUtil.uploadImage(filePart.getInputStream(), fileName);
            imageUrl = (String) result.get("secure_url");
            imageId = (String) result.get("public_id");

        }


//        testing and checking value
        System.out.println(first_name);
        System.out.println(last_name);
        System.out.println(father_name);
        System.out.println(email);
        System.out.println(dobParam + " = " + dob);
        System.out.println(gender);
        System.out.println(district);
        System.out.println(city);
        System.out.println(state);
        System.out.println(nationality);
        System.out.println(classroom);
        System.out.println(imageUrl);
        StudentModel student = new StudentModel(first_name, last_name, father_name, email, dob, mobile_no, gender, district, city, state, nationality, imageUrl, classroom, imageId);

        boolean success = studentDao.createStudent(student);

        if (!success) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add new student", null);
            return;
        }


        StudentModel createdStudent = studentDao.getStudentById(studentDao.getStudentId(email));

        if (createdStudent == null) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Student created but could not be retrieved", null);
            return;
        }
        writeJson(resp, HttpServletResponse.SC_CREATED, "New student added successfully", createdStudent);
//        writeJson(resp, success ? 200 : 400, success ? "new Student added successfully" : "failed to add new student");

    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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

        int studentId = Integer.parseInt(req.getParameter("id"));
        StudentModel existing = studentDao.getStudentById(studentId);
        if (existing == null) {
            sendError(resp, "Student not found");
            return;
        }

        // get updated value
        String firstName = Optional.ofNullable(req.getParameter("first_name")).orElse(existing.getFirst_name());
        String lastName = Optional.ofNullable(req.getParameter("last_name")).orElse(existing.getLast_name());
        String fatherName = Optional.ofNullable(req.getParameter("father_name")).orElse(existing.getFather_name());
        String email = Optional.ofNullable(req.getParameter("email")).orElse(existing.getEmail());
        String mobileNo = Optional.ofNullable(req.getParameter("mobile_no")).orElse(existing.getMobile_no());
        String gender = Optional.ofNullable(req.getParameter("gender")).orElse(existing.getGender());
        String district = Optional.ofNullable(req.getParameter("district")).orElse(existing.getDistrict());
        String city = Optional.ofNullable(req.getParameter("city")).orElse(existing.getCity());
        String state = Optional.ofNullable(req.getParameter("state")).orElse(existing.getState());
        String nationality = Optional.ofNullable(req.getParameter("nationality")).orElse(existing.getNationality());

        int classroom = existing.getClassroom();


        try {
            if (req.getParameter("classroom") != null) {
                classroom = Integer.parseInt(req.getParameter("classroom"));
                if (classroom < 1 || classroom > 12) {
                    sendError(resp, "Classroom must be between 1 and 12");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            sendError(resp, "Invalid classroom value");
            return;
        }

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
        String imageUrl = existing.getPhoto();
        String imageId = existing.getPhoto_id();

        try {
            Part filePart = req.getPart("photo");
            if (filePart != null
                    && filePart.getSize() > 0
                    && filePart.getContentType() != null
                    && filePart.getContentType().startsWith("image/")) {

                String fileName = "student/" + classroom + "_" + firstName + "_" + fatherName + "_photo";
                Map result = CloudinaryUtil.uploadImage(filePart.getInputStream(), fileName);
                imageUrl = (String) result.get("secure_url");
                imageId = (String) result.get("public_id");

                // delete old image if new uploaded
                if (existing.getPhoto_id() != null) {
                    CloudinaryUtil.deleteImage(existing.getPhoto_id());
                }
            }

        } catch (RuntimeException e) {
            System.err.println("Error processing image: " + e.getMessage());
        }

        StudentModel updated = new StudentModel(firstName, lastName, fatherName, email, dob, mobileNo, gender, district, city, state, nationality, imageUrl, classroom, imageId);
        boolean success = studentDao.updateStudent(studentId, updated);

        if (!success) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update student details", null);
        }

        StudentModel updatedStudent = studentDao.getStudentById(studentDao.getStudentId(email));
        if (updatedStudent == null) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Student updated but could not be retrieved", null);
            return;
        }
        writeJson(resp, HttpServletResponse.SC_CREATED, "student updated successfully", updatedStudent);
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, "Unauthorized");
            return;
        }

        String idParam = req.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            writeJson(resp, 400, "Missing ID for deletion");
            return;
        }


        int studentId = Integer.parseInt(idParam);


        StudentModel student = studentDao.getStudentById(studentId);

        if (student == null) {
            sendError(resp, "Student not found");
            return;
        }

        if (student.getPhoto_id() != null) {
            CloudinaryUtil.deleteImage(student.getPhoto_id());
        }

        boolean success = studentDao.deleteStudent(studentId);
        writeJson(resp, success ? 200 : 400, success ? "Student deleted successfully" : "Deletion failed");
    }


    private void writeJson(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        Map<Object, Object> response = new HashMap<>();
        response.put("status_code", status);
        response.put("message", message);

        resp.getWriter().write(new Gson().toJson(response));

    }

    private void writeJson(HttpServletResponse resp, int status, String message, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);


        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", status >= 200 && status < 300 ? "success" : "error");
        responseMap.put("message", message);
        if (data != null) {
            responseMap.put("data", data);
        }

        new Gson().toJson(responseMap, resp.getWriter());
    }

    private void sendError(HttpServletResponse resp, String msg) throws IOException {
        writeJson(resp, 404, msg);
    }

    // Utility method to extract ID from /student/{id}
    private Integer extractIdFromPath(String path) {
        if (path == null || path.split("/").length != 2) {
            return null;
        }

        try {
            System.out.println(path.split("/"));
            System.out.println(path.split("/")[1]);
            return Integer.parseInt(path.split("/")[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
