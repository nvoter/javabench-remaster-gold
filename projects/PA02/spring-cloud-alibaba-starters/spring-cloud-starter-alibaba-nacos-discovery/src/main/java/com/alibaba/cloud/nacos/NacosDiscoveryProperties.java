/*
 * Copyright 2013-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.nacos;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.cloud.commons.context.support.PropertySourcesUtils;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.util.InetIPv6Utils;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.utils.UtilAndComs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import static com.alibaba.nacos.api.PropertyKeyConst.ACCESS_KEY;
import static com.alibaba.nacos.api.PropertyKeyConst.ENDPOINT;
import static com.alibaba.nacos.api.PropertyKeyConst.ENDPOINT_PORT;
import static com.alibaba.nacos.api.PropertyKeyConst.NAMESPACE;
import static com.alibaba.nacos.api.PropertyKeyConst.NAMING_LOAD_CACHE_AT_START;
import static com.alibaba.nacos.api.PropertyKeyConst.PASSWORD;
import static com.alibaba.nacos.api.PropertyKeyConst.SECRET_KEY;
import static com.alibaba.nacos.api.PropertyKeyConst.SERVER_ADDR;
import static com.alibaba.nacos.api.PropertyKeyConst.USERNAME;

/**
 * @author dungu.zpf
 * @author xiaojing
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @author <a href="mailto:lyuzb@lyuzb.com">lyuzb</a>
 * @author <a href="mailto:78552423@qq.com">eshun</a>
 * @author freeman
 */
@ConfigurationProperties("spring.cloud.nacos.discovery")
public class NacosDiscoveryProperties {

    /**
     * Prefix of {@link NacosDiscoveryProperties}.
     */
    public static final String PREFIX = "spring.cloud.nacos.discovery";

    private static final Logger log = LoggerFactory.getLogger(NacosDiscoveryProperties.class);

    private static final Pattern PATTERN = Pattern.compile("-(\\w)");

    private static final String IPV4 = "IPv4";

    private static final String IPV6 = "IPv6";

    /**
     * nacos discovery server address.
     */
    @Nullable
    private String serverAddr;

    /**
     * the nacos authentication username.
     */
    @Nullable
    private String username;

    /**
     * the nacos authentication password.
     */
    @Nullable
    private String password;

    /**
     * the domain name of a service, through which the server address can be dynamically
     * obtained.
     */
    @Nullable
    private String endpoint;

    /**
     * namespace, separation registry of different environments.
     */
    @Nullable
    private String namespace;

    /**
     * watch delay,duration to pull new service from nacos server.
     */
    private long watchDelay = 30000;

    /**
     * nacos naming log file name.
     */
    @Nullable
    private String logName;

    /**
     * service name to registry.
     */
    @Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
    @Nullable
    private String service;

    /**
     * weight for service instance, the larger the value, the larger the weight.
     */
    private float weight = 1;

    /**
     * cluster name for nacos .
     */
    @Nullable
    private String clusterName;

    /**
     * group name for nacos.
     */
    private String group = "DEFAULT_GROUP";

    /**
     * naming load from local cache at application start. true is load.
     */
    private String namingLoadCacheAtStart = "false";

    /**
     * extra metadata to register.
     */
    private Map<String, String> metadata = new HashMap<>();

    /**
     * if you just want to subscribe, but don't want to register your service, set it to
     * false.
     */
    private boolean registerEnabled = true;

    /**
     * The ip address your want to register for your service instance, needn't to set it
     * if the auto detect ip works well.
     */
    @Nullable
    private String ip;

    /**
     * which network interface's ip you want to register.
     */
    private String networkInterface = "";

    /**
     * choose IPv4 or IPv6,if you don't set it will choose IPv4.
     * When IPv6 is chosen but no IPv6 can be found, system will automatically find IPv4 to ensure there is an
     * available service address.
     */
    @Nullable
    private String ipType;

    /**
     * The port your want to register for your service instance, needn't to set it if the
     * auto detect port works well.
     */
    private int port = -1;

    /**
     * whether your service is a https service.
     */
    private boolean secure = false;

    /**
     * access key for namespace.
     */
    @Nullable
    private String accessKey;

    /**
     * secret key for namespace.
     */
    @Nullable
    private String secretKey;

    /**
     * Heart beat interval. Time unit: millisecond.
     */
    @Nullable
    private Integer heartBeatInterval;

    /**
     * Heart beat timeout. Time unit: millisecond.
     */
    @Nullable
    private Integer heartBeatTimeout;

