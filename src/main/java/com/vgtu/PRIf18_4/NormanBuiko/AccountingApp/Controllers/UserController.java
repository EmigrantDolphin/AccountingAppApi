package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Controllers;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.ErrorModel;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.LoginModel;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.User;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){

        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("You are not logged in."));
        }

        try{
            return ResponseEntity.ok(UserManager.getUserManager().read());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Object> postUser(@RequestBody User user){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        try{
            UserManager.getUserManager().add(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (id <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("User id has  to be higher than zero."));
        }

        try{
            var userToDelete = new User();
            userToDelete.setId(id);
            UserManager.getUserManager().remove(userToDelete);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok(null);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> putUser(@RequestBody User user, @PathVariable int id){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (id <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("User id from path has to be higher than zero."));
        }

        if (user.getId() > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("User object should not have id set as a parameter"));
        }

        try{
            user.setId(id);
            UserManager.getUserManager().update(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginModel loginModel){
        UserManager.getUserManager().login(loginModel.username, loginModel.password);

        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        return ResponseEntity.ok("logged in successfully. Welcome " + UserManager.getLoggedInUser().name);
    }
}
