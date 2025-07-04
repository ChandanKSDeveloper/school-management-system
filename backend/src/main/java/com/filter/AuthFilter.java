package com.filter;

import com.utils.CookieUtility;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/admin/*", "/api/student/*", "/api/teacher/*"} )
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

//        String path = req.getRequestURI();
        HttpSession session = req.getSession(false);
        String usernameFromCookie = CookieUtility.getCookieValue(req, "adminUsername");

//        // ✅ Allow unauthenticated access to login and register only
//        if(path.endsWith("/login") || path.endsWith("/register")){
//            chain.doFilter(request,response);
//            return;
//        }

        if(session == null || usernameFromCookie == null){
            resp.setContentType("application/json");
            resp.setStatus(401);
            resp.getWriter().write("{\"message\": \"Unauthorized. Please login or signup.\"}");
            return;
        }

        // ✅ User is authenticated
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
