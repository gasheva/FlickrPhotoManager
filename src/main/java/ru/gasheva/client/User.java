package ru.gasheva.client;

import com.github.scribejava.core.model.OAuth1AccessToken;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String login;
    private OAuth1AccessToken token;
    private String nsid;
    private String password;

    public User(){}
    public User(String login) {
        this.login = login;
    }
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }


    //region Properites

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token.getToken();
    }

    public void setToken(String token, String token_secret) {
        this.token = new OAuth1AccessToken(token, token_secret);
    }
    public void setToken(OAuth1AccessToken token) {
        this.token = token;
    }

    public String getToken_secret() {
        return token.getTokenSecret();
    }

    public String getNsid() {
        return nsid;
    }

    public void setNsid(String nsid) {
        this.nsid = NsidParser(nsid);
    }

    //endregion
    private String NsidParser(String nsid){
        Pattern pattern = Pattern.compile("%40");
        Matcher matcher = pattern.matcher(nsid);
        nsid = matcher.replaceAll("@");
        System.out.println(nsid);
        return nsid;
    }

}
