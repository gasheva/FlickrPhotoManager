package ru.gasheva.client;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.api.client.auth.openidconnect.IdToken;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import ru.gasheva.client.models.PhotoModel;
import ru.gasheva.client.views.InfoPhotoForm;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class FlickrApp {

    private static final String PROTECTED_RESOURCE_URL = "https://api.flickr.com/services/rest/";

    private FlickrApp() {
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your own api key and secret
        final String apiKey = "3c1c29bb6f2c6a113d6e4d8179b429e2";
        final String apiSecret = "93dc95190bb4ad08";

//        final OAuth10aService service = new ServiceBuilder(apiKey)
//                .apiSecret(apiSecret)
//                .build(FlickrApi.instance(FlickrApi.FlickrPerm.DELETE));
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Flickr's OAuth Workflow ===");
        System.out.println();


//        // Obtain the Request Token
//        System.out.println("Fetching the Request Token...");
//        final OAuth1RequestToken requestToken = service.getRequestToken();
//        System.out.println("Got the Request Token!");
//        System.out.println();
//
//        System.out.println("Now go and authorize ScribeJava here:");
//        final String authorizationUrl = service.getAuthorizationUrl(requestToken);
//        System.out.println(authorizationUrl);
//        System.out.println("And paste the verifier here");
//        System.out.print(">>");
//        final String oauthVerifier = in.nextLine();
//        System.out.println();
//
//        // Trade the Request Token and Verifier for the Access Token
//        System.out.println("Trading the Request Token for an Access Token...");
//        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
//        System.out.println("Got the Access Token!");
//        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
//        System.out.println();

        OAuth1AccessToken accessToken = new OAuth1AccessToken("72157716110418228-6ed68f05becc6dc7", "35904e95b9ac8533");
        // get user info
          System.out.println("Now we're going to access a protected resource...");
//        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
//        request.addQuerystringParameter("method", "flickr.test.login");
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//        }

        //add tag to photo
//        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
//        request.addQuerystringParameter("method", "flickr.photos.addTags");
//        request.addQuerystringParameter("api_key", apiKey);
//        request.addQuerystringParameter("photo_id", "50382156756");
//        request.addQuerystringParameter("tags", "Лето");
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//        }

        // get info from photo
//        final OAuthRequest request2 = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
//        request2.addQuerystringParameter("method", "flickr.photos.getInfo");
//        request2.addQuerystringParameter("api_key", apiKey);
//        request2.addQuerystringParameter("photo_id", "50382156756");
//        service.signRequest(accessToken, request2);
//        try (Response response = service.execute(request2)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//        }

//        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
//        request.addQuerystringParameter("method", "flickr.photos.removeTag");
//        request.addQuerystringParameter("api_key", apiKey);
//        request.addQuerystringParameter("tag_id", "190329138-50382156756-280588056");
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//        }


        //локация
//        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
//        request.addQuerystringParameter("method", "flickr.photos.geo.setLocation");
//        request.addQuerystringParameter("api_key", apiKey);
//        request.addQuerystringParameter("photo_id", "50382156756");
//        request.addQuerystringParameter("lat", "55.7496");
//        request.addQuerystringParameter("lon", "37.610");
//        request.addQuerystringParameter("accuracy", "16");
//
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//        }

        //получаем все фотки
//        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
//        request.addQuerystringParameter("method", "flickr.people.getPhotos");
//        request.addQuerystringParameter("api_key", apiKey);
//        request.addQuerystringParameter("user_id", "190361277@N04");
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//
//        }

        //upload photo
//        File img = new File("C:\\Users\\Таня\\Pictures\\Td5qD1WZlic.jpg");
//        //получить расширение файла
//        int index = img.getName().lastIndexOf('.');
//        String extension = img.getName().substring(index+1);
//
//        byte[] payload = Files.readAllBytes(img.toPath());
//        System.out.println(payload);
//        String base64 = Base64.encode(payload);
//        OAuthRequest request = new OAuthRequest(Verb.POST, "https://up.flickr.com/services/upload/");
//        String multipartBoundary = getMultipartBoundary();
//        request.initMultipartPayload(multipartBoundary);
//        request.addHeader("Content-Type", "multipart/form-data; boundary=" + multipartBoundary);
//        request.addFileByteArrayBodyPartPayloadInMultipartPayload("image/"+extension, payload, "photo",img.getName());
//
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//
//        }


        //delete photo
//        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
//        request.addQuerystringParameter("method", "flickr.photos.delete");
//        request.addQuerystringParameter("api_key", apiKey);
//        request.addQuerystringParameter("photo_id", "50384217993");
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//
//        }



        //region xml
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<rsp stat=\"ok\">\n" +
                "<photos page=\"1\" pages=\"1\" perpage=\"100\" total=\"1\">\n" +
                "\t<photo id=\"50382156756\" owner=\"190361277@N04\" secret=\"0e5b938b8b\" server=\"65535\" farm=\"66\" title=\"avatar\" ispublic=\"1\" isfriend=\"0\" isfamily=\"0\" />\n" +
                "</photos>\n" +
                "</rsp>";
        //endregion
//        XMLParser xmlParser = new XMLParser();
//        try {
//            xmlParser.findPhotoIds(xml);
//        } catch (SAXException | ParserConfigurationException | XPathExpressionException e) {
//            e.printStackTrace();
//        }

//        InfoPhotoForm dialog = new InfoPhotoForm(new PhotoModel());
//        dialog.createView();

        System.out.println();
    }


    private static String getMultipartBoundary() {
        return "---------------------------" + UUID.randomUUID();
    }
}