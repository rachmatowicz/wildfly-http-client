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

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/*
 * A TransactionManager implementation used for testing purposes.
 * This transaction manager holds a single current transaction which can be suspebd
 * and resumed and has a fixed status of 0.
 */
public class TestTransactionManager implements TransactionManager {

    private volatile Transaction current;

    public TestTransactionManager(Transaction current) {
        this.current = current;
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        // noop
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        // noop
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        // noop
    }

    @Override
    public int getStatus() throws SystemException {
        return 0;
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        return current;
    }

    @Override
    public Transaction suspend() throws SystemException {
        Transaction old = current;
        current = null;
        return old;
    }

    @Override
    public void resume(Transaction old) throws InvalidTransactionException, IllegalStateException, SystemException {
        current = old;
    }


    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        // noop
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
        // noop
    }
}
