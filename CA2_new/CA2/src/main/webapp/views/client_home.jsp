<%@page import="Controller.MizDooni"%>
<html lang="en"><head>
    <meta charset="UTF-8">
    <title>Client Home</title>
</head>
<body>
<%
    MizDooni mizDooni = MizDooni.getInstance();
    if (!mizDooni.isLoggedIn() ){
        response.sendRedirect("views/login.jsp");
    }
    else{
%>
<h1>Welcome <%= mizDooni.getLoggedInUser().getUsername() %><a href="/logout" style="color: red">Log Out</a></h1>


<ul type="square">
    <li>
        <a href="/restaurants">Restaurants</a>
    </li>
    <li>
        <a href="/reservations">Reservations</a>
    </li>
</ul>

<%
    }
%>
</body>
</html>