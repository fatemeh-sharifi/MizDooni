package Servlet;

import Controller.AuthenticationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/loginController")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the login page
        request.getRequestDispatcher("views/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve username and password from the request parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check if username or password is empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            handleError(request, response, "All fields must be completed.");
            return;
        }

        // Perform authentication
        try {
            String newPage = AuthenticationController.getInstance().login(username, password);
            // Redirect to the appropriate page after successful login
            response.sendRedirect(newPage);
        } catch (Exception e) {
            // Handle authentication failure
            handleError(request, response, "Username or password is not correct!");
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        // Set error message attribute and forward to error page
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}
