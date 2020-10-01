package ru.gasheva.client.controllers;

import ru.gasheva.client.User;
import ru.gasheva.client.models.UserModel;
import ru.gasheva.client.views.LogInForm;

public class LogInControl {
    LogInForm view;
    UserModel model;
    boolean isCanceled = false;

    public LogInControl(UserModel model) {
        this.model = model;
    }
    public void createView(){
        view = new LogInForm(this, model);
        view.createView();
    }

    private boolean isStringValid(String checkingString){
        if (checkingString==null || checkingString.length()==0) return false;
        return true;
    }
    public void Authorise(){
        String login = view.getLogin();
        String password = view.getPassword();
        if (!isStringValid(login) || !isStringValid(password)) {
            view.showMessage("Don't leave the fields empty");
            return;
        }
        //если в БД нет логина
        if (model.isLoginFree(view.getLogin())){
            view.showMessage("Your login is wrong");
            return;
        }
        if(!model.isLocalAuthentificated(view.getLogin(), view.getPassword())){
            view.showMessage("Your password is wrong");
            return;
        }
        view.showMessage("Welcome, "+login);
        view.Dispose();
    }
    public void cancel(){
        isCanceled = true;
        view.Dispose();
    }

    public boolean isCanceled(){return isCanceled;}
    public void createNewUser() {
        String login = view.getLogin();
        String password = view.getPassword();
        if (!isStringValid(login) || !isStringValid(password)) {
            view.showMessage("Don't leave the fields empty");
            return;
        }
        if (!model.isLoginFree(view.getLogin())){
            view.showMessage("This login is already in use");
            return;
        }


        String url = model.getAuthorizationUrlFromFlickr();
        if (url=="") {
            view.showMessage("Oops, something went wrong. Check your Internet connection and try it later");
        }
        String code = view.askCode(url);
        System.out.println("Trying to register...");
        boolean isResisterSuccessful = model.registerOnFlickr(code, login, password);
        System.out.println("Register "+isResisterSuccessful);
        if (isResisterSuccessful){
            model.saveUserInDB();
            view.showMessage("Welcome to the world of tomorrow, "+ login+"!");
        }
        else{
            view.showMessage("Oops, something went wrong. Check your Internet connection and try it later");
            return;
        }
        view.Dispose();
    }
}
