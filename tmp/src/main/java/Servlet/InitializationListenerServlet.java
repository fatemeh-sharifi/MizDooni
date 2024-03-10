package Servlet;

import Service.MizDooni;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class InitializationListenerServlet implements ServletContextListener {

    private MizDooni mizDooni = MizDooni.getInstance();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            mizDooni.loadUsersFromJson();
            mizDooni.loadRestaurantsFromJson();
//            mizDooni.loadFeedbacksFromJson();
            System.out.println("number of pre-defined restaurants: " + mizDooni.getRestaurants().size());
            System.out.println("number of pre-defined users: " + mizDooni.getUsers().size());
//            System.out.println("number of pre-defined feedbacks: " + mizDooni.getFeedbacks().size());
        } catch (Exception e) {
            System.err.println("Error loading users and restaurants and feedbacks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}

