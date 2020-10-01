package ru.gasheva.client.models;

import ru.gasheva.client.Photo;
import ru.gasheva.client.PhotoObserver;
import ru.gasheva.client.Tag;
import ru.gasheva.client.User;
import ru.gasheva.client.clientserver.Flickr;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PhotoModel{
    List<Photo> photos;
    List<PhotoObserver> photoObservers;

    public PhotoModel() {
        photos = new ArrayList<>();
        photoObservers = new ArrayList<>();
    }

    public void init(){

    }
    public Photo getPhotoById(String id){
        Optional<Photo> photo = photos.stream().filter(x->x.getId()==id).findAny();
        return photo.get();
    }
    public void clear(){
        photos = new ArrayList<>();
    }
    public int size(){return photos.size();}

    public void registerPhotoObserver(PhotoObserver o){
        photoObservers.add(o);
    }
    public void removePhotoObserver(PhotoObserver o){
        if (photoObservers.indexOf(o)!=-1) photoObservers.remove(o);
    }

    public boolean getPhotos(Flickr flickr, User user) {
        try {
            List<Photo> newPhotos = flickr.getPhotos(user);
            System.out.println(photos==null);
            photos = newPhotos;
            System.out.println(photos==null);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return false;
        }
        //photoObservers.forEach(x->x.update());
        return true;
    }
    public Photo get(int index){
        return photos.get(index);
    }

    public Photo getInfo(Flickr flickr, User user, String id) {
        try {
            return flickr.getPhotoInfo(user, id);

        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return null;
        }
        //photoObservers.forEach(x->x.update());
    }

    //апдейт только локации
    public boolean updatePhoto(Flickr flickr, User user, Photo photo) {
        try {
            return flickr.updatePhoto(user, photo);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Set<Tag> addTag(Flickr flickr, User user, String tag, Photo photo) {
        try {
            return flickr.addTag(user, photo, tag);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeTag(Flickr flickr, User user, Tag tag, Photo photo) {
        try {
            return flickr.removeTag(user, photo, tag);
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String uploadPhoto(Flickr flickr, User user, File photo) {
        try {
            String isSucc = flickr.uploadPhoto(user, photo);
            if (isSucc!=null){
                photos.add(new Photo(isSucc, photo.getName()));
            }
            return isSucc;
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deletePhoto(Flickr flickr, User user, String selectedPhotoId) {
        try {
            boolean isSucc =  flickr.deletePhoto(user, selectedPhotoId);
            if(isSucc){
                Photo p = photos.stream().filter(x->x.getId()==selectedPhotoId).toArray(Photo[]::new)[0];
                photos.remove(p);
            }

            return isSucc;
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
