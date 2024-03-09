package servlet;

import Controller.MizDooni;
import Controller.RestaurantController;
import Model.Restaurant.Restaurant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/restaurants")
public class RestaurantsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MizDooni mizDooni = MizDooni.getInstance();
    private RestaurantController restaurantController = new RestaurantController();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Restaurant> restaurants = mizDooni.getRestaurants();
        request.setAttribute("restaurants", restaurants);
        request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "search_by_name":
                    String nameQuery = request.getParameter("search_query");
                    List<Restaurant> restaurantsByName = restaurantController.searchRestaurantsByName(nameQuery);
                    request.setAttribute("restaurants", restaurantsByName);
                    break;
                case "search_by_type":
                    String typeQuery = request.getParameter("search_query");
                    List<Restaurant> restaurantsByType = restaurantController.searchRestaurantsByType(typeQuery);
                    request.setAttribute("restaurants", restaurantsByType);
                    break;
                case "search_by_city":
                    String cityQuery = request.getParameter("search_query");
                    List<Restaurant> restaurantsByCity = restaurantController.searchRestaurantsByCity(cityQuery);
                    request.setAttribute("restaurants", restaurantsByCity);
                    break;
                case "sort_by_score":
                    List<Restaurant> sortedRestaurants = restaurantController.sortRestaurantsByScore();
                    request.setAttribute("restaurants", sortedRestaurants);
                    break;
                case "clear":
                    List<Restaurant> allRestaurants = restaurantController.getRestaurants();
                    request.setAttribute("restaurants", allRestaurants);
                    break;
                default:
                    // Handle unknown action
                    break;
            }
        }

        request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
    }
}
