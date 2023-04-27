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

import org.wildfly.transaction.client.ImportResult;
import org.wildfly.transaction.client.XAImporter;
import org.wildfly.transaction.client.spi.SubordinateTransactionControl;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import java.util.Map;

/*
 * An XAImporter class to be used for testing purposes.
 *
 */
public class TestXAImporter implements XAImporter {

    Map<Xid, TestTransaction> transactions;

    public TestXAImporter(Map<Xid, TestTransaction> transactions) {
        this.transactions = transactions;
    }

    /*
     *
     */
    public ImportResult<?> findOrImportTransaction(Xid xid, int timeout) throws XAException {
        TestTransaction existing = transactions.get(xid);
        return new ImportResult<Transaction>(existing, new SubordinateTransactionControl() {
            @Override
            public void rollback() throws XAException {
                try {
                    existing.rollback();
                } catch(SystemException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void end(int flags) throws XAException {
                // noop
            }

            @Override
            public void beforeCompletion() throws XAException {
                // noop
            }

            @Override
            public int prepare() throws XAException {
                return 0;
            }

            @Override
            public void forget() throws XAException {
                // forget
            }

            @Override
            public void commit(boolean onePhase) throws XAException {
                //commit
            }
        }, false);
    }

    @Override
    public ImportResult<?> findOrImportTransaction(Xid xid, int timeout, boolean doNotImport) throws XAException {
        if (doNotImport && !transactions.containsKey(xid)) {
            return null;
        }
        return findOrImportTransaction(xid, timeout);
    }

    @Override
    public Transaction findExistingTransaction(Xid xid) throws XAException {
        return transactions.get(xid);
    }

    @Override
    public void commit(Xid xid, boolean onePhase) throws XAException {
        throw new RuntimeException();
    }

    @Override
    public void forget(Xid xid) throws XAException {
        throw new RuntimeException();
    }

    @Override
    public Xid[] recover(int flag, String parentName) throws XAException {
        throw new RuntimeException();
    }
}
