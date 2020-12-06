package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.Interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IRepository<T> {
    void add(T object) throws SQLException;
    ArrayList<T> getAll() throws SQLException;
    void update(T updatedObject) throws SQLException;
    void delete(T objectToDelete) throws SQLException;
}
