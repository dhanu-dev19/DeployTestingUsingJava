// src/main/java/com/example/servlet/DashboardServlet.java
package com.example.servlet;

import com.example.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if session exists and user is logged in
        if (session == null) {
            response.sendRedirect("login?message=Session expired. Please login again.");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login?message=Please login to access dashboard.");
            return;
        }

        // Make sure all attributes are set
        session.setAttribute("username", user.getUsername());
        request.setAttribute("user", user);
        request.setAttribute("username", user.getUsername());
        request.setAttribute("userId", user.getId());
        request.setAttribute("email", user.getEmail());

        RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}