package com.controller;

import com.dao.AdminDao;
import com.google.gson.Gson;
import com.model.AdminModel;
import com.utils.CloudinaryUtil;
import com.utils.CookieUtility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig
@WebServlet("/api/admin/*")
public class AdminController extends HttpServlet {

    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "path not found - invalid path");
            return;
        }

        switch (path) {
            case "/change-password" -> changePassword(req, resp);
            case "/update-profile-img" -> updateProfileImage(req, resp);
            default -> sendError(resp, "unknown path");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null){
            sendError(resp, "path not found - invalid path");
            return;
        }

        HttpSession session = req.getSession(false);
        if(session == null){
            writeJson(resp, 401, "Unauthorized. Please login.");
        }

        if("/admin-detail".equals(path)){
            System.out.println("retrieving admin details");
            getAdminDetail(req,resp);
            return;
        }
    }

    private void updateProfileImage(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = CookieUtility.getCookieValue(req, "adminUsername");
        if (username == null) {
            writeJson(resp, 401, "Unauthorized. Please login.");
            return;
        }

        if (!req.getContentType().toLowerCase().startsWith("multipart/form-data")) {
            sendError(resp, "Content-type must be multipart/form-data");
            return;
        }

        Part filePart = req.getPart("profile_image");
        if (filePart == null || filePart.getSize() == 0) {
            writeJson(resp, 401, "No image file provided.");
            return;
        }

        String filename = username + "_profile";
        Map<String, String> result = CloudinaryUtil.uploadImage(filePart.getInputStream(), filename);

        String secureUrl = result.get("secure_url");
//        String photo_id = result.get("public_id");
        if (secureUrl == null ) {
            writeJson(resp, 500, "Image upload failed");
            return;
        }

        boolean newProfileImage = adminDao.updateProfile_image(username, secureUrl);
        writeJson(resp, newProfileImage ? 200 : 500, newProfileImage ? "Profile image updated" : "Database update failed", newProfileImage ? secureUrl : null); // todo send image url too
    }


    private void changePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = CookieUtility.getCookieValue(req, "adminUsername");
        if (username == null) {
            writeJson(resp, 401, "Unauthorized. Please login.");
            return;
        }

        Map<String, String> body = new Gson().fromJson(req.getReader(), Map.class);
        String oldPass = body.get("oldPassword");
        String newPass = body.get("newPassword");

        if (oldPass == null || newPass == null) {
            writeJson(resp, 400, "Missing old or new password");
            return;
        }

        if (!adminDao.validateAdmin(username, oldPass)) {
            writeJson(resp, 401, "Old password incorrect");
            return;
        }

        if (oldPass.equals(newPass)) {
            writeJson(resp, 401, "Your new password cannot be old password");
            return;
        }

        boolean success = adminDao.changePassword(username, newPass); // password hashed is being present in adminDao change password method
        writeJson(resp, success ? 200 : 500, success ? "Password changed" : "Password update failed");
    }

    private void getAdminDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) {
            sendError(resp, "path not found - invalid path");
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null) {
            writeJson(resp, 401, "Unauthorized. Please login.");
            return;
        }

        String username = CookieUtility.getCookieValue(req, "adminUsername");
        if (username == null) {
            writeJson(resp, 401, "Unauthorized. Missing session cookie.");
            return;
        }

        AdminModel adminDetails = adminDao.getAdminByUsername(username);
        if (adminDetails == null) {
            writeJson(resp, 500, "Error retrieving admin details");
            return;
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("username", adminDetails.getUsername());
        responseMap.put("email", adminDetails.getEmail());
        responseMap.put("profile_image", adminDetails.getProfile_image());

        writeJson(resp, responseMap);


    }

    private void writeJson(HttpServletResponse resp, Object obj) {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(200);
            resp.getWriter().write(new Gson().toJson(obj));
        } catch (IOException e) {
            System.out.println("sending json failed");
            e.printStackTrace();
        }
    }

    private void writeJson(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);

        Map<Object, Object> response = new HashMap<>();
        response.put("message", msg);


//        resp.getWriter().print("{\"message\":\"" + msg + "\"}");
        new Gson().toJson(response, resp.getWriter());
    }

    //    for image
    private void writeJson(HttpServletResponse resp, int status, String msg, String updatedUrl) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);

        Map<Object, Object> response = new HashMap<>();
        response.put("message", msg);
        response.put("newUrl", updatedUrl);


//        resp.getWriter().print("{\"message\":\"" + msg + "\"}");
        new Gson().toJson(response, resp.getWriter());
    }


    private void sendError(HttpServletResponse resp, String msg) throws IOException {
        writeJson(resp, 404, msg);
    }
}
