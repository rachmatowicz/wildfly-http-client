/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.httpclient.transaction;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Transaction;
import javax.transaction.xa.Xid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.httpclient.common.HTTPTestServer;
import org.wildfly.transaction.client.LocalTransactionContext;
import org.wildfly.transaction.client.RemoteUserTransaction;
import io.undertow.server.handlers.CookieImpl;

/**
 * TODO: this test needs a lot of work. It does not really test much at the moment.
 *
 * @author Stuart Douglas
 */
@RunWith(HTTPTestServer.class)
public class SimpleTransactionOperationsTestCase {

    static volatile Xid lastXid;
    static final Map<Xid, TestTransaction> transactions = new ConcurrentHashMap<>();

    private volatile Transaction current;

    @Before
    public void setup() {
        HTTPTestServer.registerServicesHandler("common/v1/affinity", exchange -> exchange.getResponseCookies().put("JSESSIONID", new CookieImpl("JSESSIONID", "foo")));
        HTTPTestServer.registerServicesHandler("txn", new HttpRemoteTransactionService(
                    new LocalTransactionContext(new TestLocalTransactionProvider(transactions, current)),
                    localTransaction -> localTransaction.getProviderInterface(TestTransaction.class).getXid())
                .createHandler());
    }

    private InitialContext createContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        env.put(Context.PROVIDER_URL, HTTPTestServer.getDefaultServerURL());
        return new InitialContext(env);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        InitialContext ic = createContext();
        RemoteUserTransaction result = (RemoteUserTransaction) ic.lookup("txn:UserTransaction");
        result.begin();
        result.commit();

    }
}
