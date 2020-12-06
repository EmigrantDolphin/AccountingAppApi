package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.User;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.UserView;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class UserManager {

    private static User loggedInUser = null;

    private final UserRepository userRepository = new UserRepository();

    private static UserManager userManager;

    public static UserManager getUserManager(){
        if (userManager == null){
            userManager = new UserManager();
        }
        return userManager;
    }

    public static UserView getLoggedInUser() {
        return loggedInUser ;
    }

    public void login(String username, String password) {
        ArrayList<User> allUsers = new ArrayList<>();
        try{
            allUsers = read();
        }catch (Exception e){

        }
        loggedInUser = allUsers.stream()
                .filter(u -> u.username.equals(username) && u.password.equals(password))
                .findFirst()
                .orElse(null);
    }

    public void add(User item) throws Exception {
        if (loggedInUser != null && loggedInUser.isSystemAdmin){

            try{
                userRepository.add(item);
            }catch (SQLException e){
                throw new Exception(e.getMessage());
            }
        }else{
            throw new Exception("You are not an admin. Only admins can add users.");
        }
    }

    public void remove(User item) throws Exception {
        if (loggedInUser != null && loggedInUser.isSystemAdmin){

            try{
                userRepository.delete(item);
            }catch (SQLException e){
                var message = String.format("Make sure to delete all categories where this user(%s) is admin first. (%s)\n", item.username, e.getMessage());
                throw new Exception(message);
            }
        }
    }

    public void update(User item) throws Exception{
        if (loggedInUser != null && loggedInUser.isSystemAdmin){

            try{
                userRepository.update(item);
            }catch (SQLException e){
                throw new Exception(e.getMessage());
            }
        }
    }

    public ArrayList<User> read() throws Exception {
        try{
            return userRepository.getAll();
        }catch (SQLException e){
            throw new Exception("Failed to get users from DB" + e.getMessage());
        }
    }

}
