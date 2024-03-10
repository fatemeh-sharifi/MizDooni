//package Filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import java.io.IOException;
//
//@WebFilter("/error")
//public class ErrorHandlingFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        request.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(request, response);
//    }
//}
