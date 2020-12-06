package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models;

public class CategoryView extends BaseModel{

    public CategoryView(){};

    public CategoryView(int id, String name){
        this.id = id;
        this.name = name;
    }
    public String name;
}
