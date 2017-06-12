import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 212546734 on 23.05.2017.
 */
public class Test {
    public static void main(String[] args) throws IOException, ParseException {

/*
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("c:\\Projects\\predix\\tulipeIsland.json"));

        JSONObject jsonObject1 = (JSONObject) obj;

        for (Object o1 : jsonObject1.<String, Object>entrySet()) {
            Map.Entry mapEntry1 = (Map.Entry) o1;
            System.out.println(mapEntry1.getKey());
            if (mapEntry1.getValue() instanceof JSONArray){
                JSONArray array1 = (JSONArray)mapEntry1.getValue();
                JSONObject jsonObject2 = (JSONObject) array1.get(0);
                for (Object o2 : jsonObject2.entrySet()) {
                    Map.Entry mapEntry2 = (Map.Entry) o2;
                    System.out.println("   ---" + mapEntry2.getKey());
                }
                System.out.println(jsonObject2.getClass());
                jsonObject2.keySet().iterator().next();
            }

        }
        System.out.println();
*/




        ObjectMapper mapper = new ObjectMapper();
        JacksonModel staff = mapper.readValue(new File("c:\\Projects\\predix\\tulipeIsland.json"), JacksonModel.class);

        System.out.println("done");

    }
}
