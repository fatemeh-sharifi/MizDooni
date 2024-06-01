package Controller.Error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Handle the error, e.g., redirect to a custom error page
        return "error"; // Assuming you have an error.html page in your templates folder
    }

//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
}