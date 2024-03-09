<%@ page import="Model.Restaurant.Restaurant" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurants</title>
</head>
<body>
<p id="username">username: ali <a href="/">Home</a> <a href="/logout" style="color: red">Log Out</a></p>
<br><br>
<form action="/CA2/restaurants" method="POST">
    <label>Search:</label>
    <input type="text" name="search_query" value="">
    <button type="submit" name="action" value="search_by_type">Search By Type</button>
    <button type="submit" name="action" value="search_by_name">Search By Name</button>
    <button type="submit" name="action" value="search_by_city">Search By City</button>
    <button type="submit" name="action" value="clear">Clear Search</button>
</form>
<br><br>
<form action="/CA2/restaurants" method="POST">
    <label>Sort By:</label>
    <button type="submit" name="action" value="sort_by_score">Overall Score</button>
</form>
<br><br>
<table style="width:100%; text-align:center;" border="1">
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>City</th>
        <th>Type</th>
        <th>Time</th>
        <th>Service Score</th>
        <th>Food Score</th>
        <th>Ambiance Score</th>
        <th>Overall Score</th>
    </tr>
    <% List<Restaurant> restaurants = (List<Restaurant>) request.getAttribute("restaurants");
        if (restaurants != null) {
            for (Restaurant restaurant : restaurants) {
    %>
    <tr>
        <td><%= restaurant.getId() %></td>
        <td><a href="/restaurants/<%= restaurant.getId() %>"><%= restaurant.getName() %></a></td>
        <td><%= restaurant.getAddress().getCity() %></td>
        <td><%= restaurant.getType() %></td>
        <td><%= restaurant.getStartTime() %> - <%= restaurant.getEndTime() %></td>
        <td><%= restaurant.getServiceAvg() %></td>
        <td><%= restaurant.getFoodAvg() %></td>
        <td><%= restaurant.getAmbianceAvg() %></td>
        <td><%= (restaurant.getServiceAvg() + restaurant.getFoodAvg() + restaurant.getAmbianceAvg()) / 3 %></td>
    </tr>
    <% }
    }
    %>
</table>
</body>
</html>
