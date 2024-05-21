//package Filter;
//
//import Utility.JwtUtil;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtFilter extends HttpFilter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        super.init(filterConfig);
//    }
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        String authorizationHeader = request.getHeader("Authorization");
//        System.out.println("Authorization Header: " + authorizationHeader);
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
//            return;
//        }
//
//        String token = authorizationHeader.substring(7);
//        if (!JwtUtil.validateToken(token)) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//            return;
//        }
//
//        String userEmail = JwtUtil.getUserEmailFromToken(token);
//        System.out.println("User Email from Token: " + userEmail);
//        request.setAttribute("userEmail", userEmail);
//        chain.doFilter(request, response);
//    }
//
////    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
////        String authorizationHeader = request.getHeader("Authorization");
////        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
////            return;
////        }
////
////        String token = authorizationHeader.substring(7);
////        if (!JwtUtil.validateToken(token)) {
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
////            return;
////        }
////
////        String userEmail = JwtUtil.getUserEmailFromToken(token);
////        request.setAttribute("userEmail", userEmail);
////        chain.doFilter(request, response);
////    }
//
//    @Override
//    public void destroy() {
//        super.destroy();
//    }
//}
