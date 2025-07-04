package com.controller;

import com.dao.AdminDao;
import com.google.gson.Gson;
import com.model.AdminModel;
import com.utils.CloudinaryUtil;
import com.utils.CookieUtility;
import com.utils.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig
//It tells the servlet container (Tomcat) that this servlet will handle multipart requests (used for file uploads). Without this, calling req.getPart(...) will fail.
@WebServlet("/api/auth/*")
public class AuthController extends HttpServlet {
    private final Gson gson = new Gson();
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        System.out.println(path);
        if(path == null){
            sendError(resp, "path not found - invalid path");
            return;
        }

        switch (path){
            case "/register" -> register(req,resp);
            case "/login" -> {
                if(req.getSession(false) != null){
                    writeJson(resp, 400, "admin is already logged in");
                    return;
                }
                login(req,resp);
            }
            default -> sendError(resp, "unknown path");
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if(path == null){
            sendError(resp, "path not found - invalid path");
            return;
        }

        if("/check-session".equals(path)){
            HttpSession session = req.getSession(false);

            Map<String, Object> responseMap = new HashMap<>();

            if(session != null && session.getAttribute("adminUsername") != null){
                responseMap.put("authenticated", true);
                responseMap.put("admin_username", session.getAttribute("adminUsername"));
            }  else {
                responseMap.put("authenticated", false);
            }
            writeJson(resp, session != null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_FORBIDDEN, responseMap);

        } else if("/logout".equals(path)){
            HttpSession session = req.getSession(false);
            if(session != null){
                session.invalidate();
                CookieUtility.expireCookie(resp, "adminUsername");
                writeJson(resp, 200, "Logged out successfully");
            } else {
                sendError(resp, "admin not logged in");

            }


//            req.getSession().invalidate();
        } else {
            sendError(resp, "Unknown GET path");
        }
    }



    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        AdminModel adminModel = gson.fromJson(req.getReader(), AdminModel.class);
//
//        adminModel.setPassword(PasswordUtil.hashPassword(adminModel.getPassword()));
//        boolean success = adminDao.register(adminModel);
//        writeJson(resp, success ? 200 : 400, success ? "Admin account created" : "registration failed or admin  with this username already exist");

//        1. Accept multipart/form-data instead of JSON.
//        2. Parse text fields (username, password) from form fields.
//        3. Upload image to Cloudinary if it's provided.
//        4. Set image URL in AdminModel or keep it null.

        System.out.println("registering a new admin");
        if(!req.getContentType().toLowerCase().startsWith("multipart/form-data")){
            writeJson(resp, 400, "Content-Type must be multipart/form-data");
            return;
        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");


        if (username == null || password == null || email == null) {
            writeJson(resp, 400, "Username, email and password are required");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);


        // Handle optional image
        System.out.println("getting image");
        Part filePart = req.getPart("profile_image");
        System.out.println("image mil gaya");
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = username + "_profile";
            Map result = CloudinaryUtil.uploadImage(filePart.getInputStream(), fileName);
            imageUrl = (String) result.get("secure_url");

        }

        AdminModel adminModel = new AdminModel(email,username,hashedPassword,imageUrl);


        boolean success = adminDao.register(adminModel);
        writeJson(resp, success ? 200 : 400,
                success ? "Admin account created" : "Registration failed or admin with this username already exists");
    }

//    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
////        AdminModel adminModel = gson.fromJson(req.getReader(), AdminModel.class);
//        String username = req.getParameter("username");
//        String password = req.getParameter("password");
//        System.out.println("Login attempt by: " + username);
//        if(username == null || password == null) {
//            writeJson(resp, 400, "Email and password are required");
//            return;
//        }
//        System.out.println("login");
//        boolean isValid = adminDao.validateAdmin(username, password);
//
//        if(isValid){
//
//            AdminModel admin = adminDao.getAdminByUsername(username);
//            if (admin == null) {
//                writeJson(resp, 500, "Error retrieving admin details");
//                return;
//            }
//
//
//            CookieUtility.createCookie(resp, "adminUsername", username, 24 * 60 * 60); // valid for 24 hrs
//            HttpSession session = req.getSession(true);
//            session.setAttribute("adminUsername", username);
//            Map<Object, Object> responseMap = new HashMap<>();
//            responseMap.put("message", "Login successful");
//            responseMap.put("username", admin.getUsername());
//            responseMap.put("email", admin.getEmail());
//            responseMap.put("profile_image", admin.getProfile_image());
//
//            resp.setContentType("application/json");
//            resp.setStatus(200);
//            resp.getWriter().write(gson.toJson(responseMap));
//
//
//        } else {
//            writeJson(resp, 401, "Invalid credentials");
//        }
//    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        AdminModel loginRequest = gson.fromJson(req.getReader(), AdminModel.class);

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("login attempt by : " + username);

        if(username == null || password == null){
            writeJson(resp, 400, "Username and password are required");
            return;
        }

        boolean isValid = adminDao.validateAdmin(username, password);

        if(isValid){
            AdminModel admin = adminDao.getAdminByUsername(username);
            if(admin == null){
                writeJson(resp, 500, "Error retrieving admin details");
                return;
            }

            CookieUtility.createCookie(resp, "adminUsername", username, 24 * 60 * 60);
            HttpSession session = req.getSession(true);
            session.setAttribute("adminUsername", username);

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Login successful");
            responseMap.put("username", admin.getUsername());
            responseMap.put("email", admin.getEmail());
            responseMap.put("profile_image", admin.getProfile_image());

            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().write(gson.toJson(responseMap));
        } else {
            writeJson(resp, HttpServletResponse.SC_NOT_FOUND, "no admin with this username and password");
        }

    }
    private void writeJson(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(status);
        resp.getWriter().print("{\"message\":\"" + msg + "\"}");
    }

    private void writeJson(HttpServletResponse resp, int status, Object obj) {
        try {

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(status);
            resp.getWriter().write(new Gson().toJson(obj));
        } catch (IOException e) {
            System.out.println("sending json failed");
            e.printStackTrace();
        }
    }

    private void sendError(HttpServletResponse resp, String msg) throws IOException {
        writeJson(resp, 404, msg);
    }
}
