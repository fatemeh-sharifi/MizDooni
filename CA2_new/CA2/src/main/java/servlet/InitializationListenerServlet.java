package servlet;

import Controller.MizDooni;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class InitializationListenerServlet implements ServletContextListener {

    private MizDooni mizDooni = MizDooni.getInstance();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // Load users and restaurants from JSON files
        try {
            mizDooni.loadUsersFromJson();
            mizDooni.loadRestaurantsFromJson();
            System.out.println(mizDooni.getRestaurants().size());
            System.out.println(mizDooni.getUsers().size());
            System.out.println("Users and restaurants loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error loading users and restaurants: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}

