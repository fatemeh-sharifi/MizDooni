<%@ page import="Service.Mizdooni.MizDooni" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Restaurant.Restaurant" %>
<%@ page import="Model.Table.Table" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manager Home</title>
</head>
<body>
<%
    MizDooni mizDooni = MizDooni.getInstance();
    if (!mizDooni.isLoggedIn()) {
        response.sendRedirect(request.getContextPath() + "/login");
    } else if (!mizDooni.isManager(mizDooni.getLoggedInUser().getUsername())) {
        response.sendRedirect(request.getContextPath() + "/client_home.jsp");
    } else {
%>
<h1>Welcome <%= mizDooni.getLoggedInUser().getUsername() %></h1>
<a href="/logout" style="color: red">Logout</a>
<h2>Your Restaurant Information:</h2>
<ul>
    <%
        String loggedInUsername = mizDooni.getLoggedInUser().getUsername();
        List<Restaurant> restaurants = mizDooni.getRestaurants();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getManagerUsername().equals(loggedInUsername)) {
    %>
    <li id="id">Id: <%= restaurant.getId() %></li>
    <li id="name">Name: <%= restaurant.getName() %></li>
    <li id="type">Type: <%= restaurant.getType() %></li>
    <li id="time">Time: <%= restaurant.getStartTime() %> - <%= restaurant.getEndTime() %></li>
    <li id="description">Description: <%= restaurant.getDescription() %></li>
    <li id="address">Address: <%= restaurant.getAddress().getStreet() + ", " + restaurant.getAddress().getCity() + ", " + restaurant.getAddress().getCountry() %></li>
    <li id="tables">Tables:</li>
    <ul>
        <%
            List<Table> tables = restaurant.getTables();
            for (Table table : tables) {
        %>
        <li><%= "Table " + table.getTableNumber()%></li>
        <% } %>
    </ul>
    <%
            }
        }
    %>
</ul>

<table border="1" cellpadding="10">
    <tr>
        <td>
            <h3>Add Table:</h3>
            <form method="post">
                <label>Restaurant Name:</label>
                <input name="restaurant_name" type="text"/>
                <br>
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
