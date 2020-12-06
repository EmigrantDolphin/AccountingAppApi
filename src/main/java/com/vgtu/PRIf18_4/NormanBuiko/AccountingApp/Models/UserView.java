package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models;

import java.io.Serializable;

public class UserView extends BaseModel implements Serializable {
    public String username;
    public String name;
    public String surname;
    public boolean isSystemAdmin;
}