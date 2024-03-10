package Servlet;

import Controller.MizDooni;
import Controller.RestaurantController;
import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import Model.Restaurant.Restaurant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RestaurantsServlet", urlPatterns = "/restaurants")
public class RestaurantsServlet extends HttpServlet {
    private final MizDooni mizDooni = MizDooni.getInstance();
    private final RestaurantController restaurantController = new RestaurantController();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Restaurant> restaurants = mizDooni.getRestaurants();
            request.setAttribute("restaurants", restaurants);
            request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            try {
                switch (action) {
                    case "search_by_name":
                        handleSearch(request, response, "name");
                        break;
                    case "search_by_type":
                        handleSearch(request, response, "type");
                        break;
                    case "search_by_city":
                        handleSearch(request, response, "city");
                        break;
                    case "sort_by_score":
                        handleSort(request, response);
                        break;
                    case "clear":
                        handleClear(request, response);
                        break;
                    default:
                        handleInvalidAction(request, response);
                        break;
                }
            } catch (Exception e) {
                handleError(request, response, e);
            }
        }
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response, String parameter) throws ServletException, IOException {
        String query = request.getParameter("search_query");
        try {
            validateQueryParameter(query, parameter);
            List<Restaurant> restaurants = getSearchResults(parameter, query);
            request.setAttribute("restaurants", restaurants);
            request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
        } catch (SuperException e) {
            handleError(request, response, e.getMessage());
        }
    }

    private List<Restaurant> getSearchResults(String parameter, String query) throws SuperException {
        switch (parameter) {
            case "name":
                return restaurantController.searchRestaurantsByName(query);
            case "type":
                return restaurantController.searchRestaurantsByType(query);
            case "city":
                return restaurantController.searchRestaurantsByCity(query);
            default:
                throw new SuperException("Unknown parameter: " + parameter);
        }
    }

    private void handleSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Restaurant> sortedRestaurants = restaurantController.sortRestaurantsByScore();
            request.setAttribute("restaurants", sortedRestaurants);
            request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void handleClear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Restaurant> allRestaurants = restaurantController.getRestaurants();
            request.setAttribute("restaurants", allRestaurants);
            request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void handleInvalidAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleError(request, response, "Invalid action.");
    }

    private void validateQueryParameter(String query, String parameterName) throws SuperException {
        if (query == null || query.isEmpty()) {
            switch (parameterName) {
                case "name":
                    throw new SuperException(ExceptionMessages.MISSING_RESTAURANT_NAME);
                case "type":
                    throw new SuperException(ExceptionMessages.MISSING_TYPE);
                case "city":
                    throw new SuperException(ExceptionMessages.MISSING_CITY);
                default:
                    throw new SuperException("Unknown parameter: " + parameterName);
            }
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        handleError(request, response, e.getMessage());
    }
}
