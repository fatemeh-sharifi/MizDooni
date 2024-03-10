<%@ page import="Controller.MizDooni" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Client Home</title>
</head>
<body>
<%
    MizDooni mizDooni = MizDooni.getInstance();
    if (!mizDooni.isLoggedIn()) {
        response.sendRedirect(request.getContextPath() + "/login");
    } else {
%>
<h1>Welcome <%= mizDooni.getLoggedInUser().getUsername() %></h1>
<ul>
    <li><a href="/restaurants">Restaurants</a></li>
    <li><a href="/reservations">Reservations</a></li>
</ul>
<a href="/logout" style="color: red">Logout</a>
<%
    }
%>
</body>
</html>
