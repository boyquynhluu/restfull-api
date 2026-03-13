package handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import modle.UserModel;
import utils.HttpUtil;

public class MainApp {
    public static void main(String[] args) throws Exception {
        String jsonPost = HttpUtil.post("http://localhost:8000/users", Map.of("1", "Tai", "2", "Hue", "3", "Ngoc Nhi"), 5);
        System.out.println(jsonPost);

        String json = HttpUtil.get("http://localhost:8000/users", new HashMap<>(), 5);
        if (json.contains("[]")) {
            System.out.println("Value: " + json);
        } else {

            ObjectMapper mapper = new ObjectMapper();

            List<UserModel> users = mapper.readValue(json, new TypeReference<List<UserModel>>() {
            });

            for (UserModel u : users) {
                System.out.println(u.getId() + " - " + u.getName());
            }
        }
    }
}
