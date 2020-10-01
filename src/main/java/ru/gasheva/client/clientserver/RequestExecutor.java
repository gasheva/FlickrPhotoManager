package ru.gasheva.client.clientserver;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth10aService;
import ru.gasheva.client.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestExecutor {
    final OAuth10aService service;

    final String apiKey = "3c1c29bb6f2c6a113d6e4d8179b429e2";
    final String apiSecret = "93dc95190bb4ad08";
    String authorizationUrl;

    public RequestExecutor() {
        //CallbackServer cbs = new CallbackServer();
        //cbs.start();
        service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .build(FlickrApi.instance(FlickrApi.FlickrPerm.DELETE));

        System.out.println("=== Flickr's OAuth Workflow ===");
        System.out.println();
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public OAuth1RequestToken createAuthUrl() throws InterruptedException, ExecutionException, IOException {
        OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println("Now go and authorize ScribeJava here:");
        this.authorizationUrl = service.getAuthorizationUrl(requestToken);
        System.out.println(authorizationUrl);

        return requestToken;
    }
    public User getAccessToken(String oauthVerifier, OAuth1RequestToken requestToken) {
        System.out.println("Getting token in executor...");
        User user = new User();
        System.out.println();
        // Trade the Request Token and Verifier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        OAuth1AccessToken accessToken = null;
        try {
            accessToken = service.getAccessToken(requestToken, oauthVerifier);
            System.out.println("Got the Access Token!");
            System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
            System.out.println();
            user.setToken(accessToken);
            Pattern pattern = Pattern.compile("user_nsid=.+?&username");
            Matcher matcher = pattern.matcher(accessToken.getRawResponse());
            matcher.find();
            String nsid = matcher.group();
            nsid = nsid.replaceAll("user_nsid=", "");
            nsid = nsid.replaceAll("&username", "");
            System.out.println(nsid);
            user.setNsid(nsid);
            return user;
        } catch (Exception e) {
            System.out.println("Code is wrong");
            e.printStackTrace();
            return null;
        }
    }


    Response execute(OAuth1AccessToken accessToken, OAuthRequest request) throws ExecutionException, InterruptedException, IOException {
        service.signRequest(accessToken, request);
        return service.execute(request);
    }
}
