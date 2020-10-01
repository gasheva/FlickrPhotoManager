package ru.gasheva.client.controllers;

import ru.gasheva.client.Photo;
import ru.gasheva.client.views.InfoPhotoForm;

public class PhotoInfoControlOnlyRead implements IPhotoInfoControl{
    Photo photo;
    InfoPhotoForm view;

    public PhotoInfoControlOnlyRead(Photo photo) {
        this.photo = photo;
        System.out.println("Photo title2: "+photo.getTitle());
        view = new InfoPhotoForm(this, photo);
        view.createView();
    }
    @Override
    public void enableElements(){
        boolean enable = false;
        view.SpLatitudeEnable(enable);
        view.SpLongitudeEnable(enable);
        view.SpAccuracyEnable(enable);
        view.TfTitleEnable(enable);
        view.TfDescriptionEnable(enable);
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
        view.Dispose();
    }

    @Override
    public void setDefaultValues() {
        System.out.println("Title: "+photo.getTitle());
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
