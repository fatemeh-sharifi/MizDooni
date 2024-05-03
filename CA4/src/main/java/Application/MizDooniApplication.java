//package Application;
//
//import Service.MizDooni;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.io.IOException;
//
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages = {"Service", "Controller"})
//public class MizDooniApplication {
//
//    public static void main(String[] args) throws IOException {
//        MizDooni.getInstance().fetchAndStoreDataFromAPI();
//        SpringApplication.run(MizDooniApplication.class, args);
//
//    }
//}
package Application;

import Service.MizDooni;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"Service", "Controller"})
public class MizDooniApplication {

    public static void main(String[] args) throws IOException {
        MizDooni.getInstance().fetchAndStoreDataFromAPI();
        SpringApplication.run(MizDooniApplication.class, args);
    }

    // CORS configuration
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:3000"); // Specify your frontend origin
            }
        };
    }
}
