<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<%
    // Get session
    HttpSession currentSession = request.getSession(false);
    User user = null;
    String username = null;

    if (currentSession != null) {
        user = (User) currentSession.getAttribute("user");
        if (user != null) {
            username = user.getUsername();
        } else {
            // Try to get username from session attribute
            username = (String) currentSession.getAttribute("username");
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f5f5f5; }
        .navbar { background: white; padding: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); display: flex; justify-content: space-between; align-items: center; }
        .container { max-width: 1200px; margin: 40px auto; padding: 0 20px; }
        .card { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }
        h1 { color: #333; margin-bottom: 20px; }
        .welcome-text { font-size: 18px; color: #666; margin-bottom: 30px; }
        .btn { padding: 10px 20px; background: #f44336; color: white; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn:hover { background: #d32f2f; }
        .username { color: #4CAF50; font-weight: bold; }
        .info-box { background: #e8f5e9; padding: 20px; border-radius: 5px; margin: 20px 0; }
        .debug-info { background: #f5f5f5; padding: 15px; margin: 20px 0; border-left: 4px solid #2196F3; text-align: left; }
        .debug-info h4 { margin-bottom: 10px; color: #2196F3; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo">Dashboard</div>
        <div>
            <% if (username != null) { %>
                <span>Welcome, <span class="username"><%= username %></span></span>
            <% } else { %>
                <span style="color: red;">Not logged in</span>
            <% } %>
            <a href="logout" class="btn" style="margin-left: 20px;">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <h1>Welcome to Your Dashboard</h1>

            <div class="info-box">
                <% if (username != null) { %>
                    <p class="welcome-text">Hello <span class="username"><%= username %></span>! You have successfully logged in.</p>
                <% } else { %>
                    <p class="welcome-text" style="color: red;">
                        No user session found. Please <a href="login">login</a> again.
                    </p>
                <% } %>
            </div>

            <p>This is your personal dashboard. More features coming soon!</p>

            <!-- Debug Information -->
            <div class="debug-info">
                <h4>Debug Information:</h4>
                <p><strong>Session ID:</strong> <%= currentSession != null ? currentSession.getId() : "No session" %></p>
                <p><strong>Username:</strong> <%= username != null ? username : "null" %></p>
                <p><strong>Session Status:</strong> <%= currentSession != null ? "Active" : "No session" %></p>
            </div>

            <div style="margin-top: 30px;">
                <h3>Quick Links:</h3>
                <p>
                    <a href="debug">View Detailed Session Debug</a> |
                    <a href="test-session">Test Session</a>
                </p>
            </div>
        </div>
    </div>
</body>
</html>