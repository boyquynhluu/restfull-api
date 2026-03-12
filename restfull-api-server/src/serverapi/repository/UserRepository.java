package serverapi.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import serverapi.model.User;

public class UserRepository {

    private static Map<Integer, User> users = new HashMap<>();
    private static int idCounter = 1;

    public static List<User> findAll() {
        return Arrays.asList(new User(1, "Tai"), new User(2, "Hue"));
    }

    public static User findById(int id) {
        return users.get(id);
    }

    public static User save(String name) {
        User user = new User(idCounter++, name);
        users.put(user.getId(), user);
        return user;
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
