package org.wilfly.httpclient.interop.test;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This test case represents a current version test case running against a legacy version server.
 *
 * @author Richard Achmatowicz
 */
public class EEInteropCurrent2LegacyITCase {
    @Test
    public void testCurrent2LegacyInteroperation() {
        System.out.println("EEInteropCurrent2LegacyITCase:testCurrent2LegacyInteroperation (version 2.0.2.Final)");

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
