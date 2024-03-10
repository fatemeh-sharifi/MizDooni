package Servlet;
import Model.Exception.ExceptionMessages;
import Service.MizDooni;
import java.time.LocalDateTime;
import Model.Feedback.Feedback;
import Model.Reservation.Reservation;
import Model.Restaurant.Restaurant;
import Model.Table.Table;
import Model.User.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RestaurantServlet", urlPatterns = "/restaurants/*")
public class RestaurantServlet extends HttpServlet {
    private final MizDooni mizDooni = MizDooni.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String restaurantId = req.getPathInfo().substring(1); // Extract restaurant_id from URL
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantId);

        if (restaurant != null) {
            req.setAttribute("restaurant", restaurant);
            req.getRequestDispatcher("/views/restaurant.jsp").forward(req, resp);
        } else {
            handleError(req, resp, ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String restaurantId = req.getPathInfo().substring(1); // Extract restaurant_id from URL
        Restaurant restaurant = mizDooni.getRestaurantByName(restaurantId);

        if (restaurant != null) {
            try {
                // Handle feedback submission
                String username = mizDooni.getLoggedInUser().getUsername(); // Get username from logged-in user
                double foodRate = Double.parseDouble(req.getParameter("food_rate"));
                double serviceRate = Double.parseDouble(req.getParameter("service_rate"));
                double ambianceRate = Double.parseDouble(req.getParameter("ambiance_rate"));
                double overallRate = Double.parseDouble(req.getParameter("overall_rate"));
                String comment = req.getParameter("comment");
                LocalDateTime dateTime = LocalDateTime.now(); // Assuming feedback time is current time

                Feedback feedback = new Feedback(username, restaurantId, foodRate, serviceRate, ambianceRate, overallRate, comment, dateTime);
                mizDooni.addFeedback(feedback); // Add or update feedback

                // Update restaurant ratings
                mizDooni.updateRestaurantRatings(restaurantId, foodRate, serviceRate, ambianceRate, overallRate);

                // Redirect to the same page to display updated feedbacks
                resp.sendRedirect(req.getRequestURI());
            } catch (Exception e) {
                handleError(req, resp, "Error processing feedback: " + e.getMessage());
            }
        } else {
            handleError(req, resp, ExceptionMessages.RESERVATION_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(request, response);
    }
}
