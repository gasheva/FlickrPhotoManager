package ru.gasheva.client.database;

import ru.gasheva.client.User;
import ru.gasheva.client.UserCifer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    DataSource ds = new DataSource("src/main/resources/JDBCConfigSQLite.properties");

    public User authentificate(String login, String password){
        User user = null;
        try(Connection conn = ds.getConnection()){
            PreparedStatement statement = conn.prepareStatement("\n" +
                    "SELECT token, token_secret, nsid\n" +
                    "FROM users_table\n" +
                    "WHERE login=? AND password=?;");
            statement.setString(1, login);
            statement.setBytes(2, UserCifer.encryptAES(login, password));

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                 user = new User(login);
                 user.setPassword(password);
                 user.setToken(new String(UserCifer.decryptAES(login, rs.getBytes("token"))),
                         new String(UserCifer.decryptAES(login, rs.getBytes("token_secret"))));
                 user.setNsid(new String(UserCifer.decryptAES(login, rs.getBytes("nsid"))));
            }

        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }
    public boolean isLoginFree(String login){
        try(Connection conn= ds.getConnection()){
            PreparedStatement statement = conn.prepareStatement("SELECT count(*) as count from users_table where login=?;");
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if(rs.getInt("count")==0) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUser(User user){
        try(Connection conn = ds.getConnection()){
            PreparedStatement statement = conn.prepareStatement("\n" +
                    "Insert INTO users_table(login, password, token, token_secret, nsid)\n" +
                    "values (?, ?, ?, ?, ?);");
            statement.setString(1, user.getLogin());
            statement.setBytes(2, UserCifer.encryptAES(user.getLogin(), user.getPassword()));
            statement.setBytes(3, UserCifer.encryptAES(user.getLogin(), user.getToken()));
            statement.setBytes(4, UserCifer.encryptAES(user.getLogin(), user.getToken_secret()));
            statement.setBytes(5, UserCifer.encryptAES(user.getLogin(), user.getNsid()));

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
