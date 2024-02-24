package com.reservation.controller;

import com.reservation.model.User;
import com.reservation.service.UserService;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void handleAddUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Reading JSON data from request
        JSONObject jsonData = (JSONObject) request.getAttribute("jsonData");

        // Parsing JSON data to create User object
        User user = new User();
        user.setUsername((String) jsonData.get("username"));
        user.setPassword((String) jsonData.get("password"));
        user.setEmail((String) jsonData.get("email"));


        // Adding user
        try {
            userService.addUser(user);
            response.getWriter().println("{\"success\": true, \"data\": \"User added successfully.\"}");
        } catch (Exception e) {
            response.getWriter().println("{\"success\": false, \"data\": \"" + e.getMessage() + "\"}");
        }
    }
}
