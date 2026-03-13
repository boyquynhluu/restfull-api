package serverapi.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import serverapi.model.User;

public class UserRepository {

    private static Map<Integer, User> users = new HashMap<>();
    private static int idCounter = 1;

    public static List<User> findAll() {
        return users.values().stream().collect(Collectors.toList());
    }

    public static User findById(int id) {
        return users.get(id);
    }

    public static void save(int id, String name) {
        users.put(id, new User(id, name));
    }

    public static User update(int id, String name) {
        User user = users.get(id);
        if (user != null) {
            user.setName(name);
        }
        return user;
    }

    public static void delete(int id) {
        users.remove(id);
    }
}
