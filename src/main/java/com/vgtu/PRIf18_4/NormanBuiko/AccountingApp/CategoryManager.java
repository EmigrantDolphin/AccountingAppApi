package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.Category;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.Record;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.CategoryRepository;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.RecordRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CategoryManager {
    public static final int ROOT_ID = 1;

    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final RecordRepository recordRepository = new RecordRepository();

    public void add(Category item) throws Exception{
        try{
            categoryRepository.add(item);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }

    public void removeById(int id) throws Exception{
        try{
            categoryRepository.deleteById(id);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }

    public void update(Category item) throws Exception{
        try{
            categoryRepository.update(item);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }

    public Category getCategoryById(int id) throws Exception{
        try{
            return categoryRepository.getCategory(id);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }

    public void addRecord(Record record) throws Exception{
        try{
            recordRepository.add(record);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }

    public void removeRecordById(int recordId) throws Exception{
        try{
            recordRepository.removeById(recordId);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }

    public void updateRecord(Record record) throws Exception{
        try{
            recordRepository.update(record);
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }
}
