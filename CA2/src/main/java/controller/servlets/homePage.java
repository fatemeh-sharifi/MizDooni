package controller.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import service.MizDooni;

@WebServlet(name = "homePage", value = "/")
public class homePage extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MizDooni mizDooni = MizDooni.getInstance();
        boolean res = mizDooni.isManager(mizDooni.getLoggedInUser().getUsername());
        String page;
        if(res){
            page = "manager_home.jsp";
        }
        else {
            page = "client_home.jsp";
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(page);
        requestDispatcher.forward(request, response);
    }
}