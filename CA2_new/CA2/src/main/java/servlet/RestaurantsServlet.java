package servlet;

import Controller.MizDooni;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Restaurant> restaurants = mizDooni.getRestaurants();

        request.setAttribute("restaurants", restaurants);

        System.out.println(restaurants.size());
        request.getRequestDispatcher("/views/restaurants.jsp").forward(request, response);
    }
}
