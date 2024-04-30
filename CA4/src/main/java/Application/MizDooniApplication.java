package Application;

import Service.MizDooni;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"Service", "Controller"})
public class MizDooniApplication {

    public static void main(String[] args) throws IOException {
        MizDooni.getInstance().fetchAndStoreDataFromAPI();
        SpringApplication.run(MizDooniApplication.class, args);

    }
}
