package ru.gasheva.client.clientserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

class CallbackServer {
    private final HttpServer server;
    private final CompletableFuture<String> oAuth2VerifierCF = new CompletableFuture<>();

    CallbackServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/", new OAuth2CallbackHandler(this));
        server.setExecutor(null);
    }

    String getAuthUrl() {
        return String.format("http://localhost:%d/", server.getAddress().getPort());
    }

    void start() {
        server.start();
    }

    CompletableFuture<String> getOAuth1VerifierCF() {
        return oAuth2VerifierCF;
    }

    void setOAuth1Verifier(String value) {
        oAuth2VerifierCF.complete(value);
        server.stop(0);
    }

}
