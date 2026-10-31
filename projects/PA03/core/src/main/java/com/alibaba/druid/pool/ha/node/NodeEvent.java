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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Define the detail of a node update event.
 *
 * @author DigitalSonic
 */
public class NodeEvent {

    private NodeEventTypeEnum type;

    private String nodeName;

    private String url;

    private String username;

    private String password;

    /**
     * Diff the given two Properties.
     *
     * @return A List of AddEvent and DelEvent
     */
    public static List<NodeEvent> getEventsByDiffProperties(Properties previous, Properties next) {
        // TODO
        return null;
    }

    public static List<NodeEvent> generateEvents(Properties properties, List<String> names, NodeEventTypeEnum type) {
        List<NodeEvent> list = new ArrayList<NodeEvent>();
        for (String n : names) {
            NodeEvent event = new NodeEvent();
            event.setType(type);
            event.setNodeName(n);
            event.setUrl(properties.getProperty(n + ".url"));
            event.setUsername(properties.getProperty(n + ".username"));
            event.setPassword(properties.getProperty(n + ".password"));
            list.add(event);
        }
        return list;
    }

    @Override
    public String toString() {
        String str = "NodeEvent{" + "type=" + type + ", nodeName='" + nodeName + '\'' + ", url='" + url + '\'' + ", username='" + username + '\'';
        if (password != null) {
            str += ", password.length=" + password.length();
        }
        str += '}';
        return str;
    }

    public NodeEventTypeEnum getType() {
        // TODO
        return null;
    }

    public void setType(NodeEventTypeEnum type) {
        // TODO
    }

    public String getNodeName() {
        // TODO
        return null;
    }

    public void setNodeName(String nodeName) {
        // TODO
    }

    public String getUrl() {
        // TODO
        return null;
    }

    public void setUrl(String url) {
        // TODO
    }

    public String getUsername() {
        // TODO
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        // TODO
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
