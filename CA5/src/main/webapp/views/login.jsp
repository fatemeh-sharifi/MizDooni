<%@page import="Service.MizDooni"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        form {
            border-style: double;
            padding: 20px 0;
        }

        form input {
            margin: 10px;
        }

        form button {
            background-color: green;
            color: white;
            width: 25%;
            padding: 2px;
            font-size: 20px;
        }

        form button:hover {
            transform: scale(105%);
            cursor: pointer;
        }

    </style>
</head>

<body style="text-align:center">
<%
    if (MizDooni.getInstance().isLoggedIn()) {
        response.sendRedirect("/");
    } else {
%>
<h1>Welcome to Mizdooni</h1>
<form method="post" action="loginController">
    <label>Username:</label>
    <input name="username" type="text" />
    <br>
    <label>Password:</label>
    <input name="password" type="password" />
    <br>
    <button type="submit">Login!</button>
</form>
<%
    }
%>
</body>

</html>
