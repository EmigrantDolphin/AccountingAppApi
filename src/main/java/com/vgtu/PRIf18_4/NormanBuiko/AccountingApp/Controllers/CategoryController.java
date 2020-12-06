package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Controllers;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.CategoryManager;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.Category;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.ErrorModel;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    private final CategoryManager categoryManager;

    public CategoryController(CategoryManager categoryManager){
        this.categoryManager = categoryManager;
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getCategories(){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("You are not logged in."));
        }

        try{
            return ResponseEntity.ok(categoryManager.getCategoryById(CategoryManager.ROOT_ID));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Object> getCategoriesById(@PathVariable int id){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("You are not logged in."));
        }

        try{
            return ResponseEntity.ok(categoryManager.getCategoryById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> postCategory(@RequestBody Category category){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (category.getId() > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not allowed to set id on post.");
        }

        try{
            categoryManager.add(category);
            category = categoryManager.getCategoryById(category.getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok(category);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Object> postCategory(@PathVariable int id, @RequestBody Category category){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (category.getId() > 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not allowed to send Category object body with id property");
        }

        if (id == CategoryManager.ROOT_ID){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not allowed to modify root category");
        }

        try{
            category.setId(id);
            categoryManager.update(category);
            category = categoryManager.getCategoryById(category.getId());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable int id){
        if (UserManager.getLoggedInUser() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel("Bad username or password."));
        }

        if (id == CategoryManager.ROOT_ID){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deleting root category not allowed");
        }

        try{
            categoryManager.removeById(id);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
        }

        return ResponseEntity.ok("success");
    }
}