    /**
     * Ip delete timeout. Time unit: millisecond.
     */
    @Nullable
    private Integer ipDeleteTimeout;

    /**
     * If instance is enabled to accept request. The default value is true.
     */
    private boolean instanceEnabled = true;

    /**
     * If instance is ephemeral.The default value is true.
     */
    private boolean ephemeral = true;

    /**
     * Whether to enable nacos failure tolerance. If enabled, nacos will return cached
     * values when exceptions occur.
     */
    private boolean failureToleranceEnabled;

    /**
     * Throw exceptions during service registration if true, otherwise, log error
     * (defaults to true).
     */
    private boolean failFast = true;

    /**
     * graceful shutdown wait time. Time unit: millisecond.
     * default is 10s
     * When the Springboot shutdown hook is executed, remove the Nacos service, wait for 10 seconds, and then Tomcat rejects the request
     */
    private Integer gracefulShutdownWaitTime = 10 * 1000;

    @Autowired
    private InetIPv6Utils inetIPv6Utils;

    @Autowired
    private InetUtils inetUtils;

    @Autowired
    private Environment environment;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void init() throws Exception {
        // TODO
    }

    /**
     * copy {@link NacosDiscoveryProperties} .
     * @return NacosDiscoveryProperties
     */
    private NacosDiscoveryProperties copyNacosDiscoveryProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        nacosDiscoveryProperties.endpoint = this.endpoint;
        nacosDiscoveryProperties.namespace = this.namespace;
        nacosDiscoveryProperties.logName = this.logName;
        nacosDiscoveryProperties.weight = this.weight;
        nacosDiscoveryProperties.clusterName = this.clusterName;
        nacosDiscoveryProperties.service = this.service;
        nacosDiscoveryProperties.registerEnabled = this.registerEnabled;
        nacosDiscoveryProperties.ip = this.ip;
        nacosDiscoveryProperties.ipType = this.ipType;
        nacosDiscoveryProperties.networkInterface = this.networkInterface;
        nacosDiscoveryProperties.port = this.port;
        nacosDiscoveryProperties.secure = this.secure;
        nacosDiscoveryProperties.metadata = new HashMap<>(this.metadata);
        nacosDiscoveryProperties.serverAddr = this.serverAddr;
        nacosDiscoveryProperties.accessKey = this.accessKey;
        nacosDiscoveryProperties.secretKey = this.secretKey;
        nacosDiscoveryProperties.heartBeatInterval = this.heartBeatInterval;
        nacosDiscoveryProperties.heartBeatTimeout = this.heartBeatTimeout;
        nacosDiscoveryProperties.ipDeleteTimeout = this.ipDeleteTimeout;
        nacosDiscoveryProperties.namingLoadCacheAtStart = this.namingLoadCacheAtStart;
        nacosDiscoveryProperties.watchDelay = this.watchDelay;
        nacosDiscoveryProperties.group = this.group;
        nacosDiscoveryProperties.username = this.username;
        nacosDiscoveryProperties.password = this.password;
        nacosDiscoveryProperties.instanceEnabled = this.instanceEnabled;
        nacosDiscoveryProperties.ephemeral = this.ephemeral;
        nacosDiscoveryProperties.failureToleranceEnabled = this.failureToleranceEnabled;
        nacosDiscoveryProperties.failFast = this.failFast;
        nacosDiscoveryProperties.gracefulShutdownWaitTime = this.gracefulShutdownWaitTime;
        nacosDiscoveryProperties.inetIPv6Utils = this.inetIPv6Utils;
        nacosDiscoveryProperties.inetUtils = this.inetUtils;
        nacosDiscoveryProperties.environment = this.environment;
        nacosDiscoveryProperties.nacosServiceManager = this.nacosServiceManager;
        nacosDiscoveryProperties.applicationEventPublisher = this.applicationEventPublisher;
        return nacosDiscoveryProperties;
    }

    /**
     * recommend to use {@link NacosServiceManager#getNamingService()}.
     * @return NamingService
     */
    @Deprecated
    public NamingService namingServiceInstance() {
        return nacosServiceManager.getNamingService();
    }

    @Nullable
    public String getEndpoint() {
        // TODO
        return null;
    }

    public void setEndpoint(@Nullable String endpoint) {
        this.endpoint = endpoint;
    }

    @Nullable
    public String getNamespace() {
        // TODO
        return null;
    }

    public void setNamespace(@Nullable String namespace) {
        this.namespace = namespace;
    }

    @Nullable
    public String getLogName() {
        // TODO
        return null;
    }

    public void setLogName(@Nullable String logName) {
        this.logName = logName;
    }

    public void setInetUtils(InetUtils inetUtils) {
        this.inetUtils = inetUtils;
    }

    public float getWeight() {
        // TODO
        return 0.0f;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Nullable
    public String getClusterName() {
        // TODO
        return null;
    }

    public void setClusterName(@Nullable String clusterName) {
        // TODO
    }

    @Nullable
    public String getService() {
        // TODO
        return null;
    }

    public void setService(@Nullable String service) {
        this.service = service;
    }

    public boolean isRegisterEnabled() {
        return registerEnabled;
    }

    public void setRegisterEnabled(boolean registerEnabled) {
        this.registerEnabled = registerEnabled;
    }

    @Nullable
    public String getIp() {
        // TODO
        return null;
    }

    public void setIp(@Nullable String ip) {
        this.ip = ip;
    }

    @Nullable
    public String getIpType() {
        return ipType;
    }

    public void setIpType(@Nullable String ipType) {
        // TODO
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        // TODO
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSecure() {
        // TODO
        return false;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Map<String, String> getMetadata() {
        // TODO
        return null;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Nullable
    public String getServerAddr() {
        // TODO
        return null;
    }

    public void setServerAddr(@Nullable String serverAddr) {
        // TODO
    }

    @Nullable
    public String getAccessKey() {
        // TODO
        return null;
    }

    public void setAccessKey(@Nullable String accessKey) {
        this.accessKey = accessKey;
    }

    @Nullable
    public String getSecretKey() {
        // TODO
        return null;
    }

    public void setSecretKey(@Nullable String secretKey) {
        this.secretKey = secretKey;
    }

    @Nullable
    public Integer getHeartBeatInterval() {
        // TODO
        return null;
    }

    public void setHeartBeatInterval(@Nullable Integer heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    @Nullable
    public Integer getHeartBeatTimeout() {
        // TODO
        return null;
    }

    public void setHeartBeatTimeout(@Nullable Integer heartBeatTimeout) {
        this.heartBeatTimeout = heartBeatTimeout;
    }

    @Nullable
    public Integer getIpDeleteTimeout() {
        // TODO
        return null;
    }

    public void setIpDeleteTimeout(@Nullable Integer ipDeleteTimeout) {
        // TODO
    }

    public String getNamingLoadCacheAtStart() {
        // TODO
        return null;
    }

    public void setNamingLoadCacheAtStart(String namingLoadCacheAtStart) {
        this.namingLoadCacheAtStart = namingLoadCacheAtStart;
    }

    public long getWatchDelay() {
        return watchDelay;
    }

    public void setWatchDelay(long watchDelay) {
        this.watchDelay = watchDelay;
    }

    public String getGroup() {
        // TODO
        return null;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getPassword() {
        // TODO
        return null;
    }

    public void setPassword(@Nullable String password) {
        // TODO
    }

    public boolean isInstanceEnabled() {
        return instanceEnabled;
    }

    public void setInstanceEnabled(boolean instanceEnabled) {
        this.instanceEnabled = instanceEnabled;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public boolean isFailureToleranceEnabled() {
        return failureToleranceEnabled;
    }

    public void setFailureToleranceEnabled(boolean failureToleranceEnabled) {
        this.failureToleranceEnabled = failureToleranceEnabled;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public Integer getGracefulShutdownWaitTime() {
        // TODO
        return null;
    }

    public void setGracefulShutdownWaitTime(Integer gracefulShutdownWaitTime) {
        // TODO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NacosDiscoveryProperties that = (NacosDiscoveryProperties) o;
        return watchDelay == that.watchDelay && Float.compare(that.weight, weight) == 0 && registerEnabled == that.registerEnabled && port == that.port && secure == that.secure && instanceEnabled == that.instanceEnabled && ephemeral == that.ephemeral && failureToleranceEnabled == that.failureToleranceEnabled && Objects.equals(serverAddr, that.serverAddr) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(endpoint, that.endpoint) && Objects.equals(namespace, that.namespace) && Objects.equals(logName, that.logName) && Objects.equals(service, that.service) && Objects.equals(clusterName, that.clusterName) && Objects.equals(group, that.group) && Objects.equals(namingLoadCacheAtStart, that.namingLoadCacheAtStart) && Objects.equals(metadata, that.metadata) && Objects.equals(ip, that.ip) && Objects.equals(networkInterface, that.networkInterface) && Objects.equals(ipType, that.ipType) && Objects.equals(accessKey, that.accessKey) && Objects.equals(secretKey, that.secretKey) && Objects.equals(heartBeatInterval, that.heartBeatInterval) && Objects.equals(heartBeatTimeout, that.heartBeatTimeout) && Objects.equals(failFast, that.failFast) && Objects.equals(ipDeleteTimeout, that.ipDeleteTimeout) && Objects.equals(gracefulShutdownWaitTime, that.gracefulShutdownWaitTime);
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }

    public void overrideFromEnv(Environment env) {
        if (StringUtils.isEmpty(this.getServerAddr())) {
            String serverAddr = env.resolvePlaceholders("${spring.cloud.nacos.discovery.server-addr:}");
            if (StringUtils.isEmpty(serverAddr)) {
                serverAddr = env.resolvePlaceholders("${spring.cloud.nacos.server-addr:127.0.0.1:8848}");
            }
            this.setServerAddr(serverAddr);
        }
        if (StringUtils.isEmpty(this.getNamespace())) {
            this.setNamespace(env.resolvePlaceholders("${spring.cloud.nacos.discovery.namespace:}"));
        }
        if (StringUtils.isEmpty(this.getAccessKey())) {
            this.setAccessKey(env.resolvePlaceholders("${spring.cloud.nacos.discovery.access-key:}"));
        }
        if (StringUtils.isEmpty(this.getSecretKey())) {
            this.setSecretKey(env.resolvePlaceholders("${spring.cloud.nacos.discovery.secret-key:}"));
        }
        if (StringUtils.isEmpty(this.getLogName())) {
            this.setLogName(env.resolvePlaceholders("${spring.cloud.nacos.discovery.log-name:}"));
        }
        if (StringUtils.isEmpty(this.getClusterName())) {
            this.setClusterName(env.resolvePlaceholders("${spring.cloud.nacos.discovery.cluster-name:}"));
        }
        if (StringUtils.isEmpty(this.getEndpoint())) {
            this.setEndpoint(env.resolvePlaceholders("${spring.cloud.nacos.discovery.endpoint:}"));
        }
        if (StringUtils.isEmpty(this.getGroup())) {
            this.setGroup(env.resolvePlaceholders("${spring.cloud.nacos.discovery.group:}"));
        }
        if (StringUtils.isEmpty(this.getUsername())) {
            this.setUsername(env.resolvePlaceholders("${spring.cloud.nacos.username:}"));
        }
        if (StringUtils.isEmpty(this.getPassword())) {
            this.setPassword(env.resolvePlaceholders("${spring.cloud.nacos.password:}"));
        }
    }

    public Properties getNacosProperties() {
        Properties properties = new Properties();
        properties.put(SERVER_ADDR, Objects.toString(serverAddr, ""));
        properties.put(USERNAME, Objects.toString(username, ""));
        properties.put(PASSWORD, Objects.toString(password, ""));
        properties.put(NAMESPACE, Objects.toString(namespace, ""));
        properties.put(UtilAndComs.NACOS_NAMING_LOG_NAME, Objects.toString(logName, ""));
        String endpointValue = Objects.toString(endpoint, "");
        if (endpointValue.contains(":")) {
            int index = endpointValue.indexOf(":");
            properties.put(ENDPOINT, endpointValue.substring(0, index));
            properties.put(ENDPOINT_PORT, endpointValue.substring(index + 1));
        } else {
            properties.put(ENDPOINT, endpointValue);
        }
        properties.put(ACCESS_KEY, Objects.toString(accessKey, ""));
        properties.put(SECRET_KEY, Objects.toString(secretKey, ""));
        // only used for instance.setClusterName()
        //		properties.put(CLUSTER_NAME, clusterName);
        properties.put(NAMING_LOAD_CACHE_AT_START, namingLoadCacheAtStart);
        enrichNacosDiscoveryProperties(properties);
        return properties;
    }

    private void enrichNacosDiscoveryProperties(Properties nacosDiscoveryProperties) {
        Map<String, Object> properties = PropertySourcesUtils.getSubProperties((ConfigurableEnvironment) environment, PREFIX);
        properties.forEach((k, v) -> nacosDiscoveryProperties.putIfAbsent(resolveKey(k), String.valueOf(v)));
    }

    private String resolveKey(String key) {
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase(Locale.ROOT));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
