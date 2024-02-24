package domain.user;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class user {
    String path = "./resources/user.json";
    public void addUser(String data){
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(path);
            JSONObject users = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
