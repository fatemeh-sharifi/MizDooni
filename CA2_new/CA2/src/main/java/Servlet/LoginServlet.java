package Servlet;

import Controller.AuthenticationController;
import Model.Exception.ExceptionMessages;
import Model.Exception.SuperException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/loginController")
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            handleError(request, response, ExceptionMessages.MISSING_USERNAME_PASSWORD);
            return;
        }

        try {
            String newPage = AuthenticationController.getInstance().login(username, password);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (SuperException e) {
            handleError(request, response, e.getMessage());
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error.jsp");
        requestDispatcher.forward(request, response);
    }
}
