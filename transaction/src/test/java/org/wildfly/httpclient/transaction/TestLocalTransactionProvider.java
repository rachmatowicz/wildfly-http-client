/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2023 Red Hat, Inc., and individual contributors
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

import org.wildfly.transaction.client.XAImporter;
import org.wildfly.transaction.client.spi.LocalTransactionProvider;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.Xid;
import java.util.Map;


/*
 * A transaction implementation used for testing purposes.
 * This transaction implementation provides an Xid, a fixed status of 0.
 */
public class TestLocalTransactionProvider implements LocalTransactionProvider {

    Transaction current;
    Map<Xid, TestTransaction> transactions;

    public TestLocalTransactionProvider(Map<Xid, TestTransaction> transactions, Transaction current) {
        this.current = current;
        this.transactions = transactions;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return new TestTransactionManager(current);
    }

    @Override
    public XAImporter getXAImporter() {
        return new TestXAImporter(transactions);
    }

    @Override
    public Transaction createNewTransaction(int timeout) throws SystemException, SecurityException {
        TestTransaction testTransaction = new TestTransaction();
        transactions.put(testTransaction.getXid(), testTransaction);
        return testTransaction;
    }

    @Override
    public boolean isImported(Transaction transaction) throws IllegalArgumentException {
        return false;
    }

    @Override
    public void registerInterposedSynchronization(Transaction transaction, Synchronization sync) throws IllegalArgumentException {
        // noop
    }

    @Override
    public Object getResource(Transaction transaction, Object key) {
        return null;
    }

    @Override
    public void putResource(Transaction transaction, Object key, Object value) throws IllegalArgumentException {
        // noop
    }

    @Override
    public Object putResourceIfAbsent(Transaction transaction, Object key, Object value) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean getRollbackOnly(Transaction transaction) throws IllegalArgumentException {
        return false;
    }

    @Override
    public Object getKey(Transaction transaction) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void commitLocal(Transaction transaction) throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        // noop
    }

    @Override
    public void rollbackLocal(Transaction transaction) throws IllegalStateException, SystemException {
        // noop
    }

    @Override
    public void dropLocal(Transaction transaction) {
        // noop
    }

    @Override
    public void dropRemote(Transaction transaction) {
        // noop
    }

    @Override
    public int getTimeout(Transaction transaction) {
        return 0;
    }

    @Override
    public Xid getXid(Transaction transaction) {
        return null;
    }

    @Override
    public String getNodeName() {
        return null;
    }

    @Override
    public <T> T getProviderInterface(Transaction transaction, Class<T> providerInterfaceType) {
        return providerInterfaceType.isInstance(transaction) ? (T) transaction : null;
    }
}
