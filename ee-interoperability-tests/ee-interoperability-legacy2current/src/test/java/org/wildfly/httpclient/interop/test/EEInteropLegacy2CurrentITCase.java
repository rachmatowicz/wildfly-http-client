package org.wildfly.httpclient.interop.test;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EEInteropLegacy2CurrentITCase {
    @Test
    public void testLegacy2CurrentInteroperation() {
        // get a page from undertow
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080")).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println(response.body());
            }

        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
