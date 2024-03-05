package servletControllers;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import controller.AuthenticationController;


@WebServlet(name = "loginController", value = "/loginController")
public class loginController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            String buyPageName = "/login.jsp";
            request.setAttribute("emptyField", "true");
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(buyPageName);
            requestDispatcher.forward(request, response);
        } else {

            try{
                String newPage = AuthenticationController.getInstance().login(username,password);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(newPage);
                requestDispatcher.forward(request, response);
            }catch(Exception e){
                String buyPageName = "/login.jsp";
                request.setAttribute("wrongData", "true");
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(buyPageName);
                requestDispatcher.forward(request, response);
            }
        }
    }
}