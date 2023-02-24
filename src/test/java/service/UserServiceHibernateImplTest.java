package service;

import model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceHibernateImplTest {
    private final UserService userService = new UserServiceHibernateImpl();
    private final String testName = "Oleg";
    private final String testLastName = "Olegovich";
    private final byte testAge = 45;

    @Test
    void createUserTable() {
        try {
            userService.dropUserTable();
            userService.createUserTable();
        } catch(Exception e) {
            fail("При создание таблицы произошла ошибка\n" + e.getMessage());
        }
    }

    @Test
    void dropUserTable() {
        try {
            userService.dropUserTable();
        } catch(Exception e) {
            fail("При удаление таблицы произошла ошибка\n" + e.getMessage());
        }
    }

    @Test
    void saveUser() {
        try {
            userService.dropUserTable();
            userService.createUserTable();
            userService.saveUser(testName, testLastName, testAge);

            User user = userService.getAllUsers().get(0);
            if(!testName.equals(user.getName()) || !testLastName.equals(user.getLastName()) || testAge != user.getAge()) {
                fail("пользователь не был сохранён в базу данных");
            }
        } catch(Exception e) {
            fail("При сохранение пользователя в базу произошла ошибка\n" + e.getMessage());
        }
    }

    @Test
    void removeUserById() {
        try {
            userService.dropUserTable();
            userService.createUserTable();
            userService.saveUser(testName, testLastName,testAge);
            userService.removeUserById(1);
        } catch(Exception e) {
            fail("При удаления пользователя по id произошла ошибка\n" + e.getMessage());
        }
    }

    @Test
    void getAllUsers() {
        try {
            userService.dropUserTable();
            userService.createUserTable();
            userService.saveUser(testName, testLastName,testAge);
            List<User> list = userService.getAllUsers();
            if(list.size() != 1) {
                fail("При проверке размерности произошла ошибка\n");
            }
        } catch(Exception e) {
            fail("При получение пользователей произошла ошибка\n" + e.getMessage());
        }
    }

    @Test
    void clearUserTable() {
        try {
            userService.dropUserTable();
            userService.createUserTable();
            userService.saveUser(testName, testLastName,testAge);
            userService.clearUserTable();
            if(userService.getAllUsers().size() != 0) {
                fail("Очистка произведена некоректно\n");
            }
        } catch(Exception e) {
            fail("При очистки таблицы произошла ошибка\n" + e.getMessage());
        }
    }
}