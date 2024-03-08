package controller.servlets;

import controller.logic.FeedbackController;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "feedbackController", value = "/feedbackController")
public class feedbackController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String food_rate = request.getParameter("food_rate");
        String service_rate = request.getParameter("service_rate");
        String ambiance_rate = request.getParameter("ambiance_rate");
        String overall_rate = request.getParameter("overall_rate");
        String comment = request.getParameter("comment");
        String username = request.getParameter("username");
        String restaurantName = request.getParameter("restaurantName");
        if(StringUtils.isBlank(food_rate) || StringUtils.isBlank(service_rate) || StringUtils.isBlank(ambiance_rate)
        || StringUtils.isBlank(overall_rate) || StringUtils.isBlank(comment)) {
            String buyPageName = "/error.jsp";
            request.setAttribute("error", "all feedback field's must be completed!");
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(buyPageName);
            requestDispatcher.forward(request, response);
        } else {

            try{
                FeedbackController.getInstance().addFeedback(username,restaurantName,Double.valueOf(food_rate),Double.valueOf(service_rate),Double.valueOf(ambiance_rate),Double.valueOf(overall_rate),comment);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/restaurant.jsp");
                requestDispatcher.forward(request, response);
            }catch(Exception e){
                String pageName = "/error.jsp";
                request.setAttribute("error", e.getMessage());
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(pageName);
                requestDispatcher.forward(request, response);
            }
        }
    }
}