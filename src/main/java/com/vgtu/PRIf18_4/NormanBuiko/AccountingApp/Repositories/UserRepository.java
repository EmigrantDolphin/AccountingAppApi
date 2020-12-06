package com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories;

import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Models.User;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.Abstract.AccountingAppAbstraction;
import com.vgtu.PRIf18_4.NormanBuiko.AccountingApp.Repositories.Interfaces.IRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserRepository extends AccountingAppAbstraction implements IRepository<User> {

    @Override
    public void add(User userToAdd) throws SQLException {
        var connection = getConnection();
        connection.setAutoCommit(false);

        var query = "insert into users(username, name, surname, password, isSystemAdmin) values(?,?,?,?,?)";

        PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pStatement.setString(1, userToAdd.username);
        pStatement.setString(2, userToAdd.name);
        pStatement.setString(3, userToAdd.surname);
        pStatement.setString(4, userToAdd.password);
        pStatement.setBoolean(5, userToAdd.isSystemAdmin);

        var affectedRows = pStatement.executeUpdate();
        if (affectedRows == 0){
            connection.rollback();
            throw new SQLException("Failed to add a user");
        }

        ResultSet generatedKeys = pStatement.getGeneratedKeys();
        generatedKeys.next();

        try{
            userToAdd.setId(generatedKeys.getInt(1));
            connection.commit();
        }catch (Exception e){
            connection.rollback();
        }
    }

    @Override
    public ArrayList<User> getAll() throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        var connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * from users");

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

            users.add(user);
        }

        return users;
    }

    @Override
    public void update(User updatedUser) throws SQLException {
        var connection = getConnection();
        var query = "update `users` set " +
                "`username` = ?," +
                "`name` = ?," +
                "`surname` = ?," +
                "`password` = ?," +
                "`isSystemAdmin` = ? where `id` = ?";
        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.setString(1, updatedUser.username);
        pStatement.setString(2, updatedUser.name);
        pStatement.setString(3, updatedUser.surname);
        pStatement.setString(4, updatedUser.password);
        pStatement.setBoolean(5, updatedUser.isSystemAdmin);
        pStatement.setInt(6, updatedUser.getId());

        pStatement.executeUpdate();
    }

    @Override
    public void delete(User userToDelete) throws SQLException {
        var connection = getConnection();
        var query = "delete from `users` where `id` = ?";

        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.setInt(1, userToDelete.getId());

        pStatement.executeUpdate();
    }
}
