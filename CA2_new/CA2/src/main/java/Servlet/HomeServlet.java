package Servlet;

import Controller.MizDooni;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MizDooni mizDooni = MizDooni.getInstance();
        String loggedInUser = mizDooni.getLoggedInUser().getUsername();
        String page;
        if (loggedInUser != null) {
            if (mizDooni.isManager(loggedInUser)) {
                page = "views/manager_home.jsp"; // Just the JSP filename without /views prefix
            } else {
                page = "views/client_home.jsp"; // Just the JSP filename without /views prefix
            }
        } else {
            page = "views/login.jsp"; // Redirect to login page if user is not logged in
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/" + page);
        requestDispatcher.forward(request, response);
    }
}
