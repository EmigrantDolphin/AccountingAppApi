package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Controllers;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.CategoryManager;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.Record;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.ErrorModel;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class RecordController {

    private final CategoryManager categoryManager;

    public RecordController(CategoryManager categoryManager){
        this.categoryManager = categoryManager;
    }

    @PostMapping("/records")
    public ResponseEntity<Object> postRecord(@RequestBody Record record){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (record.getId() > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("You are not allowed to set id on post."));
        }

        try{
            record.creationDate = LocalDateTime.now();
            record.userCreator = UserManager.getLoggedInUser();

            categoryManager.addRecord(record);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable int recordId){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        try{
            categoryManager.removeRecordById(recordId);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok("success");
    }


    @PutMapping("/records/{id}")
    public ResponseEntity<Object> postCategory(@PathVariable int id, @RequestBody Record record){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (record.getId() > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not allowed to send record object body with id property. Specify id in path");
        }


        if (record.creationDate != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not allowed to update creation date");
        }

        if (record.getCategoryId() > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not allowed to change category");
        }

        try{
            record.setId(id);
            categoryManager.updateRecord(record);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok("success");
    }
}
