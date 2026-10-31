/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.pool.xa;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.lang.reflect.Method;
import java.sql.Connection;

public class JtdsXAResource implements XAResource {

    private static final Log LOG = LogFactory.getLog(JtdsXAResource.class);

    private final Connection connection;

    private final JtdsXAConnection xaConnection;

    private String rmHost;

    private static Method method;

    public JtdsXAResource(JtdsXAConnection xaConnection, Connection connection) {
        this.xaConnection = xaConnection;
        this.connection = connection;
        if (method == null) {
            try {
                method = connection.getClass().getMethod("getRmHost");
            } catch (Exception e) {
                LOG.error("getRmHost method error", e);
            }
        }
        if (method != null) {
            try {
                rmHost = (String) method.invoke(connection);
            } catch (Exception e) {
                LOG.error("getRmHost error", e);
            }
        }
    }

    protected JtdsXAConnection getResourceManager() {
        return xaConnection;
    }

    protected String getRmHost() {
        return this.rmHost;
    }

    @Override
    public void commit(Xid xid, boolean commit) throws XAException {
        // TODO
    }

    @Override
    public void end(Xid xid, int flags) throws XAException {
        // TODO
    }

    @Override
    public void forget(Xid xid) throws XAException {
        // TODO
    }

    @Override
    public int getTransactionTimeout() throws XAException {
        return 0;
    }

    @Override
    public boolean isSameRM(XAResource xares) throws XAException {
        // TODO
        return false;
    }

    @Override
    public int prepare(Xid xid) throws XAException {
        // TODO
        return 0;
    }

    @Override
    public Xid[] recover(int flags) throws XAException {
        // TODO
        return null;
    }

    @Override
    public void rollback(Xid xid) throws XAException {
        // TODO
    }

    @Override
    public boolean setTransactionTimeout(int seconds) throws XAException {
        return false;
    }

    @Override
    public void start(Xid xid, int flags) throws XAException {
        // TODO
    }
}
