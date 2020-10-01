package ru.gasheva.client.controllers;

import ru.gasheva.client.Photo;
import ru.gasheva.client.Tag;
import ru.gasheva.client.User;
import ru.gasheva.client.clientserver.Flickr;
import ru.gasheva.client.models.PhotoModel;
import ru.gasheva.client.views.InfoPhotoForm;

import java.util.HashSet;
import java.util.Set;

public class PhotoInfoControlEdit implements IPhotoInfoControl{
    Photo photo;
    InfoPhotoForm view;
    Flickr flickr;
    PhotoModel model;
    User user;
    Photo oldPhoto;

    public PhotoInfoControlEdit(Photo photo, PhotoModel model, User user, Flickr flickr) {
        this.photo = photo;
        try {
            oldPhoto = (Photo) photo.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.user = user;
        this.model = model;
        this.flickr = flickr;
        view = new InfoPhotoForm(this, photo);
        view.createView();
    }
    @Override
    public void enableElements(){
        boolean enable = true;
        view.SpLatitudeEnable(enable);
        view.SpLongitudeEnable(enable);
        view.SpAccuracyEnable(enable);
        view.TfTitleEnable(false);
        view.TfDescriptionEnable(false);
        view.TfUrlEnable(false);
        view.BtnRemoveTagEnable(enable);
        view.BtnAddTagEnable(enable);
    }


    @Override
    public void cancel() {
        view.Dispose();
    }

    @Override
    public void ok() {
        if(view.areFieldsEmpty()){
            view.showMessage("Don't leave the fields empty");
            return;
        }
        if(!view.checkLocationBoundary()){
            view.showMessage("Check location boundary");
            return;
        };
        view.getPhotoUpdate();  //обновляем данные модели (фотки)
        System.out.println("Acc - "+photo.getAccurancy());

        if(!model.updatePhoto(flickr, user, photo)) {
            view.showMessage("We can't update location. Try later");
            return;
        }
        view.Dispose();
    }
    public void removeTag(Tag tag){
        if(model.removeTag(flickr, user, tag, photo)){
            view.removeTagFromTable(tag);
        };
    }
    public void addTag(String tag){
        if (tag.length()==0) {
            return; //не ввели тэги
        }

        Set<Tag> allTags = model.addTag(flickr, user, tag, photo);
        if(allTags!=null){
            view.addTagsToTable(allTags);
        }
        else view.showMessage("We can't add tags. Try later");
    }

    @Override
    public void setDefaultValues() {
        view.setTfTitle(photo.getTitle());
        view.setTfDescription(photo.getDescription());
        view.setTfUrl(photo.getUrl());

        view.createTable();
        if (photo.getAccurancy()>-1000) {
            view.setSpAccuracy(photo.getAccurancy());
            view.setSpLatitude(photo.getLatit());
            view.setSpLongitude(photo.getLongit());
        }
    }

}
