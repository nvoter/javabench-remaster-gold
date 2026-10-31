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

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A NodeList that monitors the change of a file.
 *
 * @author DigitalSonic
 */
public class FileNodeListener extends NodeListener {

    private static final Log LOG = LogFactory.getLog(FileNodeListener.class);

    private Lock lock = new ReentrantLock();

    private String file;

    private int intervalSeconds = 60;

    private ScheduledExecutorService executor;

    /**
     * Start a Scheduler to check the specified file.
     *
     * @see #setIntervalSeconds(int)
     * @see #update()
     */
    @Override
    public void init() {
        // TODO
    }

    /**
     * Load the properties file and diff with the stored Properties.
     *
     * @return A List of the modification
     */
    @Override
    public List<NodeEvent> refresh() {
        // TODO
        return null;
    }

    /**
     * Close the ScheduledExecutorService.
     */
    @Override
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

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        // TODO
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        // TODO
    }
}
