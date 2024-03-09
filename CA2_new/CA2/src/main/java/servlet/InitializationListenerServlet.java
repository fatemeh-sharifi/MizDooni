package servlet;

import controller.mizdooni.MizDooni;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class InitializationListenerServlet implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // Load users and restaurants from JSON files
        try {
            MizDooni.getInstance().loadUsersFromJson();
            MizDooni.getInstance().loadRestaurantsFromJson();
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

