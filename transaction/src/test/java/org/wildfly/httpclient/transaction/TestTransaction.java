package org.wildfly.httpclient.transaction;

import org.wildfly.transaction.client.SimpleXid;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.util.Random;

/*
 * A transaction implementation used for testing purposes.
 * This transaction implementation provides an Xid, a fixed status of 0.
 */
public class TestTransaction implements Transaction {

    private final Xid xid;

    public TestTransaction() {
        byte[] global = new byte[10];
        byte[] branch = new byte[10];
        new Random().nextBytes(global);
        new Random().nextBytes(branch);
        xid = new SimpleXid(1, global, branch);
        // not good
        SimpleTransactionOperationsTestCase.lastXid = xid;
    }

    public Xid getXid() {
        return xid;
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        // noop
    }

    @Override
    public void rollback() throws IllegalStateException, SystemException {
        // noop
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        // noop
    }

    @Override
    public boolean delistResource(XAResource xaRes, int flag) throws IllegalStateException, SystemException {
        return false;
    }

    @Override
    public boolean enlistResource(XAResource xaRes) throws RollbackException, IllegalStateException, SystemException {
        return false;
    }

    @Override
    public int getStatus() throws SystemException {
        return 0;
    }

    @Override
    public void registerSynchronization(Synchronization sync) throws RollbackException, IllegalStateException, SystemException {
        // noop
    }

}
