package ru.gasheva.client.models;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import ru.gasheva.client.User;
import ru.gasheva.client.clientserver.Flickr;
import ru.gasheva.client.clientserver.RequestExecutor;
import ru.gasheva.client.database.UserDao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class UserModel {
    User user = null;
    UserDao userDao;
    RequestExecutor requestExecutor;
    Flickr flickr;
    OAuth1RequestToken requestToken;

    public UserModel(Flickr flickr, RequestExecutor requestExecutor) {
        this.flickr = flickr;
        this.requestExecutor = requestExecutor;
        init();
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }


    public void init(){
        userDao = new UserDao();
    }
    //работа с БД
    public boolean isLoginFree(String login){
        return userDao.isLoginFree(login);
    }
    public boolean isLocalAuthentificated(String login, String password){
        user = userDao.authentificate(login, password);
        return user!=null;
    }
    public boolean saveUserInDB(){
        //получаем nsid

        userDao.addUser(user);
        return true;
    }

    //работа с сервером
    public boolean registerOnFlickr(String code, String login, String password){
        User newUser = requestExecutor.getAccessToken(code, requestToken);
        user = newUser;
        user.setLogin(login);
        user.setPassword(password);

        //user.setNsid("190361277%40N04");
        //user.setToken("72157716110418228-6ed68f05becc6dc7", "35904e95b9ac8533");
        return true;
    }
    public String getAuthorizationUrlFromFlickr() {
        try {
            requestToken = requestExecutor.createAuthUrl();
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            //заглушкa
            return "";
        }
        return requestExecutor.getAuthorizationUrl();
    }
}
