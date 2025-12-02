<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f5f5f5; }
        .container { max-width: 800px; margin: 100px auto; padding: 40px; background: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }
        h1 { color: #333; margin-bottom: 30px; }
        .buttons { display: flex; justify-content: center; gap: 20px; margin-top: 30px; }
        .btn { padding: 12px 30px; font-size: 16px; border: none; border-radius: 5px; cursor: pointer; text-decoration: none; display: inline-block; }
        .btn-login { background: #4CAF50; color: white; }
        .btn-register { background: #2196F3; color: white; }
        .btn:hover { opacity: 0.9; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to Our Application</h1>
        <p>Please login or register to continue</p>
        <div class="buttons">
            <a href="login" class="btn btn-login">Login</a>
            <a href="register" class="btn btn-register">Register</a>
        </div>
    </div>
</body>
</html>