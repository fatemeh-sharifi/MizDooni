package controller.servlets;

import model.restaurant.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RestaurantsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all restaurants from the database or a service
        List<Restaurant> restaurants = fetchRestaurants();

        // Apply search filter if provided
        String searchQuery = request.getParameter("search");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            restaurants = searchRestaurantsByName(restaurants, searchQuery);
        }

        // Apply type filter if provided
        String typeFilter = request.getParameter("type");
        if (typeFilter != null && !typeFilter.isEmpty()) {
            restaurants = filterRestaurantsByType(restaurants, typeFilter);
        }

        // Apply city filter if provided
        String cityFilter = request.getParameter("city");
        if (cityFilter != null && !cityFilter.isEmpty()) {
            restaurants = filterRestaurantsByCity(restaurants, cityFilter);
        }

        // Sort restaurants based on feedback score if requested
        String sortBy = request.getParameter("sort");
        if ("feedback".equalsIgnoreCase(sortBy)) {
            sortRestaurantsByFeedback(restaurants);
        }

        // Set the list of restaurants as an attribute in the request
        request.setAttribute("restaurants", restaurants);

        // Forward the request to the restaurants.jsp for rendering
        request.getRequestDispatcher("/restaurants.jsp").forward(request, response);
    }

    // Method to fetch all restaurants
    private List<Restaurant> fetchRestaurants() {
        // Implementation to fetch restaurants from the database or a service
        return null; // Replace with actual implementation
    }

    // Method to search restaurants by name
    private List<Restaurant> searchRestaurantsByName(List<Restaurant> restaurants, String searchQuery) {
        // Implementation to filter restaurants by name
        return null; // Replace with actual implementation
    }

    // Method to filter restaurants by type
    private List<Restaurant> filterRestaurantsByType(List<Restaurant> restaurants, String typeFilter) {
        // Implementation to filter restaurants by type
        return null; // Replace with actual implementation
    }

    // Method to filter restaurants by city
    private List<Restaurant> filterRestaurantsByCity(List<Restaurant> restaurants, String cityFilter) {
        // Implementation to filter restaurants by city
        return null; // Replace with actual implementation
    }

    // Method to sort restaurants by feedback score
    private void sortRestaurantsByFeedback(List<Restaurant> restaurants) {
        // Implementation to sort restaurants by feedback score
    }
}
