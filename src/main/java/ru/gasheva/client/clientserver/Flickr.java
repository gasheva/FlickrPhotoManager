package ru.gasheva.client.clientserver;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.xml.sax.SAXException;
import ru.gasheva.client.Photo;
import ru.gasheva.client.Tag;
import ru.gasheva.client.User;
import ru.gasheva.client.XMLParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Flickr {
    private final String urlPrefix = "http://api.tumblr.com/v2/blog/" + System.getenv("MOVS_TRRP_TUMBLR_BLOG_URL");
    private final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";
    final String apiKey = "3c1c29bb6f2c6a113d6e4d8179b429e2";
    final String apiSecret = "93dc95190bb4ad08";
    private RequestExecutor requestExecutor;

    public Flickr(RequestExecutor requestExecutor) {
            this.requestExecutor = requestExecutor;
    }

    public List<Photo> getPhotos(User user) throws InterruptedException, ExecutionException, IOException {
        //получаем все фотки
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.people.getPhotos");
        request.addQuerystringParameter("api_key", apiKey);
        request.addQuerystringParameter("user_id", user.getNsid());
        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            return xmlParser.getPhotos(response.getBody());
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Photo getPhotoInfo(User user, String id) throws InterruptedException, ExecutionException, IOException {
        //получаем все фотки
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.photos.getInfo");
        request.addQuerystringParameter("api_key", apiKey);
        request.addQuerystringParameter("photo_id", id);
        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            //if (xmlParser.getStatus(response.getBody()) !="ok") return null;
            return xmlParser.getPhotoInfo(response.getBody());
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //ок, здесь апдейтим только координаты
    public boolean updatePhoto(User user, Photo photo) throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.photos.geo.setLocation");
        request.addQuerystringParameter("api_key", apiKey);
        request.addQuerystringParameter("photo_id", photo.getId());
        request.addQuerystringParameter("lat", String.valueOf(photo.getLatit()));
        request.addQuerystringParameter("lon", String.valueOf(photo.getLongit()));
        request.addQuerystringParameter("accuracy", String.valueOf(photo.getAccurancy()));

        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            String stat = xmlParser.getStatus(response.getBody());
            System.out.println("."+stat+"."+stat.equals("ok"));
            return (stat.equals("ok"));
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return false;

    }


    public Set<Tag> addTag(User user, Photo photo, String tag) throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.photos.addTags");
        request.addQuerystringParameter("api_key", apiKey);
        request.addQuerystringParameter("photo_id", photo.getId());
        request.addQuerystringParameter("tags", tag);

        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            if (xmlParser.getStatus(response.getBody()).equals("ok")) {
                Photo photoInfo = getPhotoInfo(user, photo.getId());
                if (photoInfo != null) {
                    return photoInfo.getTags();
                }

            } else return null;
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeTag(User user, Photo photo, Tag tag) throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.photos.removeTag");
        request.addQuerystringParameter("api_key", apiKey);
        request.addQuerystringParameter("tag_id", tag.getId());

        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            String stat = xmlParser.getStatus(response.getBody());
            System.out.println("."+stat+"."+stat.equals("ok"));
            return (stat.equals("ok"));
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return false;

    }

    public String uploadPhoto(User user, File img) throws InterruptedException, ExecutionException, IOException {
        //получить расширение файла
        int index = img.getName().lastIndexOf('.');
        String extension = img.getName().substring(index+1);

        byte[] payload = Files.readAllBytes(img.toPath());
        System.out.println(payload);
        String base64 = Base64.encode(payload);
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://up.flickr.com/services/upload/");
        String multipartBoundary = getMultipartBoundary();
        request.initMultipartPayload(multipartBoundary);
        request.addHeader("Content-Type", "multipart/form-data; boundary=" + multipartBoundary);
        request.addFileByteArrayBodyPartPayloadInMultipartPayload("image/"+extension, payload, "photo",img.getName());

        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            if (!xmlParser.getStatus(response.getBody()).equals("ok")) {
                return null;
            }
            String id= xmlParser.getPhotoId(response.getBody());
            System.out.println(id);
            return id;
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String getMultipartBoundary() {
        return "---------------------------" + UUID.randomUUID();
    }

    public boolean deletePhoto(User user, String selectedPhotoId) throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
        request.addQuerystringParameter("method", "flickr.photos.delete");
        request.addQuerystringParameter("api_key", apiKey);
        request.addQuerystringParameter("photo_id", selectedPhotoId);
        Response response = requestExecutor.execute(new OAuth1AccessToken(user.getToken(), user.getToken_secret()), request);
        System.out.println(response.getBody());
        XMLParser xmlParser = new XMLParser();
        try {
            if (xmlParser.getStatus(response.getBody()).equals("ok")) return true;
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
