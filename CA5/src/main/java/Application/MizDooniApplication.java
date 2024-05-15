//package Application;
//
//import Service.Mizdooni.MizDooni;
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

import Service.DataFetchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"Service", "Controller", "Repository", "Entity", "DAO"})
@EnableJpaRepositories(basePackages = {"Repository"})
@EntityScan(basePackages = {"Entity"})
public class MizDooniApplication {

    public static void main(String[] args) throws IOException {
//        MizDooni.getInstance().fetchAndStoreDataFromAPI();
        ConfigurableApplicationContext context = SpringApplication.run(MizDooniApplication.class, args);
        DataFetchService dataFetchService = context.getBean(DataFetchService.class);
          // Fetch users and restaurants asynchronously
        dataFetchService.fetchUsersAndRestaurantsFromApi();
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
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
