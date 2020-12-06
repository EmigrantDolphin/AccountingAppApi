package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.*;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.Record;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.Abstract.AccountingAppAbstraction;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryRepository extends AccountingAppAbstraction {

    public void add(Category categoryToAdd) throws SQLException {
        var connection = getConnection();
        connection.setAutoCommit(false);

        var categoryQuery = "insert into categories(name, parentCategory) values(?,?)";
        var categoryAdminsQuery = "insert into categoryAdmins(categoryId, userId) values(?,?)";

        PreparedStatement categoryPStatement = connection.prepareStatement(categoryQuery, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement categoryAdminsPStatement = connection.prepareStatement(categoryAdminsQuery, Statement.RETURN_GENERATED_KEYS);

        categoryPStatement.setString(1, categoryToAdd.name);
        if (categoryToAdd.parentCategory != null) {
            categoryPStatement.setInt(2, categoryToAdd.parentCategory.getId());
        }else{
            categoryPStatement.setNull(2, Types.INTEGER);
        }

        var affectedRows = categoryPStatement.executeUpdate();
        if (affectedRows == 0){
            connection.rollback();
            throw new SQLException("Failed to add a record");
        }

        ResultSet generatedKeys = categoryPStatement.getGeneratedKeys();
        generatedKeys.next();

        try{
            categoryToAdd.setId(generatedKeys.getInt(1));
        }catch (Exception e){
            connection.rollback();
            throw new SQLException(e);
        }

        for (var admin : categoryToAdd.admins){
            categoryAdminsPStatement.setInt(1, categoryToAdd.getId());
            categoryAdminsPStatement.setInt(2, admin.getId());
            categoryAdminsPStatement.executeUpdate();
        }

        connection.commit();
    }

    public Category getCategory(int categoryId) throws SQLException{
        var connection = getConnection();

        var categoryQuery ="select * from categories where id = ?";
        PreparedStatement categoryStatement = connection.prepareStatement(categoryQuery);

        categoryStatement.setInt(1, categoryId);

        ResultSet resultSet = categoryStatement.executeQuery();
        resultSet.next();

        var categoryName = resultSet.getString("name");
        var parentId = resultSet.getInt("parentCategory");
        var adminUsers = getAdminUsersByCategoryId(categoryId);
        var records = getRecordsByCategoryId(categoryId);

        Category category;
        if (parentId != 0){
            var parentView = getCategoryView(parentId);
            category = new Category(adminUsers.get(0), categoryName, parentView);
        }else{
            category = new Category(adminUsers.get(0), categoryName);
        }

        try{
            category.setId(categoryId);
        }catch (Exception e){
            throw new SQLException(e);
        }

        category.spending = records.stream().filter(rec -> rec.isSpending).collect(Collectors.toCollection(ArrayList::new));
        category.income = records.stream().filter(rec -> !rec.isSpending).collect(Collectors.toCollection(ArrayList::new));

        category.subCategories = getSubCategoryViewsByParentId(categoryId);

        return category;
    }

    private CategoryView getCategoryView(int categoryId) throws SQLException{
        var connection = getConnection();

        var categoryQuery ="select * from categories where id = ?";
        PreparedStatement categoryStatement = connection.prepareStatement(categoryQuery);

        categoryStatement.setInt(1, categoryId);

        ResultSet resultSet = categoryStatement.executeQuery();
        resultSet.next();

        var categoryName = resultSet.getString("name");

        CategoryView categoryView = new CategoryView(categoryId, categoryName);

        return categoryView;
    }

    private ArrayList<CategoryView> getSubCategoryViewsByParentId(int parentId) throws SQLException{
        var subCategoryViews = new ArrayList<CategoryView>();
        var connection = getConnection();

        var categoryQuery ="select * from categories where parentCategory = ?";
        PreparedStatement categoryStatement = connection.prepareStatement(categoryQuery);

        categoryStatement.setInt(1, parentId);

        ResultSet resultSet = categoryStatement.executeQuery();

        while (resultSet.next()) {
            var categoryName = resultSet.getString("name");
            var categoryId = resultSet.getInt("id");

            var categoryView = new CategoryView(categoryId, categoryName);
            subCategoryViews.add(categoryView);
        }

        return subCategoryViews;
    }

    public void update(Category updatedCategory) throws SQLException {
        var connection = getConnection();
        var query = "update `categories` set " +
                "`name` = ? where `id` = ?";

        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.setString(1, updatedCategory.name);
        pStatement.setInt(2, updatedCategory.getId());

        pStatement.executeUpdate();
    }

    public void deleteById(int id) throws SQLException {
        var connection = getConnection();
        var query = "delete from `categories` where `id` = ?";

        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.setInt(1, id);

        try{
            pStatement.executeUpdate();
        }catch (SQLException e){
            throw e;
        }
    }

    private ArrayList<UserView> getAdminUsersByCategoryId(int id) throws SQLException{
        var adminUsers = new ArrayList<UserView>();
        var connection = getConnection();

        var adminsQuery = "select * from users where id in (select userId from categoryAdmins where categoryId = ?)";
        PreparedStatement adminsStatement = connection.prepareStatement(adminsQuery);
        adminsStatement.setInt(1, id);
        ResultSet resultSet = adminsStatement.executeQuery();


        while (resultSet.next()){
            User user = new User();
            user.username = resultSet.getString("username");
            user.name = resultSet.getString("name");
            user.surname = resultSet.getString("surname");
            user.password = resultSet.getString("password");
            user.isSystemAdmin = resultSet.getBoolean("isSystemAdmin");
            try{
                user.setId(resultSet.getInt("id"));
            }catch (Exception e){
                throw new SQLException(e);
            }

            adminUsers.add(user);
        }

        return adminUsers;
    }

    private ArrayList<Record> getRecordsByCategoryId(int categoryId) throws SQLException{
        ArrayList<Record> records = new ArrayList<>();
        var connection = getConnection();

        var query ="select * from records where categoryId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, categoryId);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            Record record = new Record();
            record.name = resultSet.getString("name");
            record.amount = resultSet.getDouble("amount");
            record.creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
            record.isSpending = resultSet.getBoolean("isSpending");
            record.userCreator = getUserById(resultSet.getInt("userCreatorId"));

            try{
                //todo: maybe delete this later since the object already has a user object that has an id
                record.setCategoryId(resultSet.getInt("categoryId"));
                record.setId(resultSet.getInt("id"));
            }catch (Exception e){
                throw new SQLException(e);
            }

            records.add(record);
        }

        return records;
    }

    private User getUserById(int userId) throws SQLException{
        var connection = getConnection();

        var query ="select * from users where id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        User user = new User();
        user.username = resultSet.getString("username");
        user.name = resultSet.getString("name");
        user.surname = resultSet.getString("surname");
        user.password = resultSet.getString("password");
        user.isSystemAdmin = resultSet.getBoolean("isSystemAdmin");


        try{
            user.setId(resultSet.getInt("id"));
        }catch (Exception e){
            throw new SQLException(e);
        }

        return user;
    }

}
