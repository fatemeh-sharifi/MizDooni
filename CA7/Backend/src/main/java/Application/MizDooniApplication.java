////package Application;
////
////import Filter.JwtFilter;
////import Service.DataFetch.DataFetchService;
////import org.springframework.boot.SpringApplication;
////import org.springframework.boot.autoconfigure.SpringBootApplication;
////import org.springframework.boot.autoconfigure.domain.EntityScan;
////import org.springframework.boot.web.servlet.FilterRegistrationBean;
////import org.springframework.context.ConfigurableApplicationContext;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.ComponentScan;
////import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
////import org.springframework.web.cors.CorsConfiguration;
////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////import org.springframework.web.filter.CorsFilter;
////
////import java.io.IOException;
////
////@SpringBootApplication
////@ComponentScan(basePackages = {"Service", "Repository", "Entity", "Controller", "Filter"})
////@EnableJpaRepositories(basePackages = {"Repository"})
////@EntityScan(basePackages = {"Entity"})
////public class MizDooniApplication {
////
////    public static void main(String[] args) throws IOException {
////        ConfigurableApplicationContext context = SpringApplication.run(MizDooniApplication.class, args);
////        DataFetchService dataFetchService = context.getBean(DataFetchService.class);
////        // Fetch users and restaurants asynchronously
////        dataFetchService.fetchUsersAndRestaurantsFromApi();
////    }
////
////    @Bean
////    public CorsFilter corsFilter() {
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.addAllowedOrigin("http://localhost:3000");
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("OPTIONS");
////        config.addAllowedMethod("GET");
////        config.addAllowedMethod("POST");
////        config.addAllowedMethod("PUT");
////        config.addAllowedMethod("DELETE");
////        config.addAllowedMethod("*"); // Allow all HTTP methods
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter(source);
////    }
////}
//
//package Application;
//
//import Service.DataFetch.DataFetchService;
//import Utility.JwtUtil;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@SpringBootApplication
//@ComponentScan(basePackages = {"Service", "Repository", "Entity", "Controller", "Filter"})
//@EnableJpaRepositories(basePackages = {"Repository"})
//@EntityScan(basePackages = {"Entity"})
//public class MizDooniApplication {
//
//    public static void main(String[] args) throws IOException {
//        ConfigurableApplicationContext context = SpringApplication.run(MizDooniApplication.class, args);
//        DataFetchService dataFetchService = context.getBean(DataFetchService.class);
//        // Fetch users and restaurants asynchronously
//        dataFetchService.fetchUsersAndRestaurantsFromApi();
//    }
//
////    @Bean
////    public CorsFilter corsFilter() {
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.addAllowedOrigin("http://localhost:3000");
////        config.addAllowedHeader("*");
////        config.addAllowedMethod("OPTIONS");
////        config.addAllowedMethod("GET");
////        config.addAllowedMethod("POST");
////        config.addAllowedMethod("PUT");
////        config.addAllowedMethod("DELETE");
////        config.addAllowedMethod("*"); // Allow all HTTP methods
////        source.registerCorsConfiguration("/**", config);
////        return new CorsFilter(source);
////    }
//
//    @Bean
//    public FilterRegistrationBean<HttpFilter> jwtFilter() {
//        FilterRegistrationBean<HttpFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new HttpFilter() {
//            @Override
//            protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//                String authorizationHeader = request.getHeader("Authorization");
//                System.out.println ("_______________________________________hello_______________________________________________" );
//                if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
//                    return;
//                }
//
//                String token = authorizationHeader.substring(7);
//                if (!JwtUtil.validateToken(token)) {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//                    return;
//                }
//
//                String userEmail = JwtUtil.getUserEmailFromToken(token);
//                request.setAttribute("userEmail", userEmail);
//                chain.doFilter(request, response);
//            }
//        });
//        registrationBean.addUrlPatterns("/*"); // Define URL patterns to which the filter should be applied
//        return registrationBean;
//    }
//}

package Application;

import Service.DataFetch.DataFetchService;
import Utility.JwtUtil;
import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"Service", "Repository", "Entity", "Controller", "Filter"})
@EnableJpaRepositories(basePackages = {"Repository"})
@EntityScan(basePackages = {"Entity"})
public class MizDooniApplication {

    public static void main(String[] args) throws IOException {
        ElasticApmAttacher.attach();
        ConfigurableApplicationContext context = SpringApplication.run(MizDooniApplication.class, args);
        DataFetchService dataFetchService = context.getBean(DataFetchService.class);
        // Fetch users and restaurants asynchronously
        dataFetchService.fetchUsersAndRestaurantsFromApi();
    }

    @Bean
    public FilterRegistrationBean<HttpFilter> jwtFilter() {
        FilterRegistrationBean<HttpFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HttpFilter() {
            @Override
            protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // Adjust this to your frontend's URL
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
                response.setHeader("Access-Control-Allow-Credentials", "true");

                if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
                if(request.getServletPath().contains ( "login" ) || request.getServletPath ().contains ( "signup" ) || request.getServletPath ().contains ( "callback" )||request.getServletPath ().contains ( "svg" )||request.getServletPath ().contains ( "png" )){
                    chain.doFilter ( request, response );
                    System.out.println ("I AM RETURNING!-------------------------------------------------" );
                    return;
                }
                System.out.println ("jwtfilter---------------------------------------------------------" );
                System.out.println (request.getServletPath () );
                System.out.println (request.getHeader ("Origin"));
                System.out.println (request.getHeader ("Content-Type"  ) );
                System.out.println (request.getHeader ("Accept"  ) );
                String authorizationHeader = request.getHeader("Authorization");
                System.out.println ( authorizationHeader);
                if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
                    return;
                }

                String token = authorizationHeader.substring(7);
                if (!JwtUtil.validateToken(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                String userEmail = JwtUtil.getUserEmailFromToken(token);
                request.setAttribute("userEmail", userEmail);
                chain.doFilter(request, response);
            }
        });
        registrationBean.addUrlPatterns("/*"); // Define URL patterns to which the filter should be applied
        return registrationBean;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("*"); // Allow all HTTP methods
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
