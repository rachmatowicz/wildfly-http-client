package org.wildfly.httpclient.interop.test;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class EEInteropTestServer {

    /**
     * Test server to start and stop Undertow
     */
    public static void main(final String[] args) {
        // start Undertow
        Undertow undertow = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World from EEInteropTestSErver (2.0.2.Final)");
                    }
                }).build();
        undertow.start();
    }
}
