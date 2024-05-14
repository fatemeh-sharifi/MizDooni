package API;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RestaurantReservationSystem {

    public static void main(String[] args) throws IOException {
        // Create an HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Define the endpoint URLs
        String restaurantsEndpoint = "http://91.107.137.117:55/restaurants";
        String reviewsEndpoint = "http://91.107.137.117:55/reviews";
        String usersEndpoint = "http://91.107.137.117:55/users";
        String tablesEndpoint = "http://91.107.137.117:55/tables";

        // Make a GET request to the restaurants endpoint
        CloseableHttpResponse restaurantsResponse = httpClient.execute(new HttpGet(restaurantsEndpoint));
        printResponse(restaurantsResponse);

        // Make a GET request to the reviews endpoint
        CloseableHttpResponse reviewsResponse = httpClient.execute(new HttpGet(reviewsEndpoint));
        printResponse(reviewsResponse);

        // Make a GET request to the users endpoint
        CloseableHttpResponse usersResponse = httpClient.execute(new HttpGet(usersEndpoint));
        printResponse(usersResponse);

        // Make a GET request to the tables endpoint
        CloseableHttpResponse tablesResponse = httpClient.execute(new HttpGet(tablesEndpoint));
        printResponse(tablesResponse);

        // Close the HTTP client
        httpClient.close();
    }

    // Helper method to print HTTP response
    private static void printResponse(CloseableHttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        response.close();
    }
}
