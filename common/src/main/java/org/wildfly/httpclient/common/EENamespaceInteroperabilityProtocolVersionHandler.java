package org.wildfly.httpclient.common;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import static org.wildfly.httpclient.common.EENamespaceInteroperability.PROTOCOL_VERSION;
import static org.wildfly.httpclient.common.EENamespaceInteroperability.LATEST_VERSION;

/**
 * Handler used in conjunction with EENamespaceInteroperability protocol negotiation mechanism
 * to return the protocol version of the server on the first request from a client-side
 * HttpConnectionPool.
 *
 * @author Richard Achmatowicz
 */
public class EENamespaceInteroperabilityProtocolVersionHandler implements HttpHandler {

    private final HttpHandler next;

    public EENamespaceInteroperabilityProtocolVersionHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // if a PROTOCOL_VERSION header is present, we need to respond with our protocol version
        if (exchange.getRequestHeaders().getFirst(PROTOCOL_VERSION) != null) {
            exchange.getResponseHeaders().add(PROTOCOL_VERSION, LATEST_VERSION);
        }
        next.handleRequest(exchange);
    }
}
