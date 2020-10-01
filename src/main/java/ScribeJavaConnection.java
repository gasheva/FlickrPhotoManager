import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ScribeJavaConnection {
    private static final String NETWORK_NAME = "Google";
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/calendar/v3/calendars/primary";

    private ScribeJavaConnection() {
    }

    private void connect(){
        OAuth20Service service = new ServiceBuilder("api__key")
                .apiSecret("api__secret")
                .defaultScope("https://www.googleapis.com/auth/userinfo.email")
                .callback("http://localhost:8080/auth")
                .build(GoogleApi20.instance());

        String authUrl = service.getAuthorizationUrl();

    }

    //ya29.a0AfH6SMB2sGUWAEDO0a3ELTuTCgGE-zfe_tSXLwVDcg4U-tA0J83NZpwi7spSVghfO7ECkoMmofZXUlQ__8kHnaeASQ3o-REQyHMdbD43Wp_xWIfxFcZTS8tc8v3cRNNVbpvzNu-t4sAmgMG2dxocD4kNbycnUaMFKbg
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        // Replace these with your client id and secret
        final String clientId = "1088749637627-u9omrurl0p1esb8j8g3r2iui7np9jgpr.apps.googleusercontent.com";
        final String clientSecret = "J7mb39ZSQnHB1jVfCZtcP45Q";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder(clientId)
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .defaultScope("https://www.googleapis.com/auth/calendar")
                .callback("urn:ietf:wg:oauth:2.0:oob")
                .build(GoogleApi20.instance());
        final Scanner in = new Scanner(System.in, "UTF-8");

        // Obtain the Authorization URL
        // System.out.println("Fetching the Authorization URL...");
        //pass access_type=offline to get refresh token
        //https://developers.google.com/identity/protocols/OAuth2WebServer#preparing-to-start-the-oauth-20-flow
//        final Map<String, String> additionalParams = new HashMap<>();
//        additionalParams.put("access_type", "offline");
//        //force to reget refresh token (if user are asked not the first time)
//        additionalParams.put("prompt", "consent");
//        final String authorizationUrl = service.createAuthorizationUrlBuilder()
//                .state(secretState)
//                .additionalParams(additionalParams)
//                .build();
//
//        System.out.println("Got the Authorization URL!");
//        System.out.println("Now go and authorize ScribeJava here:");
//        System.out.println(authorizationUrl);
//        System.out.println("And paste the authorization code here");
//        System.out.print(">>");
//        final String code = in.nextLine();
//        System.out.println();


        System.out.println("Trading the Authorization Code for an Access Token...");
        //OAuth2AccessToken accessToken = service.getAccessToken(code);
        OAuth2AccessToken accessToken = service.getAccessToken("ya29.a0AfH6SMB2sGUWAEDO0a3ELTuTCgGE-zfe_tSXLwVDcg4U-tA0J83NZpwi7spSVghfO7ECkoMmofZXUlQ__8kHnaeASQ3o-REQyHMdbD43Wp_xWIfxFcZTS8tc8v3cRNNVbpvzNu-t4sAmgMG2dxocD4kNbycnUaMFKbg");
        //accessToken = service.refreshAccessToken("1//0cz7wLRiye7uHCgYIARAAGAwSNwF-L9Irs_yUwGKPAc6uPxPB34S0QTW8bykrPqNIRnCFX_s0d8gjRq3vJaoWY2bsVcb8_E1s5Go");



        System.out.println("Got the Access Token!");
        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");

        System.out.println("Refreshing the Access Token...");
        String refreshToken = accessToken.getRefreshToken();
        System.out.println("Refresh token: "+refreshToken);
        System.out.println();
//        accessToken = service.refreshAccessToken(accessToken.getRefreshToken());
//        System.out.println("Refreshed the Access Token!");
//        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
//        System.out.println();
//
        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        while (true) {
            System.out.println("Paste fieldnames to fetch (leave empty to get profile, 'exit' to stop example)");
            System.out.print(">>");
            final String query = in.nextLine();
            System.out.println();

            final String requestUrl;
            if ("exit".equals(query)) {
                break;
            } else if (query == null || query.isEmpty()) {
                requestUrl = PROTECTED_RESOURCE_URL;
            } else {
                requestUrl = PROTECTED_RESOURCE_URL + query;
            }

            final OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
            service.signRequest(accessToken, request);
            System.out.println();
            try (Response response = service.execute(request)) {
                System.out.println(response.getCode());
                System.out.println(response.getBody());
            }
            System.out.println();
        }
    }

}
