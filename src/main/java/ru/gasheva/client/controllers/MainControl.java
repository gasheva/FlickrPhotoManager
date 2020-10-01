package ru.gasheva.client.controllers;

import ru.gasheva.client.Photo;
import ru.gasheva.client.clientserver.Flickr;
import ru.gasheva.client.clientserver.RequestExecutor;
import ru.gasheva.client.views.MainForm;
import ru.gasheva.client.models.PhotoModel;
import ru.gasheva.client.models.UserModel;

import java.io.File;

public class MainControl {
    PhotoModel photoModel;
    MainForm view;
    LogInControl logInControl;
    UserModel userModel;
    IPhotoInfoControl infoControl;

    Flickr flickr;
    final RequestExecutor requestExecutor;


    public MainControl(PhotoModel photoModel) {
        this.photoModel = photoModel;
        requestExecutor = new RequestExecutor();
        flickr = new Flickr(requestExecutor);
        userModel = new UserModel(flickr, requestExecutor);

        view = new MainForm(this, photoModel);
        view.createView();
        view.createControllers();
        photoModel.init();
        enablePhotoButtons(false);
    }
    public void addPhoto(){
        File photo = view.choosePhoto();
        if (photo==null) return;
        String id = photoModel.uploadPhoto(flickr, userModel.getUser(), photo);
        if (id!=null)
        {
            view.addPhotoInTable(id, photo.getName());
        }else{
            view.showMessage("Photo did not upload");
            return;
        }
    }
    public void editPhoto(){
        if (view.indexRowSelect()==-1) {
            view.showMessage("Choose the photo to edit");
            return;
        }
        Photo photo = photoModel.getInfo(flickr, userModel.getUser(), view.getSelectedPhotoId());
        infoControl = new PhotoInfoControlEdit(photo, photoModel, userModel.getUser(), flickr);
    }
    public void deletePhoto(){
        if (view.indexRowSelect()==-1) {
            view.showMessage("Choose the photo to delete");
            return;
        }
        if(photoModel.deletePhoto(flickr, userModel.getUser(), view.getSelectedPhotoId())){
            view.deletePhotoInTable();
        }
        else {
            view.showMessage("We can't delete this photo. Try later");
        }
    }
    public void getPhotos(){
        if(photoModel.getPhotos(flickr, userModel.getUser()))
            view.update();
        else view.showMessage("We don't get new photos. Try later");
    }
    public void getInfo(){
        if (view.indexRowSelect()==-1) {
            view.showMessage("Choose the photo");
            return;
        }
        Photo photo = photoModel.getInfo(flickr, userModel.getUser(), view.getSelectedPhotoId());
        if (photo==null){
            view.showMessage("Check your connection");
            return;
        }
        System.out.println("Photo title: "+photo.getTitle());
        infoControl = new PhotoInfoControlOnlyRead(photo);

    }

    public void logIn(){
        logInControl = new LogInControl(userModel);
        logInControl.createView();
        boolean isCancelled = logInControl.isCanceled();
        //если отменили вход, то ничего не меняется, иначе чистим форму и разблокируем кнопки
        if (!isCancelled){
            enablePhotoButtons(true);
            //TODO - чистим форму
            view.setLblUser(userModel.getUser().getLogin());
            view.createEmptyTable();
            photoModel.clear();
        }

    }

    private void enablePhotoButtons(boolean isEnable){
        view.btnAddEnable(isEnable);
        view.btnDeleteEnable(isEnable);
        view.btnEditEnable(isEnable);
        view.btnGetEnable(isEnable);
        view.btnGetInfoEnable(isEnable);
    }
}
