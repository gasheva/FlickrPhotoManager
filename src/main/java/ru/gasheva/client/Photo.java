package ru.gasheva.client;

import java.util.HashSet;
import java.util.Set;

public class Photo implements Cloneable{
    String id;
    String title;
    Set<Tag> tags = new HashSet<>();

    String description;
    String url;

    int latit;
    int longit;
    int accurancy;

    public Photo(){}
    public Photo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getLatit() {
        return latit;
    }

    public void setLatit(int latit) {
        this.latit = latit;
    }

    public int getLongit() {
        return longit;
    }

    public void setLongit(int longit) {
        this.longit = longit;
    }

    public int getAccurancy() {
        return accurancy;
    }

    public void setAccurancy(int accurancy) {
        this.accurancy = accurancy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    public void addTag(Tag tag){tags.add(tag);}
    public void removeTag(Tag tag){tags.remove(tag);}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
