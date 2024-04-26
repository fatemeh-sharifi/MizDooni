<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant</title>
</head>
<body>
<p id="username">username: ${loggedInUser.getUsername()} <a href="/">Home</a> <a href="/logout" style="color: red">Log Out</a></p>
<br>
<h2>Restaurant Info:</h2>
<ul>
    <li id="id">Id: ${restaurant.getId()}</li>
    <li id="name">Name: ${restaurant.getName()}</li>
    <li id="type">Type: ${restaurant.getType()}</li>
    <li id="time">Time: ${restaurant.getStartTime()} - ${restaurant.getEndTime()}</li>
    <li id="rate">Scores:</li>
    <ul>
        <li>Food: ${restaurant.getFoodAvg()}</li>
        <li>Service: ${restaurant.getServiceAvg()}</li>
        <li>Ambiance: ${restaurant.getAmbianceAvg()}</li>
        <li>Overall: ${restaurant.getOverallAvg()}</li>
    </ul>
    <li id="address">Address: ${restaurant.getAddress().getStreet()}, ${restaurant.getAddress().getCity()}, ${restaurant.getAddress().getCountry()}</li>
    <li id="description">Description: ${restaurant.getDescription()}</li>
</ul>

<form action="" method="post">
    <label>Food Rate:</label>
    <input type="number" id="food_rate" name="food_rate" step="0.1" min="0" max="5">
    <label>Service Rate:</label>
    <input type="number" id="service_rate" name="service_rate" step="0.1" min="0" max="5">
    <label>Ambiance Rate:</label>
    <input type="number" id="ambiance_rate" name="ambiance_rate" step="0.1" min="0" max="5">
    <label>Overall Rate:</label>
    <input type="number" id="overall_rate" name="overall_rate" step="0.1" min="0" max="5">
    <br>
    <label>Comment:</label>
    <textarea name="comment" id="comment" cols="30" rows="5" placeholder="Enter your comment"></textarea>
    <br>
    <button type="submit" name="action" value="feedback">Submit Feedback</button>
</form>

<br>

<!-- Display existing feedbacks -->
<table style="width: 100%; text-align: center;" border="1">
    <caption><h2>Feedbacks</h2></caption>
    <tr>
        <th>Username</th>
        <th>Comment</th>
        <th>Date</th>
        <th>Food Rate</th>
        <th>Service Rate</th>
        <th>Ambiance Rate</th>
        <th>Overall Rate</th>
    </tr>
    <c:forEach var="feedback" items="${mizDooni.getFeedbacksByUsernameAndRestaurantName(loggedInUser.getUsername(), restaurant.getName())}">
        <!-- Display feedback details -->
        <tr>
            <td>${feedback.getUsername()}</td>
            <td>${feedback.getComment()}</td>
            <td>${feedback.getDateTime()}</td>
            <td>${feedback.getFoodRate()}</td>
            <td>${feedback.getServiceRate()}</td>
            <td>${feedback.getAmbianceRate()}</td>
            <td>${feedback.getOverallRate()}</td>
        </tr>
    </c:forEach>

</table>
<br><br>

</body>
</html>
