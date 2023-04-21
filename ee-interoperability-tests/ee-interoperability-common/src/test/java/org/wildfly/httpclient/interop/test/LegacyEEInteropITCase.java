package org.wildfly.httpclient.interop.test;

import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.jboss.ejb.client.URIAffinity;
import org.junit.Assert;
import org.junit.Test;
import org.wildfly.httpclient.ejb.EchoRemote;

import java.net.URI;

/*
 * A test case for testing interaction between a legacy EJB client and a curent server.
 *
 * Assumption:
 * - the server has an EJBHandler installed which can handle invocations from the Echo interface.
 */

public class LegacyEEInteropITCase {

    public static final String APP = "wildfly-app";
    public static final String MODULE = "wildfly-ejb-remote-server-side";
    public static final String BEAN = "EchoBean";

    @Test
    public void testLegacy2CurrentInteroperation() throws Exception {
        System.out.println("LegacyEEInteropITCase:testCurrent2LegacyInteroperation (version 1.1.13.Final)");

        System.out.println("LegacyEEInteropITCase:testCurrent2LegacyInteroperation(): create proxy");
        final StatelessEJBLocator<EchoRemote> statelessEJBLocator = new StatelessEJBLocator<>(EchoRemote.class, APP, MODULE, BEAN, "");
        final EchoRemote proxy = EJBClient.createProxy(statelessEJBLocator);
        final String message = "Hello, World!";
        EJBClient.setStrongAffinity(proxy, URIAffinity.forUri(new URI("http://localhost:7788")));

        // first message, handshake initialization, decide marshallers/unmarshallers
        System.out.println("LegacyEEInteropITCase:testCurrent2LegacyInteroperation(): first invocation");
        String echo = proxy.echo(message);
        Assert.assertEquals("Unexxpected echo message", message, echo);

        // second message, handshake completion, agree on marshallers/unmarshallers
        System.out.println("LegacyEEInteropITCase:testCurrent2LegacyInteroperation(): second invocation");
        echo = proxy.echo(message);
        Assert.assertEquals("Unexxpected echo message", message, echo);

        // third message message, stable marshallers/unmarshallers
        System.out.println("LegacyEEInteropITCase:testCurrent2LegacyInteroperation(): third invocation");
        echo = proxy.echo(message);
        Assert.assertEquals("Unexxpected echo message", message, echo);
    }
}
