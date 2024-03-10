package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ManagerHomeServlet", value = "/managerHome") // Servlet mapping for ManagerHomeServlet
public class ManagerHomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the manager_home.jsp page
        request.getRequestDispatcher("views/manager_home.jsp").forward(request, response);
    }

}
