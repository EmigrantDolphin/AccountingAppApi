package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Category extends BaseModel implements Serializable {
    public String name;
    public ArrayList<UserView> admins = new ArrayList<>();
    public ArrayList<Record> spending = new ArrayList<>();
    public ArrayList<Record> income = new ArrayList<>();
    public CategoryView parentCategory;
    public ArrayList<CategoryView> subCategories = new ArrayList<>();

    public Category(){}

    public Category(UserView admin, String name){
        admins.add(admin);
        this.name = name;
    }

    public Category(UserView admin, String name, CategoryView parentCategory){
        this(admin, name);
        this.parentCategory = parentCategory;
    }


    public Double getTotalSpending(){
        return spending.stream().mapToDouble(a -> a.amount).sum();
    }

    public Double getTotalIncome(){
        return income.stream().mapToDouble(a -> a.amount).sum();
    }
}
