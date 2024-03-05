<%@page import="service.MizDooni"%>
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
        if (MizDooni.getInstance().isLoggedIn() && MizDooni.getInstance().isManager(MizDooni.getInstance().getLoggedInUser().getUsername())) {
            response.sendRedirect("manager_home.jsp");
        } else if(MizDooni.getInstance().isLoggedIn() && !MizDooni.getInstance().isManager(MizDooni.getInstance().getLoggedInUser().getUsername())) {
            response.sendRedirect("client_home.jsp");
        }
        else{
    %>
        <h1>Welcome to Mizdooni</h1>
        <form method="get" action="loginController">
            <label>Username:</label>
            <input name="username" type="text" />
            <br>
            <label>Password:</label>
            <input name="password" type="password" />
            <br>
            <button type="submit">Login!</button>
            <%if(request.getParameter("wrongData") != null) {%>
            <h5>username or password is not correct!</h5>
            <%
                }
            %>
        </form>
    <%
        }
    %>

    </body>
</html>