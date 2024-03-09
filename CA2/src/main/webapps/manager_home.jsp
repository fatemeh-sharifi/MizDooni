<%@page import="service.MizDooni"%>
<html lang="en"><head>
    <meta charset="UTF-8">
    <title>Manager Home</title>
</head>
<body>
<%
    MizDooni mizDooni = MizDooni.getInstance();
    if (!mizDooni.isLoggedIn() ){
        response.sendRedirect("login.jsp");
    }
    else{
%>
<h1>Welcome <%= mizDooni.getLoggedInUser().getUsername() %><a href="/logout" style="color: red">Log Out</a></h1>

<h2>Your Restaurant Information:</h2>
<%= mizDooni.createManagerRestaurantHtml(mizDooni.getLoggedInUser().getUsername())%>

<table border="1" cellpadding="10">
    <tr>
        <td>

            <h3>Add Table:</h3>
            <form method="post" action="">
                <label>Table Number:</label>
                <input name="table_number" type="number" min="0"/>
                <br>
                <label>Seats Number:</label>
                <input name="seats_number" type="number" min="1"/>
                <br>
                <button type="submit">Add</button>
            </form>

        </td>
    </tr>
</table>

<%
    }
%>
</body>
</html>