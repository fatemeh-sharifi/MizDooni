package Servlet;

import Service.Mizdooni.MizDooni;
import Model.Table.Table;
import Model.Restaurant.Restaurant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/")
public class HomeServlet extends HttpServlet {

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MizDooni mizDooni = MizDooni.getInstance();
        String loggedInUser = mizDooni.getLoggedInUser().getUsername();
        String page;
        if (loggedInUser != null) {
            if (mizDooni.isManager(loggedInUser)) {
                page = "views/manager_home.jsp";
            } else {
                page = "views/client_home.jsp";
            }
        } else {
            page = "views/login.jsp";
        }
        request.getRequestDispatcher(page).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MizDooni mizDooni = MizDooni.getInstance();
        String loggedInUser = mizDooni.getLoggedInUser().getUsername();
        if (loggedInUser != null && mizDooni.isManager(loggedInUser)) {
            String restaurantName = request.getParameter("restaurant_name");
            String tableNumberStr = request.getParameter("table_number");
            String seatsNumberStr = request.getParameter("seats_number");
            if (restaurantName != null && !restaurantName.isEmpty() && tableNumberStr != null && !tableNumberStr.isEmpty() && seatsNumberStr != null && !seatsNumberStr.isEmpty()) {
                try {
                    int tableNumber = Integer.parseInt(tableNumberStr);
                    int seatsNumber = Integer.parseInt(seatsNumberStr);
                    Table table = new Table(tableNumber, restaurantName, loggedInUser, seatsNumber);
                    Restaurant restaurant = mizDooni.getRestaurantByName(restaurantName);
                    mizDooni.addTable(restaurant, table);
                    // Forward to "done.jsp"
                    request.getRequestDispatcher("/WEB-INF/views/done.jsp").forward(request, response);
                    return; // Stop further execution
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    handleError(request, response, "Invalid input. Please enter valid numbers for table number and seats number.");
                    return;
                }
            } else {
                // Handle missing input
                handleError(request, response, "Restaurant name, table number, and seats number cannot be empty.");
                return;
            }
        } else {
            // Handle user not logged in or not a manager
            handleError(request, response, "You are not authorized to perform this action.");
            return;
        }
    }
}
