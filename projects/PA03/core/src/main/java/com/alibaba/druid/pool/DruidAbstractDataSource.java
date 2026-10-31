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
package com.alibaba.druid.pool;

import com.alibaba.druid.DbType;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.filter.FilterManager;
import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import com.alibaba.druid.stat.DataSourceMonitorable;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.*;
import javax.management.JMException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author wenshao [szujobs@hotmail.com]
 * @author ljw [ljw2083@alibaba-inc.com]
 */
public abstract class DruidAbstractDataSource extends WrapperAdapter implements DruidAbstractDataSourceMBean, DataSource, DataSourceProxy, Serializable, DataSourceMonitorable {

    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(DruidAbstractDataSource.class);

    public static final int DEFAULT_INITIAL_SIZE = 0;

    public static final int DEFAULT_MAX_ACTIVE_SIZE = 8;

    public static final int DEFAULT_MAX_IDLE = 8;

    public static final int DEFAULT_MIN_IDLE = 0;

    public static final int DEFAULT_MAX_WAIT = -1;

    //
    public static final String DEFAULT_VALIDATION_QUERY = null;

    public static final boolean DEFAULT_TEST_ON_BORROW = false;

    public static final boolean DEFAULT_TEST_ON_RETURN = false;

    public static final boolean DEFAULT_WHILE_IDLE = true;

    public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 60 * 1000L;

    public static final long DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS = 500;

    public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;

    public static final int DEFAULT_TIME_CONNECT_TIMEOUT_MILLIS = 10_000;

    public static final int DEFAULT_TIME_SOCKET_TIMEOUT_MILLIS = 10_000;

    public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1000L * 60L * 30L;

    public static final long DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS = 1000L * 60L * 60L * 7;

    public static final long DEFAULT_PHY_TIMEOUT_MILLIS = -1;

    protected volatile boolean defaultAutoCommit = true;

    protected volatile Boolean defaultReadOnly;

    protected volatile Integer defaultTransactionIsolation;

    protected volatile String defaultCatalog;

    protected String name;

    protected volatile String username;

    protected volatile String password;

    protected volatile String jdbcUrl;

    protected volatile String driverClass;

    protected volatile ClassLoader driverClassLoader;

    protected volatile Properties connectProperties = new Properties();

    protected volatile PasswordCallback passwordCallback;

    protected volatile NameCallback userCallback;

    protected volatile int initialSize = DEFAULT_INITIAL_SIZE;

    protected volatile int maxActive = DEFAULT_MAX_ACTIVE_SIZE;

    protected volatile int minIdle = DEFAULT_MIN_IDLE;

    protected volatile int maxIdle = DEFAULT_MAX_IDLE;

    protected volatile long maxWait = DEFAULT_MAX_WAIT;

    protected int notFullTimeoutRetryCount;

    protected volatile String validationQuery = DEFAULT_VALIDATION_QUERY;

    protected volatile int validationQueryTimeout = -1;

    protected volatile boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;

    protected volatile boolean testOnReturn = DEFAULT_TEST_ON_RETURN;

    protected volatile boolean testWhileIdle = DEFAULT_WHILE_IDLE;

    protected volatile boolean poolPreparedStatements;

    protected volatile boolean sharePreparedStatements;

    protected volatile int maxPoolPreparedStatementPerConnectionSize = 10;

    protected volatile boolean inited;

    protected volatile boolean initExceptionThrow = true;

    protected PrintWriter logWriter = new PrintWriter(System.out);

    protected List<Filter> filters = new CopyOnWriteArrayList<Filter>();

    private boolean clearFiltersEnable = true;

    protected volatile ExceptionSorter exceptionSorter;

    protected Driver driver;

    // milliSeconds
    protected volatile int connectTimeout;

    // milliSeconds
    protected volatile int socketTimeout;

    private volatile String connectTimeoutStr;

    private volatile String socketTimeoutStr;

    // seconds
    protected volatile int queryTimeout;

    // seconds
    protected volatile int transactionQueryTimeout;

    protected long createTimespan;

    protected volatile int maxWaitThreadCount = -1;

    protected volatile boolean accessToUnderlyingConnectionAllowed = true;

    protected volatile long timeBetweenEvictionRunsMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

    protected volatile int numTestsPerEvictionRun = DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

    protected volatile long minEvictableIdleTimeMillis = DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    protected volatile long maxEvictableIdleTimeMillis = DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS;

    protected volatile long keepAliveBetweenTimeMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS * 2;

    protected volatile long phyTimeoutMillis = DEFAULT_PHY_TIMEOUT_MILLIS;

    protected volatile long phyMaxUseCount = -1;

    protected volatile boolean removeAbandoned;

    protected volatile long removeAbandonedTimeoutMillis = 300 * 1000;

    protected volatile boolean logAbandoned;

    protected volatile int maxOpenPreparedStatements = -1;

    protected volatile List<String> connectionInitSqls;

    protected volatile String dbTypeName;

    protected volatile long timeBetweenConnectErrorMillis = DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS;

    protected volatile ValidConnectionChecker validConnectionChecker;

    protected volatile boolean usePingMethod;

    protected final Map<DruidPooledConnection, Object> activeConnections = new IdentityHashMap<DruidPooledConnection, Object>();

    protected static final Object PRESENT = new Object();

    protected long id;

    protected int connectionErrorRetryAttempts = 1;

    protected boolean breakAfterAcquireFailure;

    protected long transactionThresholdMillis;

    protected final java.util.Date createdTime = new java.util.Date();

    protected java.util.Date initedTime;

    protected volatile long errorCount;

    protected volatile long dupCloseCount;

    protected volatile long startTransactionCount;

    protected volatile long commitCount;

    protected volatile long rollbackCount;

    protected volatile long cachedPreparedStatementHitCount;

    protected volatile long preparedStatementCount;

    protected volatile long closedPreparedStatementCount;

    protected volatile long cachedPreparedStatementCount;

    protected volatile long cachedPreparedStatementDeleteCount;

    protected volatile long cachedPreparedStatementMissCount;

    protected volatile long userPasswordVersion;

