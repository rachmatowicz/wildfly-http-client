package org.wildfly.httpclient.interop.test;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This test case represents a legacy version test case running against a current version server
 * for a current version greater than the version of this project. It will be executed using the fallsafe
 * plugin option dependenciesToScan, which allows executing test classes from a dependency.
 *
 * @author Richard Achmatowicz
 */
public class EEInteropITCase {
    @Test
    public void testLegacy2CurrentInteroperation() {
        System.out.println("EEInteropITCase:testLegacy2CurrentInteroperation (2.0.2.Final)");
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
