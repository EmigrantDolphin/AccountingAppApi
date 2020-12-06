package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models;

public abstract class BaseModel {
    protected int id;

    public void setId(int id) throws Exception{
        if (this.id == 0){
            this.id = id;
            return;
        }

        throw new Exception("Can't overwrite ID that is already assigned");
    }

    public int getId() {return id;}
}