    private volatile FilterChainImpl filterChain;

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> errorCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "errorCount"), dupCloseCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "dupCloseCount"), startTransactionCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "startTransactionCount"), commitCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "commitCount"), rollbackCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "rollbackCount"), cachedPreparedStatementHitCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementHitCount"), preparedStatementCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "preparedStatementCount"), closedPreparedStatementCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "closedPreparedStatementCount"), cachedPreparedStatementCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementCount"), cachedPreparedStatementDeleteCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementDeleteCount"), cachedPreparedStatementMissCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementMissCount"), userPasswordVersionUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "userPasswordVersion");

    protected static final AtomicReferenceFieldUpdater<DruidAbstractDataSource, FilterChainImpl> filterChainUpdater = AtomicReferenceFieldUpdater.newUpdater(DruidAbstractDataSource.class, FilterChainImpl.class, "filterChain");

    protected final Histogram transactionHistogram = new Histogram(1, 10, 100, 1000, 10 * 1000, 100 * 1000);

    private boolean dupCloseLogEnable;

    private ObjectName objectName;

    protected volatile long executeCount;

    protected volatile long executeQueryCount;

    protected volatile long executeUpdateCount;

    protected volatile long executeBatchCount;

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeQueryCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeQueryCount");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeUpdateCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeUpdateCount");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeBatchCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeBatchCount");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeCount");

    protected volatile Throwable createError;

    protected volatile Throwable lastError;

    protected volatile long lastErrorTimeMillis;

    protected volatile Throwable lastCreateError;

    protected volatile long lastCreateErrorTimeMillis;

    protected volatile long lastCreateStartTimeMillis;

    protected boolean isOracle;

    protected boolean isMySql;

    protected boolean useOracleImplicitCache = true;

    protected ReentrantLock lock;

    protected Condition notEmpty;

    protected Condition empty;

    protected ReentrantLock activeConnectionLock = new ReentrantLock();

    protected volatile int createErrorCount;

    protected volatile int creatingCount;

    protected volatile int directCreateCount;

    protected volatile long createCount;

    protected volatile long destroyCount;

    protected volatile long createStartNanos;

    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> createErrorCountUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "createErrorCount");

    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> creatingCountUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "creatingCount");

    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> directCreateCountUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "directCreateCount");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> createCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "createCount");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> destroyCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "destroyCount");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> createStartNanosUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "createStartNanos");

    private boolean useLocalSessionState = true;

    private boolean keepConnectionUnderlyingTransactionIsolation;

    protected long timeBetweenLogStatsMillis;

    protected DruidDataSourceStatLogger statLogger = new DruidDataSourceStatLoggerImpl();

    protected boolean asyncCloseConnectionEnable;

    protected int maxCreateTaskCount = 3;

    protected boolean failFast;

    protected volatile int failContinuous;

    protected volatile long failContinuousTimeMillis;

    protected ScheduledExecutorService destroyScheduler;

    protected ScheduledExecutorService createScheduler;

    protected Executor netTimeoutExecutor;

    protected volatile boolean netTimeoutError;

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> failContinuousTimeMillisUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "failContinuousTimeMillis");

    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> failContinuousUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "failContinuous");

    protected boolean initVariants;

    protected boolean initGlobalVariants;

    protected volatile boolean onFatalError;

    protected volatile int onFatalErrorMaxActive;

    protected volatile int fatalErrorCount;

    protected volatile int fatalErrorCountLastShrink;

    protected volatile long lastFatalErrorTimeMillis;

    protected volatile String lastFatalErrorSql;

    protected volatile Throwable lastFatalError;

    protected volatile Throwable keepAliveError;

    /**
     * Constructs a new DruidAbstractDataSource with a specified lock fairness setting.
     *
     * @param lockFair a boolean value indicating whether the lock should be fair or not
     */
    public DruidAbstractDataSource(boolean lockFair) {
        lock = new ReentrantLock(lockFair);
        notEmpty = lock.newCondition();
        empty = lock.newCondition();
    }

    protected FilterChainImpl createChain() {
        FilterChainImpl chain = filterChainUpdater.getAndSet(this, null);
        if (chain == null) {
            chain = new FilterChainImpl(this);
        }
        return chain;
    }

    protected void recycleFilterChain(FilterChainImpl chain) {
        chain.reset();
        filterChainUpdater.lazySet(this, chain);
    }

    public boolean isUseLocalSessionState() {
        // TODO
        return false;
    }

    public void setUseLocalSessionState(boolean useLocalSessionState) {
        this.useLocalSessionState = useLocalSessionState;
    }

    public boolean isKeepConnectionUnderlyingTransactionIsolation() {
        return keepConnectionUnderlyingTransactionIsolation;
    }

    public void setKeepConnectionUnderlyingTransactionIsolation(boolean keepConnectionUnderlyingTransactionIsolation) {
        // TODO
    }

    public DruidDataSourceStatLogger getStatLogger() {
        return statLogger;
    }

    public void setStatLoggerClassName(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            DruidDataSourceStatLogger statLogger = (DruidDataSourceStatLogger) clazz.newInstance();
            this.setStatLogger(statLogger);
        } catch (Exception e) {
            throw new IllegalArgumentException(className, e);
        }
    }

    public void setStatLogger(DruidDataSourceStatLogger statLogger) {
        this.statLogger = statLogger;
    }

    public long getTimeBetweenLogStatsMillis() {
        // TODO
        return 0L;
    }

    public void setTimeBetweenLogStatsMillis(long timeBetweenLogStatsMillis) {
        // TODO
    }

    public boolean isOracle() {
        // TODO
        return false;
    }

    public void setOracle(boolean isOracle) {
        // TODO
    }

    public boolean isUseUnfairLock() {
        // TODO
        return false;
    }

    public void setUseUnfairLock(boolean useUnfairLock) {
        // TODO
    }

    public boolean isUseOracleImplicitCache() {
        return useOracleImplicitCache;
    }

    public void setUseOracleImplicitCache(boolean useOracleImplicitCache) {
        // TODO
    }

    public Throwable getLastCreateError() {
        // TODO
        return null;
    }

    public Throwable getLastError() {
        // TODO
        return null;
    }

    public long getLastErrorTimeMillis() {
        // TODO
        return 0L;
    }

    public java.util.Date getLastErrorTime() {
        // TODO
        return null;
    }

    public long getLastCreateErrorTimeMillis() {
        // TODO
        return 0L;
    }

    public java.util.Date getLastCreateErrorTime() {
        // TODO
        return null;
    }

    public int getTransactionQueryTimeout() {
        // TODO
        return 0;
    }

    public void setTransactionQueryTimeout(int transactionQueryTimeout) {
        // TODO
    }

    public long getExecuteCount() {
        return executeCount + executeQueryCount + executeUpdateCount + executeBatchCount;
    }

    public long getExecuteUpdateCount() {
        return executeUpdateCount;
    }

    public long getExecuteQueryCount() {
        return executeQueryCount;
    }

    public long getExecuteBatchCount() {
        return executeBatchCount;
    }

    public long getAndResetExecuteCount() {
        return executeCountUpdater.getAndSet(this, 0) + executeQueryCountUpdater.getAndSet(this, 0) + executeUpdateCountUpdater.getAndSet(this, 0) + executeBatchCountUpdater.getAndSet(this, 0);
    }

    public long getExecuteCount2() {
        return executeCount;
    }

    public void incrementExecuteCount() {
        this.executeCountUpdater.incrementAndGet(this);
    }

    public void incrementExecuteUpdateCount() {
        this.executeUpdateCount++;
    }

    public void incrementExecuteQueryCount() {
        this.executeQueryCount++;
    }

    public void incrementExecuteBatchCount() {
        this.executeBatchCount++;
    }

    public boolean isDupCloseLogEnable() {
        // TODO
        return false;
    }

    public void setDupCloseLogEnable(boolean dupCloseLogEnable) {
        // TODO
    }

    public ObjectName getObjectName() {
        // TODO
        return null;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public Histogram getTransactionHistogram() {
        return transactionHistogram;
    }

    public void incrementCachedPreparedStatementCount() {
        cachedPreparedStatementCountUpdater.incrementAndGet(this);
    }

    public void decrementCachedPreparedStatementCount() {
        cachedPreparedStatementCountUpdater.decrementAndGet(this);
    }

    public void incrementCachedPreparedStatementDeleteCount() {
        cachedPreparedStatementDeleteCountUpdater.incrementAndGet(this);
    }

    public void incrementCachedPreparedStatementMissCount() {
        cachedPreparedStatementMissCountUpdater.incrementAndGet(this);
    }

    protected void incrementUserPasswordVersion() {
        userPasswordVersionUpdater.incrementAndGet(this);
    }

    protected long getUserPasswordVersion() {
        return userPasswordVersionUpdater.get(this);
    }

    public long getCachedPreparedStatementMissCount() {
        return cachedPreparedStatementMissCount;
    }

    public long getCachedPreparedStatementAccessCount() {
        // TODO
        return 0L;
    }

    public long getCachedPreparedStatementDeleteCount() {
        return cachedPreparedStatementDeleteCount;
    }

    public long getCachedPreparedStatementCount() {
        // TODO
        return 0L;
    }

    public void incrementClosedPreparedStatementCount() {
        closedPreparedStatementCountUpdater.incrementAndGet(this);
    }

    public long getClosedPreparedStatementCount() {
        // TODO
        return 0L;
    }

    public void incrementPreparedStatementCount() {
        preparedStatementCountUpdater.incrementAndGet(this);
    }

    public long getPreparedStatementCount() {
        // TODO
        return 0L;
    }

    public void incrementCachedPreparedStatementHitCount() {
        cachedPreparedStatementHitCountUpdater.incrementAndGet(this);
    }

    public long getCachedPreparedStatementHitCount() {
        return cachedPreparedStatementHitCount;
    }

    public long getTransactionThresholdMillis() {
        // TODO
        return 0L;
    }

    public void setTransactionThresholdMillis(long transactionThresholdMillis) {
        // TODO
    }

    public abstract void logTransaction(TransactionInfo info);

    public long[] getTransactionHistogramValues() {
        return transactionHistogram.toArray();
    }

    public long[] getTransactionHistogramRanges() {
        // TODO
        return null;
    }

    public long getCommitCount() {
        // TODO
        return 0L;
    }

    public void incrementCommitCount() {
        commitCountUpdater.incrementAndGet(this);
    }

    public long getRollbackCount() {
        // TODO
        return 0L;
    }

    public void incrementRollbackCount() {
        rollbackCountUpdater.incrementAndGet(this);
    }

    public long getStartTransactionCount() {
        // TODO
        return 0L;
    }

    public void incrementStartTransactionCount() {
        startTransactionCountUpdater.incrementAndGet(this);
    }

    public boolean isBreakAfterAcquireFailure() {
        // TODO
        return false;
    }

    public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
        // TODO
    }

    public int getConnectionErrorRetryAttempts() {
        // TODO
        return 0;
    }

    public void setConnectionErrorRetryAttempts(int connectionErrorRetryAttempts) {
        // TODO
    }

    public long getDupCloseCount() {
        // TODO
        return 0L;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        // TODO
        return 0;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        // TODO
    }

    public boolean isSharePreparedStatements() {
        return sharePreparedStatements;
    }

    public void setSharePreparedStatements(boolean sharePreparedStatements) {
        // TODO
    }

    public void incrementDupCloseCount() {
        // TODO
    }

    public ValidConnectionChecker getValidConnectionChecker() {
        // TODO
        return null;
    }

    public void setValidConnectionChecker(ValidConnectionChecker validConnectionChecker) {
        // TODO
    }

    public boolean isUsePingMethod() {
        return usePingMethod;
    }

    public void setUsePingMethod(boolean usePingMethod) {
        this.usePingMethod = usePingMethod;
    }

    public String getValidConnectionCheckerClassName() {
        // TODO
        return null;
    }

    public void setValidConnectionCheckerClassName(String validConnectionCheckerClass) throws Exception {
        // TODO
    }

    public String getDbType() {
        // TODO
        return null;
    }

    public void setDbType(DbType dbType) {
        // TODO
    }

    public void setDbType(String dbType) {
        // TODO
    }

    public void addConnectionProperty(String name, String value) {
        // TODO
    }

    public Collection<String> getConnectionInitSqls() {
        // TODO
        return null;
    }

    public void setConnectionInitSqls(Collection<? extends Object> connectionInitSqls) {
        // TODO
    }

    public long getTimeBetweenConnectErrorMillis() {
        // TODO
        return 0L;
    }

    public void setTimeBetweenConnectErrorMillis(long timeBetweenConnectErrorMillis) {
        // TODO
    }

    public int getMaxOpenPreparedStatements() {
        // TODO
        return 0;
    }

    public void setMaxOpenPreparedStatements(int maxOpenPreparedStatements) {
        // TODO
    }

    public boolean isLogAbandoned() {
        // TODO
        return false;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        // TODO
    }

    public int getRemoveAbandonedTimeout() {
        // TODO
        return 0;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        // TODO
    }

    public void setRemoveAbandonedTimeoutMillis(long removeAbandonedTimeoutMillis) {
        // TODO
    }

    public long getRemoveAbandonedTimeoutMillis() {
        // TODO
        return 0L;
    }

    public boolean isRemoveAbandoned() {
        // TODO
        return false;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        // TODO
    }

    public long getMinEvictableIdleTimeMillis() {
        // TODO
        return 0L;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        // TODO
    }

    public long getKeepAliveBetweenTimeMillis() {
        // TODO
        return 0L;
    }

    public void setKeepAliveBetweenTimeMillis(long keepAliveBetweenTimeMillis) {
        if (keepAliveBetweenTimeMillis < 1000 * 30) {
            LOG.error("keepAliveBetweenTimeMillis should be greater than 30000");
        }
        this.keepAliveBetweenTimeMillis = keepAliveBetweenTimeMillis;
    }

    public long getMaxEvictableIdleTimeMillis() {
        // TODO
        return 0L;
    }

    public void setMaxEvictableIdleTimeMillis(long maxEvictableIdleTimeMillis) {
        // TODO
    }

    public long getPhyTimeoutMillis() {
        // TODO
        return 0L;
    }

    public void setPhyTimeoutMillis(long phyTimeoutMillis) {
        // TODO
    }

    public long getPhyMaxUseCount() {
        // TODO
        return 0L;
    }

    public void setPhyMaxUseCount(long phyMaxUseCount) {
        // TODO
    }

    public int getNumTestsPerEvictionRun() {
        // TODO
        return 0;
    }

    /**
     * @param numTestsPerEvictionRun number of tests per eviction run
     */
    @Deprecated
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        // TODO
    }

    public long getTimeBetweenEvictionRunsMillis() {
        // TODO
        return 0L;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        // TODO
    }

    public int getMaxWaitThreadCount() {
        // TODO
        return 0;
    }

    public void setMaxWaitThreadCount(int maxWaithThreadCount) {
        // TODO
    }

    public String getValidationQuery() {
        // TODO
        return null;
    }

    public void setValidationQuery(String validationQuery) {
        // TODO
    }

    public int getValidationQueryTimeout() {
        // TODO
        return 0;
    }

    public void setValidationQueryTimeout(int validationQueryTimeout) {
        // TODO
    }

    public boolean isAccessToUnderlyingConnectionAllowed() {
        // TODO
        return false;
    }

    public void setAccessToUnderlyingConnectionAllowed(boolean accessToUnderlyingConnectionAllowed) {
        // TODO
    }

    public boolean isTestOnBorrow() {
        // TODO
        return false;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        // TODO
    }

    public boolean isTestOnReturn() {
        // TODO
        return false;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        // TODO
    }

    public boolean isTestWhileIdle() {
        // TODO
        return false;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        // TODO
    }

    public boolean isDefaultAutoCommit() {
        // TODO
        return false;
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        // TODO
    }

    public Boolean getDefaultReadOnly() {
        // TODO
        return null;
    }

    public void setDefaultReadOnly(Boolean defaultReadOnly) {
        // TODO
    }

    public Integer getDefaultTransactionIsolation() {
        // TODO
        return null;
    }

    public void setDefaultTransactionIsolation(Integer defaultTransactionIsolation) {
        // TODO
    }

    public String getDefaultCatalog() {
        // TODO
        return null;
    }

    public void setDefaultCatalog(String defaultCatalog) {
        // TODO
    }

    public PasswordCallback getPasswordCallback() {
        return passwordCallback;
    }

    public void setPasswordCallback(PasswordCallback passwordCallback) {
        // TODO
    }

    public void setPasswordCallbackClassName(String passwordCallbackClassName) throws Exception {
        // TODO
    }

    public NameCallback getUserCallback() {
        return userCallback;
    }

    public void setUserCallback(NameCallback userCallback) {
        // TODO
    }

    public boolean isInitVariants() {
        // TODO
        return false;
    }

    public void setInitVariants(boolean initVariants) {
        // TODO
    }

    public boolean isInitGlobalVariants() {
        // TODO
        return false;
    }

    public void setInitGlobalVariants(boolean initGlobalVariants) {
        // TODO
    }

    /**
     * Retrieves the number of seconds the driver will wait for a <code>Statement</code> object to execute. If the limit
     * is exceeded, a <code>SQLException</code> is thrown.
     *
     * @return the current query timeout limit in seconds; zero means there is no limit
     * <code>Statement</code>
     * @see #setQueryTimeout
     */
    public int getQueryTimeout() {
        // TODO
        return 0;
    }

    /**
     * Sets the number of seconds the driver will wait for a <code>Statement</code> object to execute to the given
     * number of seconds. If the limit is exceeded, an <code>SQLException</code> is thrown. A JDBC driver must apply
     * this limit to the <code>execute</code>, <code>executeQuery</code> and <code>executeUpdate</code> methods. JDBC
     * driver implementations may also apply this limit to <code>ResultSet</code> methods (consult your driver vendor
     * documentation for details).
     *
     * @param seconds the new query timeout limit in seconds; zero means there is no limit
     * @see #getQueryTimeout
     */
    public void setQueryTimeout(int seconds) {
        // TODO
    }

    /**
     * @since 1.2.12
     */
    public int getConnectTimeout() {
        // TODO
        return 0;
    }

    /**
     * @since 1.2.12
     */
    public void setConnectTimeout(int milliSeconds) {
        // TODO
    }

    protected void setConnectTimeout(String milliSeconds) {
        // TODO
    }

    /**
     * @since 1.2.12
     */
    public int getSocketTimeout() {
        // TODO
        return 0;
    }

    /**
     * @since 1.2.12
     */
    public void setSocketTimeout(int milliSeconds) {
        // TODO
    }

    protected void setSocketTimeout(String milliSeconds) {
        // TODO
    }

    public String getName() {
        // TODO
        return null;
    }

    public void setName(String name) {
        // TODO
    }

    public boolean isPoolPreparedStatements() {
        // TODO
        return false;
    }

    public abstract void setPoolPreparedStatements(boolean value);

    public long getMaxWait() {
        // TODO
        return 0L;
    }

    public void setMaxWait(long maxWaitMillis) {
        // TODO
    }

    public int getNotFullTimeoutRetryCount() {
        // TODO
        return 0;
    }

    public void setNotFullTimeoutRetryCount(int notFullTimeoutRetryCount) {
        this.notFullTimeoutRetryCount = notFullTimeoutRetryCount;
    }

    public int getMinIdle() {
        // TODO
        return 0;
    }

    public void setMinIdle(int value) {
        // TODO
    }

    public int getMaxIdle() {
        // TODO
        return 0;
    }

    @Deprecated
    public void setMaxIdle(int maxIdle) {
        // TODO
    }

    public int getInitialSize() {
        // TODO
        return 0;
    }

    public void setInitialSize(int initialSize) {
        // TODO
    }

    public long getCreateErrorCount() {
        // TODO
        return 0L;
    }

    public int getMaxActive() {
        // TODO
        return 0;
    }

    public abstract void setMaxActive(int maxActive);

    public String getUsername() {
        // TODO
        return null;
    }

    public void setUsername(String username) {
        // TODO
    }

    public String getPassword() {
        // TODO
        return null;
    }

    public void setPassword(String password) {
        // TODO
    }

    public Properties getConnectProperties() {
        // TODO
        return null;
    }

    public abstract void setConnectProperties(Properties properties);

    public void setConnectionProperties(String connectionProperties) {
        // TODO
    }

    public String getUrl() {
        // TODO
        return null;
    }

    public String getRawJdbcUrl() {
        return jdbcUrl;
    }

    public void setUrl(String jdbcUrl) {
        // TODO
    }

    public String getDriverClassName() {
        // TODO
        return null;
    }

    public void setDriverClassName(String driverClass) {
        // TODO
    }

    public ClassLoader getDriverClassLoader() {
        return driverClassLoader;
    }

    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }

    @Override
    public PrintWriter getLogWriter() {
        // TODO
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO
    }

    @Override
    public void setLoginTimeout(int seconds) {
        // TODO
    }

    @Override
    public int getLoginTimeout() {
        // TODO
        return 0;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        // TODO
    }

    public int getDriverMajorVersion() {
        // TODO
        return 0;
    }

    public int getDriverMinorVersion() {
        // TODO
        return 0;
    }

    public ExceptionSorter getExceptionSorter() {
        // TODO
        return null;
    }

    public String getExceptionSorterClassName() {
        // TODO
        return null;
    }

    public void setExceptionSorter(ExceptionSorter exceptionSoter) {
        // TODO
    }

    // 兼容JBOSS
    public void setExceptionSorterClassName(String exceptionSorter) throws Exception {
        // TODO
    }

    public void setExceptionSorter(String exceptionSorter) throws SQLException {
        // TODO
    }

    @Override
    public List<Filter> getProxyFilters() {
        // TODO
        return null;
    }

    public void setProxyFilters(List<Filter> filters) {
        // TODO
    }

    public String[] getFilterClasses() {
        List<Filter> filterConfigList = getProxyFilters();
        List<String> classes = new ArrayList<String>();
        for (Filter filter : filterConfigList) {
            classes.add(filter.getClass().getName());
        }
        return classes.toArray(new String[classes.size()]);
    }

    public void setFilters(String filters) throws SQLException {
        // TODO
    }

    public void addFilters(String filters) throws SQLException {
        if (filters == null || filters.length() == 0) {
            return;
        }
        String[] filterArray = filters.split("\\,");
        for (String item : filterArray) {
            FilterManager.loadFilter(this.filters, item.trim());
        }
    }

    public void clearFilters() {
        if (!isClearFiltersEnable()) {
            return;
        }
        this.filters.clear();
    }

    public void validateConnection(Connection conn) throws SQLException {
        // TODO
    }

    /**
     * @deprecated
     */
    protected boolean testConnectionInternal(Connection conn) {
        return testConnectionInternal(null, conn);
    }

    protected boolean testConnectionInternal(DruidConnectionHolder holder, Connection conn) {
        String sqlFile = JdbcSqlStat.getContextSqlFile();
        String sqlName = JdbcSqlStat.getContextSqlName();
        if (sqlFile != null) {
            JdbcSqlStat.setContextSqlFile(null);
        }
        if (sqlName != null) {
            JdbcSqlStat.setContextSqlName(null);
        }
        try {
            if (validConnectionChecker != null) {
                // mysql-connector-java will throw Exception if the connection is broken.
                boolean valid = validConnectionChecker.isValidConnection(conn, validationQuery, validationQueryTimeout);
                long currentTimeMillis = System.currentTimeMillis();
                if (holder != null) {
                    holder.lastValidTimeMillis = currentTimeMillis;
                    holder.lastExecTimeMillis = currentTimeMillis;
                }
                if (conn instanceof ConnectionProxyImpl) {
                    ((ConnectionProxyImpl) conn).setLastValidateTimeMillis(currentTimeMillis);
                }
                if (valid && onFatalError) {
                    lock.lock();
                    try {
                        if (onFatalError) {
                            onFatalError = false;
                        }
                    } finally {
                        lock.unlock();
                    }
                }
                return valid;
            }
            if (conn.isClosed()) {
                return false;
            }
            if (null == validationQuery) {
                return true;
            }
            boolean valid;
            try {
                valid = ValidConnectionCheckerAdapter.execValidQuery(conn, validationQuery, validationQueryTimeout);
            } finally {
                if (conn instanceof ConnectionProxyImpl) {
                    ((ConnectionProxyImpl) conn).setLastValidateTimeMillis(System.currentTimeMillis());
                }
            }
            if (valid && onFatalError) {
                lock.lock();
                try {
                    if (onFatalError) {
                        onFatalError = false;
                    }
                } finally {
                    lock.unlock();
                }
            }
            return valid;
        } catch (Throwable ex) {
            // skip
            return false;
        } finally {
            if (sqlFile != null) {
                JdbcSqlStat.setContextSqlFile(sqlFile);
            }
            if (sqlName != null) {
                JdbcSqlStat.setContextSqlName(sqlName);
            }
        }
    }

    public Set<DruidPooledConnection> getActiveConnections() {
        // TODO
        return null;
    }

    public List<String> getActiveConnectionStackTrace() {
        // TODO
        return null;
    }

    public long getCreateTimespanNano() {
        // TODO
        return 0L;
    }

    public long getCreateTimespanMillis() {
        // TODO
        return 0L;
    }

    @Override
    public Driver getRawDriver() {
        return driver;
    }

    public boolean isClearFiltersEnable() {
        // TODO
        return false;
    }

    public void setClearFiltersEnable(boolean clearFiltersEnable) {
        // TODO
    }

    protected volatile long connectionIdSeed = 10000L;

    protected volatile long statementIdSeed = 20000L;

    protected volatile long resultSetIdSeed = 50000L;

    protected volatile long transactionIdSeed = 60000L;

    protected volatile long metaDataIdSeed = 80000L;

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> connectionIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "connectionIdSeed");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> statementIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "statementIdSeed");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> resultSetIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "resultSetIdSeed");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> transactionIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "transactionIdSeed");

    static final AtomicLongFieldUpdater<DruidAbstractDataSource> metaDataIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "metaDataIdSeed");

    public long createConnectionId() {
        return connectionIdSeedUpdater.incrementAndGet(this);
    }

    public long createStatementId() {
        return statementIdSeedUpdater.getAndIncrement(this);
    }

    public long createMetaDataId() {
        return metaDataIdSeedUpdater.getAndIncrement(this);
    }

    public long createResultSetId() {
        return resultSetIdSeedUpdater.getAndIncrement(this);
    }

    @Override
    public long createTransactionId() {
        return transactionIdSeedUpdater.getAndIncrement(this);
    }

    void initStatement(DruidPooledConnection conn, Statement stmt) throws SQLException {
        boolean transaction = !conn.getConnectionHolder().underlyingAutoCommit;
        int queryTimeout = transaction ? getTransactionQueryTimeout() : getQueryTimeout();
        if (queryTimeout > 0) {
            stmt.setQueryTimeout(queryTimeout);
        }
    }

    public void handleConnectionException(DruidPooledConnection conn, Throwable t) throws SQLException {
        handleConnectionException(conn, t, null);
    }

    public abstract void handleConnectionException(DruidPooledConnection conn, Throwable t, String sql) throws SQLException;

    protected abstract void recycle(DruidPooledConnection pooledConnection) throws SQLException;

    public Connection createPhysicalConnection(String url, Properties info) throws SQLException {
        // TODO
        return null;
    }

    public PhysicalConnectionInfo createPhysicalConnection() throws SQLException {
        // TODO
        return null;
    }

    protected void setCreateError(Throwable ex) {
        // TODO
    }

    public boolean isFailContinuous() {
        // TODO
        return false;
    }

    protected void setFailContinuous(boolean fail) {
        if (fail) {
            failContinuousTimeMillisUpdater.set(this, System.currentTimeMillis());
        } else {
            failContinuousTimeMillisUpdater.set(this, 0L);
        }
        boolean currentState = failContinuousUpdater.get(this) == 1;
        if (currentState == fail) {
            return;
        }
        if (fail) {
            failContinuousUpdater.set(this, 1);
            if (LOG.isInfoEnabled()) {
                LOG.info("{dataSource-" + this.getID() + "} failContinuous is true");
            }
        } else {
            failContinuousUpdater.set(this, 0);
            if (LOG.isInfoEnabled()) {
                LOG.info("{dataSource-" + this.getID() + "} failContinuous is false");
            }
        }
    }

    public void initPhysicalConnection(Connection conn) throws SQLException {
        initPhysicalConnection(conn, null, null);
    }

    public void initPhysicalConnection(Connection conn, Map<String, Object> variables, Map<String, Object> globalVariables) throws SQLException {
        boolean skipAutoCommit = "odps".equals(dbTypeName);
        if ((!skipAutoCommit) && conn.getAutoCommit() != defaultAutoCommit) {
            conn.setAutoCommit(defaultAutoCommit);
        }
        if (defaultReadOnly != null) {
            if (conn.isReadOnly() != defaultReadOnly) {
                conn.setReadOnly(defaultReadOnly);
            }
        }
        if (getDefaultTransactionIsolation() != null) {
            if (conn.getTransactionIsolation() != getDefaultTransactionIsolation().intValue()) {
                conn.setTransactionIsolation(getDefaultTransactionIsolation());
            }
        }
        if (getDefaultCatalog() != null && getDefaultCatalog().length() != 0) {
            conn.setCatalog(getDefaultCatalog());
        }
    }

    private boolean initSqls(Connection conn, Map<String, Object> variables, Map<String, Object> globalVariables) throws SQLException {
        boolean checked = false;
        Collection<String> initSqls = getConnectionInitSqls();
        if (initSqls.isEmpty() && variables == null && globalVariables == null) {
            return checked;
        }
        // using raw connection to skip all filters.
        Connection rawConn;
        if (conn instanceof ConnectionProxyImpl) {
            rawConn = ((ConnectionProxyImpl) conn).getConnectionRaw();
        } else {
            rawConn = conn;
        }
        Statement stmt = ((DruidStatementConnection) rawConn).getStatement();
        for (String sql : initSqls) {
            if (StringUtils.isEmpty(sql)) {
                continue;
            }
            stmt.execute(sql);
            checked = true;
        }
        DbType dbType = DbType.of(this.dbTypeName);
        if (JdbcUtils.isMysqlDbType(dbType)) {
            if (variables != null) {
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery("show variables");
                    while (rs.next()) {
                        String name = rs.getString(1);
                        Object value = rs.getObject(2);
                        variables.put(name, value);
                    }
                } finally {
                    JdbcUtils.close(rs);
                }
                checked = true;
            }
            if (globalVariables != null) {
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery("show global variables");
                    while (rs.next()) {
                        String name = rs.getString(1);
                        Object value = rs.getObject(2);
                        globalVariables.put(name, value);
                    }
                } finally {
                    JdbcUtils.close(rs);
                }
                checked = true;
            }
        }
        return checked;
    }

    public abstract int getActivePeak();

    @Override
    public CompositeDataSupport getCompositeData() throws JMException {
        // TODO
        return null;
    }

    public static void fillStatDataToMap(final JdbcDataSourceStat stat, final Map<String, Object> map) {
        map.put("ConnectionConnectLastTime", stat.getConnectionStat().getConnectLastTime());
        // 10 - 14
        map.put("StatementCreateCount", stat.getStatementStat().getCreateCount());
        map.put("StatementPrepareCount", stat.getStatementStat().getPrepareCount());
        map.put("StatementPreCallCount", stat.getStatementStat().getPrepareCallCount());
        map.put("StatementExecuteCount", stat.getStatementStat().getExecuteCount());
        map.put("StatementRunningCount", stat.getStatementStat().getRunningCount());
        // 15 - 19
        map.put("StatementConcurrentMax", stat.getStatementStat().getConcurrentMax());
        map.put("StatementCloseCount", stat.getStatementStat().getCloseCount());
        map.put("StatementErrorCount", stat.getStatementStat().getErrorCount());
        map.put("StatementLastErrorTime", null);
        map.put("StatementLastErrorMessage", null);
        // 20 - 24
        map.put("StatementLastErrorStackTrace", null);
        map.put("StatementExecuteMillisTotal", stat.getStatementStat().getMillisTotal());
        map.put("StatementExecuteLastTime", stat.getStatementStat().getExecuteLastTime());
        map.put("ConnectionConnectingCount", stat.getConnectionStat().getConnectingCount());
        map.put("ResultSetCloseCount", stat.getResultSetStat().getCloseCount());
        // 25 - 29
        map.put("ResultSetOpenCount", stat.getResultSetStat().getOpenCount());
        map.put("ResultSetOpenningCount", stat.getResultSetStat().getOpeningCount());
        map.put("ResultSetOpenningMax", stat.getResultSetStat().getOpeningMax());
        map.put("ResultSetFetchRowCount", stat.getResultSetStat().getFetchRowCount());
        map.put("ResultSetLastOpenTime", stat.getResultSetStat().getLastOpenTime());
        // 30 - 34
        map.put("ResultSetErrorCount", stat.getResultSetStat().getErrorCount());
        map.put("ResultSetOpenningMillisTotal", stat.getResultSetStat().getAliveMillisTotal());
        map.put("ResultSetLastErrorTime", stat.getResultSetStat().getLastErrorTime());
        map.put("ResultSetLastErrorMessage", null);
        map.put("ResultSetLastErrorStackTrace", null);
        map.put("ConnectionConnectMillisTotal", stat.getConnectionStat().getConnectMillis());
        map.put("ConnectionConnectingCountMax", stat.getConnectionStat().getConnectingMax());
        // 40 - 44
        map.put("ConnectionConnectMillisMax", stat.getConnectionStat().getConnectMillisMax());
        map.put("ConnectionErrorLastTime", stat.getConnectionStat().getErrorLastTime());
        map.put("ConnectionAliveMillisMax", stat.getConnectionConnectAliveMillisMax());
        map.put("ConnectionAliveMillisMin", stat.getConnectionConnectAliveMillisMin());
        map.put("ConnectionHistogram", stat.getConnectionHistogramValues());
        map.put("StatementHistogram", stat.getStatementStat().getHistogramValues());
    }

    public long getID() {
        return this.id;
    }

    @Override
    public long getDataSourceId() {
        return getID();
    }

    public java.util.Date getCreatedTime() {
        return createdTime;
    }

    public abstract int getRawDriverMajorVersion();

    public abstract int getRawDriverMinorVersion();

    public abstract String getProperties();

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO
        return null;
    }

    public void closePreapredStatement(PreparedStatementHolder stmtHolder) {
        if (stmtHolder == null) {
            return;
        }
        closedPreparedStatementCountUpdater.incrementAndGet(this);
        decrementCachedPreparedStatementCount();
        incrementCachedPreparedStatementDeleteCount();
        JdbcUtils.close(stmtHolder.statement);
    }

    protected void cloneTo(DruidAbstractDataSource to) {
        to.defaultAutoCommit = this.defaultAutoCommit;
        to.defaultReadOnly = this.defaultReadOnly;
        to.defaultTransactionIsolation = this.defaultTransactionIsolation;
        to.defaultCatalog = this.defaultCatalog;
        to.name = this.name;
        to.username = this.username;
        to.password = this.password;
        to.jdbcUrl = this.jdbcUrl;
        to.driverClass = this.driverClass;
        to.connectProperties = new Properties();
        to.connectProperties.putAll(this.connectProperties);
        to.passwordCallback = this.passwordCallback;
        to.userCallback = this.userCallback;
        to.initialSize = this.initialSize;
        to.maxActive = this.maxActive;
        to.minIdle = this.minIdle;
        to.maxIdle = this.maxIdle;
        to.maxWait = this.maxWait;
        to.validationQuery = this.validationQuery;
        to.validationQueryTimeout = this.validationQueryTimeout;
        to.testOnBorrow = this.testOnBorrow;
        to.testOnReturn = this.testOnReturn;
        to.testWhileIdle = this.testWhileIdle;
        to.poolPreparedStatements = this.poolPreparedStatements;
        to.sharePreparedStatements = this.sharePreparedStatements;
        to.maxPoolPreparedStatementPerConnectionSize = this.maxPoolPreparedStatementPerConnectionSize;
        to.logWriter = this.logWriter;
        if (this.filters != null) {
            to.filters = new ArrayList<>(this.filters);
        }
        to.exceptionSorter = this.exceptionSorter;
        to.driver = this.driver;
        to.queryTimeout = this.queryTimeout;
        to.transactionQueryTimeout = this.transactionQueryTimeout;
        to.accessToUnderlyingConnectionAllowed = this.accessToUnderlyingConnectionAllowed;
        to.timeBetweenEvictionRunsMillis = this.timeBetweenEvictionRunsMillis;
        to.numTestsPerEvictionRun = this.numTestsPerEvictionRun;
        to.minEvictableIdleTimeMillis = this.minEvictableIdleTimeMillis;
        to.removeAbandoned = this.removeAbandoned;
        to.removeAbandonedTimeoutMillis = this.removeAbandonedTimeoutMillis;
        to.logAbandoned = this.logAbandoned;
        to.maxOpenPreparedStatements = this.maxOpenPreparedStatements;
        if (connectionInitSqls != null) {
            to.connectionInitSqls = new ArrayList<String>(this.connectionInitSqls);
        }
        to.dbTypeName = this.dbTypeName;
        to.timeBetweenConnectErrorMillis = this.timeBetweenConnectErrorMillis;
        to.validConnectionChecker = this.validConnectionChecker;
        to.connectionErrorRetryAttempts = this.connectionErrorRetryAttempts;
        to.breakAfterAcquireFailure = this.breakAfterAcquireFailure;
        to.transactionThresholdMillis = this.transactionThresholdMillis;
        to.dupCloseLogEnable = this.dupCloseLogEnable;
        to.isOracle = this.isOracle;
        to.useOracleImplicitCache = this.useOracleImplicitCache;
        to.asyncCloseConnectionEnable = this.asyncCloseConnectionEnable;
        to.createScheduler = this.createScheduler;
        to.destroyScheduler = this.destroyScheduler;
        to.socketTimeout = this.socketTimeout;
        to.connectTimeout = this.connectTimeout;
        to.socketTimeoutStr = this.socketTimeoutStr;
        to.connectTimeoutStr = this.connectTimeoutStr;
    }

    /**
     * @param realConnection
     * @return true if new connection has been requested during the execution.
     */
    public abstract boolean discardConnection(Connection realConnection);

    public boolean discardConnection(DruidConnectionHolder holder) {
        return discardConnection(holder.getConnection());
    }

    public boolean isAsyncCloseConnectionEnable() {
        // TODO
        return false;
    }

    public void setAsyncCloseConnectionEnable(boolean asyncCloseConnectionEnable) {
        // TODO
    }

    public ScheduledExecutorService getCreateScheduler() {
        return createScheduler;
    }

    public void setCreateScheduler(ScheduledExecutorService createScheduler) {
        // TODO
    }

    public ScheduledExecutorService getDestroyScheduler() {
        return destroyScheduler;
    }

    public void setDestroyScheduler(ScheduledExecutorService destroyScheduler) {
        // TODO
    }

    public boolean isInited() {
        // TODO
        return false;
    }

    public int getMaxCreateTaskCount() {
        return maxCreateTaskCount;
    }

    public void setMaxCreateTaskCount(int maxCreateTaskCount) {
        if (maxCreateTaskCount < 1) {
            throw new IllegalArgumentException();
        }
        this.maxCreateTaskCount = maxCreateTaskCount;
    }

    public boolean isFailFast() {
        // TODO
        return false;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public int getOnFatalErrorMaxActive() {
        return onFatalErrorMaxActive;
    }

    public void setOnFatalErrorMaxActive(int onFatalErrorMaxActive) {
        // TODO
    }

    public boolean isOnFatalError() {
        // TODO
        return false;
    }

    /**
     * @since 1.1.11
     */
    public boolean isInitExceptionThrow() {
        // TODO
        return false;
    }

    /**
     * @since 1.1.11
     */
    public void setInitExceptionThrow(boolean initExceptionThrow) {
        // TODO
    }

    public static class PhysicalConnectionInfo {

        private Connection connection;

        private long connectStartNanos;

        private long connectedNanos;

        private long initedNanos;

        private long validatedNanos;

        private Map<String, Object> vairiables;

        private Map<String, Object> globalVairiables;

        long createTaskId;

        public PhysicalConnectionInfo(Connection connection, long connectStartNanos, long connectedNanos, long initedNanos, long validatedNanos) {
            this(connection, connectStartNanos, connectedNanos, initedNanos, validatedNanos, null, null);
        }

        public PhysicalConnectionInfo(Connection connection, long connectStartNanos, long connectedNanos, long initedNanos, long validatedNanos, Map<String, Object> vairiables, Map<String, Object> globalVairiables) {
            this.connection = connection;
            this.connectStartNanos = connectStartNanos;
            this.connectedNanos = connectedNanos;
            this.initedNanos = initedNanos;
            this.validatedNanos = validatedNanos;
            this.vairiables = vairiables;
            this.globalVairiables = globalVairiables;
        }

        public Connection getPhysicalConnection() {
            return connection;
        }

        public long getConnectStartNanos() {
            return connectStartNanos;
        }

        public long getConnectedNanos() {
            return connectedNanos;
        }

        public long getInitedNanos() {
            return initedNanos;
        }

        public long getValidatedNanos() {
            return validatedNanos;
        }

        public long getConnectNanoSpan() {
            return connectedNanos - connectStartNanos;
        }

        public Map<String, Object> getVairiables() {
            return vairiables;
        }

        public Map<String, Object> getGlobalVairiables() {
            return globalVairiables;
        }
    }

    class SynchronousExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            try {
                command.run();
            } catch (AbstractMethodError error) {
                netTimeoutError = true;
            } catch (Exception ignored) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("failed to execute command " + command);
                }
            }
        }
    }
}
