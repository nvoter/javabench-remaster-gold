/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
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
package com.alibaba.druid.pool.ha.node;

import com.alibaba.druid.pool.ha.HighAvailableDataSource;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Update the DataSource Connection Pool when notified.
 *
 * @author DigitalSonic
 */
public class PoolUpdater implements Observer {

    public static final int DEFAULT_INTERVAL = 60;

    private static final Log LOG = LogFactory.getLog(PoolUpdater.class);

    private Set<String> nodesToDel = new CopyOnWriteArraySet<String>();

    private HighAvailableDataSource highAvailableDataSource;

    private Lock lock = new ReentrantLock();

    private ScheduledExecutorService executor;

    private int intervalSeconds = DEFAULT_INTERVAL;

    private volatile boolean inited;

    private boolean allowEmptyPool;

    public boolean isInited() {
        // TODO
        return false;
    }

    public PoolUpdater(HighAvailableDataSource highAvailableDataSource) {
        // TODO
    }

    /**
     * Create a ScheduledExecutorService to remove unused DataSources.
     */
    public void init() {
        // TODO
    }

    public void destroy() {
        if (executor == null || executor.isShutdown()) {
            return;
        }
        try {
            executor.shutdown();
        } catch (Exception e) {
            LOG.error("Can NOT shutdown the ScheduledExecutorService.", e);
        }
    }

    /**
     * Process the given NodeEvent[]. Maybe add / delete some nodes.
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO
    }

    /**
     * Remove unused DataSources.
     */
    public void removeDataSources() {
        // TODO
    }

    protected void addNode(NodeEvent event) {
        // TODO
    }

    protected void deleteNode(NodeEvent event) {
        // TODO
    }

    private void cancelBlacklistNode(String nodeName) {
        LOG.info("Cancel the deletion of Node " + nodeName + ".");
        nodesToDel.remove(nodeName);
        highAvailableDataSource.removeBlackList(nodeName);
    }

    private void blacklistNode(String nodeName) {
        LOG.info("Deleting Node " + nodeName + ", just add it into blacklist.");
        nodesToDel.add(nodeName);
        highAvailableDataSource.addBlackList(nodeName);
    }

    public HighAvailableDataSource getHighAvailableDataSource() {
        return highAvailableDataSource;
    }

    public void setHighAvailableDataSource(HighAvailableDataSource highAvailableDataSource) {
        this.highAvailableDataSource = highAvailableDataSource;
    }

    public Set<String> getNodesToDel() {
        // TODO
        return null;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public boolean isAllowEmptyPool() {
        return allowEmptyPool;
    }

    public void setAllowEmptyPool(boolean allowEmptyPool) {
        this.allowEmptyPool = allowEmptyPool;
    }
}
