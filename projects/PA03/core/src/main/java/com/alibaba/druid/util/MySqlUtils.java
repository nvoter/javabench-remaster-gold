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
package com.alibaba.druid.util;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import javax.sql.XAConnection;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class MySqlUtils {

    static Class<?> utilClass;

    static boolean utilClassError;

    static boolean utilClass_isJdbc4;

    static Class<?> class_5_connection;

    static Method method_5_getPinGlobalTxToPhysicalConnection;

    static Class<?> class_5_suspendableXAConnection;

    static Constructor<?> constructor_5_suspendableXAConnection;

    static Class<?> class_5_JDBC4SuspendableXAConnection;

    static Constructor<?> constructor_5_JDBC4SuspendableXAConnection;

    static Class<?> class_5_MysqlXAConnection;

    static Constructor<?> constructor_5_MysqlXAConnection;

    static Class<?> class_ConnectionImpl;

    static Method method_getId;

    static boolean method_getId_error;

    static Class<?> class_6_ConnectionImpl;

    static Method method_6_getId;

    static volatile Class<?> class_6_connection;

    static volatile Method method_6_getPropertySet;

    static volatile Method method_6_getBooleanReadableProperty;

    static volatile Method method_6_getValue;

    static volatile boolean method_6_getValue_error;

    static volatile Class<?> class_6_suspendableXAConnection;

    static volatile Method method_6_getInstance;

    static volatile boolean method_6_getInstance_error;

    static volatile Method method_6_getInstanceXA;

    static volatile boolean method_6_getInstanceXA_error;

    static volatile Class<?> class_6_JDBC4SuspendableXAConnection;

    public static XAConnection createXAConnection(Driver driver, Connection physicalConn) throws SQLException {
        // TODO
        return null;
    }

    public static String buildKillQuerySql(Connection connection, SQLException error) throws SQLException {
        Long threadId = getId(connection);
        if (threadId == null) {
            return null;
        }
        return "KILL QUERY " + threadId;
    }

    private static Set<String> keywords;

    public static boolean isKeyword(String name) {
        if (name == null) {
            return false;
        }
        String name_lower = name.toLowerCase();
        Set<String> words = keywords;
        if (words == null) {
            words = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/mysql/keywords", words);
            keywords = words;
        }
        return words.contains(name_lower);
    }

    private static Set<String> builtinDataTypes;

    public static boolean isBuiltinDataType(String dataType) {
        // TODO
        return false;
    }

    public static List<String> showTables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("show tables");
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    public static List<String> getTableDDL(Connection conn, List<String> tables) throws SQLException {
        List<String> ddlList = new ArrayList<String>();
        Statement stmt = null;
        try {
            for (String table : tables) {
                if (stmt == null) {
                    stmt = conn.createStatement();
                }
                if (isKeyword(table)) {
                    table = "`" + table + "`";
                }
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery("show create table " + table);
                    if (rs.next()) {
                        String ddl = rs.getString(2);
                        ddlList.add(ddl);
                    }
                } finally {
                    JdbcUtils.close(rs);
                }
            }
        } finally {
            JdbcUtils.close(stmt);
        }
        return ddlList;
    }

    public static String getCreateTableScript(Connection conn) throws SQLException {
        return getCreateTableScript(conn, true, true);
    }

    public static String getCreateTableScript(Connection conn, boolean sorted, boolean simplify) throws SQLException {
        List<String> tables = showTables(conn);
        List<String> ddlList = getTableDDL(conn, tables);
        StringBuilder buf = new StringBuilder();
        for (String ddl : ddlList) {
            buf.append(ddl);
            buf.append(';');
        }
        String ddlScript = buf.toString();
        if (!(sorted || simplify)) {
            return ddlScript;
        }
        List stmtList = SQLUtils.parseStatements(ddlScript, DbType.mysql);
        if (simplify) {
            for (Object o : stmtList) {
                if (o instanceof SQLCreateTableStatement) {
                    SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement) o;
                    createTableStmt.simplify();
                }
            }
        }
        if (sorted) {
            SQLCreateTableStatement.sort(stmtList);
        }
        return SQLUtils.toSQLString(stmtList, DbType.mysql);
    }

    private static transient Class class_connectionImpl;

    private static transient boolean class_connectionImpl_Error;

    private static transient Method method_getIO;

    private static transient boolean method_getIO_error;

    private static transient Class class_MysqlIO;

    private static transient boolean class_MysqlIO_Error;

    private static transient Method method_getLastPacketReceivedTimeMs;

    private static transient boolean method_getLastPacketReceivedTimeMs_error;

    private static volatile boolean mysqlJdbcVersion6;

    private static transient Class classJdbc;

    private static transient Method getIdleFor;

    private static transient boolean getIdleForError;

    public static Long getId(Object conn) {
        if (conn == null) {
            return null;
        }
        Class<?> clazz = conn.getClass();
        if (class_ConnectionImpl == null) {
            if (clazz.getName().equals("com.mysql.jdbc.ConnectionImpl")) {
                class_ConnectionImpl = clazz;
            } else if (clazz.getName().equals("com.mysql.jdbc.Connection")) {
                // mysql 5.0.x
                class_ConnectionImpl = clazz;
            } else if (clazz.getName().equals("com.mysql.cj.jdbc.ConnectionImpl")) {
                // mysql 5.0.x
                class_ConnectionImpl = clazz;
            } else if (clazz.getSuperclass().getName().equals("com.mysql.jdbc.ConnectionImpl")) {
                class_ConnectionImpl = clazz.getSuperclass();
            }
        }
        if (class_ConnectionImpl == clazz || class_ConnectionImpl == clazz.getSuperclass()) {
            try {
                if (method_getId == null && !method_getId_error) {
                    Method method = class_ConnectionImpl.getDeclaredMethod("getId");
                    method.setAccessible(true);
                    method_getId = method;
                }
                return (Long) method_getId.invoke(conn);
            } catch (Throwable ex) {
                method_getId_error = true;
            }
        }
        return null;
    }

    /**
     * The <b>lastQueryFinishedTime</b>, returned from mysql8 <b>com.mysql.cj.jdbc.JdbcConnection#getIdleFor</b>,
     * does not be updated by method <b>pingInternal</b> running for connection validation if option <b>usePingMethod</b>
     * is true.<br>
     * More troublesome is that <b>lastQueryFinishedTime</b> will not be set if option <b>maintainTimeStats</b>
     * is false, so does mysql5 <b>lastPacketReceivedTimeMs</b>.
     */
    @Deprecated
    public static long getLastPacketReceivedTimeMs(Connection conn) throws SQLException {
        // TODO
        return 0L;
    }

    static Class<?> class_5_CommunicationsException;

    static Class<?> class_6_CommunicationsException;

    public static Class getCommunicationsExceptionClass() {
        // TODO
        return null;
    }

    public static final Charset GBK = Charset.forName("GBK");

    public static final Charset BIG5 = Charset.forName("BIG5");

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static final Charset UTF16 = Charset.forName("UTF-16");

    public static final Charset UTF32 = Charset.forName("UTF-32");

    public static final Charset ASCII = Charset.forName("ASCII");

    public static void loadDataTypes(Set<String> dataTypes) {
        Utils.loadFromFile("META-INF/druid/parser/mysql/builtin_datatypes", dataTypes);
    }

    private static BigInteger[] MAX_INT = { new BigInteger("9"), new BigInteger("99"), new BigInteger("999"), new BigInteger("9999"), new BigInteger("99999"), new BigInteger("999999"), new BigInteger("9999999"), new BigInteger("99999999"), new BigInteger("999999999"), new BigInteger("9999999999"), new BigInteger("99999999999"), new BigInteger("999999999999"), new BigInteger("9999999999999"), new BigInteger("99999999999999"), new BigInteger("999999999999999"), new BigInteger("9999999999999999"), new BigInteger("99999999999999999"), new BigInteger("999999999999999999"), new BigInteger("9999999999999999999"), new BigInteger("99999999999999999999"), new BigInteger("999999999999999999999"), new BigInteger("9999999999999999999999"), new BigInteger("99999999999999999999999"), new BigInteger("999999999999999999999999"), new BigInteger("9999999999999999999999999"), new BigInteger("99999999999999999999999999"), new BigInteger("999999999999999999999999999"), new BigInteger("9999999999999999999999999999"), new BigInteger("99999999999999999999999999999"), new BigInteger("999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999"), new BigInteger("99999999999999999999999999999999"), new BigInteger("999999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999"), new BigInteger("99999999999999999999999999999999999"), new BigInteger("999999999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999999"), new BigInteger("99999999999999999999999999999999999999") };

    private static BigInteger[] MIN_INT = { new BigInteger("-9"), new BigInteger("-99"), new BigInteger("-999"), new BigInteger("-9999"), new BigInteger("-99999"), new BigInteger("-999999"), new BigInteger("-9999999"), new BigInteger("-99999999"), new BigInteger("-999999999"), new BigInteger("-9999999999"), new BigInteger("-99999999999"), new BigInteger("-999999999999"), new BigInteger("-9999999999999"), new BigInteger("-99999999999999"), new BigInteger("-999999999999999"), new BigInteger("-9999999999999999"), new BigInteger("-99999999999999999"), new BigInteger("-999999999999999999"), new BigInteger("-9999999999999999999"), new BigInteger("-99999999999999999999"), new BigInteger("-999999999999999999999"), new BigInteger("-9999999999999999999999"), new BigInteger("-99999999999999999999999"), new BigInteger("-999999999999999999999999"), new BigInteger("-9999999999999999999999999"), new BigInteger("-99999999999999999999999999"), new BigInteger("-999999999999999999999999999"), new BigInteger("-9999999999999999999999999999"), new BigInteger("-99999999999999999999999999999"), new BigInteger("-999999999999999999999999999999"), new BigInteger("-9999999999999999999999999999999"), new BigInteger("-99999999999999999999999999999999"), new BigInteger("-999999999999999999999999999999999"), new BigInteger("-9999999999999999999999999999999999"), new BigInteger("-99999999999999999999999999999999999"), new BigInteger("-999999999999999999999999999999999999"), new BigInteger("-9999999999999999999999999999999999999"), new BigInteger("-99999999999999999999999999999999999999") };

    private static BigDecimal[] MAX_DEC_1 = { new BigDecimal("0.9"), new BigDecimal("9.9"), new BigDecimal("99.9"), new BigDecimal("999.9"), new BigDecimal("9999.9"), new BigDecimal("99999.9"), new BigDecimal("999999.9"), new BigDecimal("9999999.9"), new BigDecimal("99999999.9"), new BigDecimal("999999999.9"), new BigDecimal("9999999999.9"), new BigDecimal("99999999999.9"), new BigDecimal("999999999999.9"), new BigDecimal("9999999999999.9"), new BigDecimal("99999999999999.9"), new BigDecimal("999999999999999.9"), new BigDecimal("9999999999999999.9"), new BigDecimal("99999999999999999.9"), new BigDecimal("999999999999999999.9"), new BigDecimal("9999999999999999999.9"), new BigDecimal("99999999999999999999.9"), new BigDecimal("999999999999999999999.9"), new BigDecimal("9999999999999999999999.9"), new BigDecimal("99999999999999999999999.9"), new BigDecimal("999999999999999999999999.9"), new BigDecimal("9999999999999999999999999.9"), new BigDecimal("99999999999999999999999999.9"), new BigDecimal("999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999.9"), new BigDecimal("99999999999999999999999999999.9"), new BigDecimal("999999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999999.9"), new BigDecimal("99999999999999999999999999999999.9"), new BigDecimal("999999999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999999999.9"), new BigDecimal("99999999999999999999999999999999999.9"), new BigDecimal("999999999999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999999999999.9") };

    private static BigDecimal[] MIN_DEC_1 = { new BigDecimal("-0.9"), new BigDecimal("-9.9"), new BigDecimal("-99.9"), new BigDecimal("-999.9"), new BigDecimal("-9999.9"), new BigDecimal("-99999.9"), new BigDecimal("-999999.9"), new BigDecimal("-9999999.9"), new BigDecimal("-99999999.9"), new BigDecimal("-999999999.9"), new BigDecimal("-9999999999.9"), new BigDecimal("-99999999999.9"), new BigDecimal("-999999999999.9"), new BigDecimal("-9999999999999.9"), new BigDecimal("-99999999999999.9"), new BigDecimal("-999999999999999.9"), new BigDecimal("-9999999999999999.9"), new BigDecimal("-99999999999999999.9"), new BigDecimal("-999999999999999999.9"), new BigDecimal("-9999999999999999999.9"), new BigDecimal("-99999999999999999999.9"), new BigDecimal("-999999999999999999999.9"), new BigDecimal("-9999999999999999999999.9"), new BigDecimal("-99999999999999999999999.9"), new BigDecimal("-999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999999999999.9") };

    private static BigDecimal[] MAX_DEC_2 = { new BigDecimal("0.99"), new BigDecimal("9.99"), new BigDecimal("99.99"), new BigDecimal("999.99"), new BigDecimal("9999.99"), new BigDecimal("99999.99"), new BigDecimal("999999.99"), new BigDecimal("9999999.99"), new BigDecimal("99999999.99"), new BigDecimal("999999999.99"), new BigDecimal("9999999999.99"), new BigDecimal("99999999999.99"), new BigDecimal("999999999999.99"), new BigDecimal("9999999999999.99"), new BigDecimal("99999999999999.99"), new BigDecimal("999999999999999.99"), new BigDecimal("9999999999999999.99"), new BigDecimal("99999999999999999.99"), new BigDecimal("999999999999999999.99"), new BigDecimal("9999999999999999999.99"), new BigDecimal("99999999999999999999.99"), new BigDecimal("999999999999999999999.99"), new BigDecimal("9999999999999999999999.99"), new BigDecimal("99999999999999999999999.99"), new BigDecimal("999999999999999999999999.99"), new BigDecimal("9999999999999999999999999.99"), new BigDecimal("99999999999999999999999999.99"), new BigDecimal("999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999.99"), new BigDecimal("99999999999999999999999999999.99"), new BigDecimal("999999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999999.99"), new BigDecimal("99999999999999999999999999999999.99"), new BigDecimal("999999999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999999999.99"), new BigDecimal("99999999999999999999999999999999999.99"), new BigDecimal("999999999999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999999999999.99") };

    private static BigDecimal[] MIN_DEC_2 = { new BigDecimal("-0.99"), new BigDecimal("-9.99"), new BigDecimal("-99.99"), new BigDecimal("-999.99"), new BigDecimal("-9999.99"), new BigDecimal("-99999.99"), new BigDecimal("-999999.99"), new BigDecimal("-9999999.99"), new BigDecimal("-99999999.99"), new BigDecimal("-999999999.99"), new BigDecimal("-9999999999.99"), new BigDecimal("-99999999999.99"), new BigDecimal("-999999999999.99"), new BigDecimal("-9999999999999.99"), new BigDecimal("-99999999999999.99"), new BigDecimal("-999999999999999.99"), new BigDecimal("-9999999999999999.99"), new BigDecimal("-99999999999999999.99"), new BigDecimal("-999999999999999999.99"), new BigDecimal("-9999999999999999999.99"), new BigDecimal("-99999999999999999999.99"), new BigDecimal("-999999999999999999999.99"), new BigDecimal("-9999999999999999999999.99"), new BigDecimal("-99999999999999999999999.99"), new BigDecimal("-999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999999999999.99") };

    public static BigDecimal decimal(BigDecimal value, int precision, int scale) {
        int v_scale = value.scale();
        int v_precision;
        if (v_scale > scale) {
            value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
            v_precision = value.precision();
        } else {
            v_precision = value.precision();
        }
        int v_ints = v_precision - v_scale;
        int ints = precision - scale;
        if (v_precision > precision || v_ints > ints) {
            boolean sign = value.signum() > 0;
            if (scale == 1) {
                return sign ? MAX_DEC_1[ints] : MIN_DEC_1[ints];
            }
            if (scale == 2) {
                return sign ? MAX_DEC_2[ints] : MIN_DEC_2[ints];
            }
            return new BigDecimal(sign ? MAX_INT[precision - 1] : MIN_INT[precision - 1], scale);
        }
        return value;
    }

    public static boolean isNumber(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        char c0 = str.charAt(0);
        boolean dot = false, expr = false;
        int i = 0;
        if (c0 == '+' || c0 == '-') {
            i++;
        }
        for (; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '.') {
                if (dot || expr) {
                    return false;
                } else {
                    dot = true;
                    continue;
                }
            }
            if (ch == 'e' || ch == 'E') {
                if (expr) {
                    return false;
                } else {
                    expr = true;
                }
                if (i < str.length() - 1) {
                    char next = str.charAt(i + 1);
                    if (next == '+' || next == '-') {
                        i++;
                    }
                    continue;
                } else {
                    return false;
                }
            }
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    public static DateFormat toJavaFormat(String fmt, TimeZone timeZone) {
        DateFormat dateFormat = toJavaFormat(fmt);
        if (dateFormat == null) {
            return null;
        }
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat;
    }

    public static DateFormat toJavaFormat(String fmt) {
        if (fmt == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0, len = fmt.length(); i < len; ++i) {
            char ch = fmt.charAt(i);
            if (ch == '%') {
                if (i + 1 == len) {
                    return null;
                }
                char next_ch = fmt.charAt(++i);
                switch(next_ch) {
                    case 'a':
                        buf.append("EEE");
                        break;
                    case 'b':
                        buf.append("MMM");
                        break;
                    case 'c':
                        buf.append("M");
                        break;
                    case 'd':
                        buf.append("dd");
                        break;
                    case 'e':
                        buf.append("d");
                        break;
                    case 'f':
                        buf.append("SSS000");
                        break;
                    case 'H':
                    case 'k':
                        buf.append("HH");
                        break;
                    case 'h':
                    case 'l':
                    case 'I':
                        buf.append("hh");
                        break;
                    case 'i':
                        buf.append("mm");
                        break;
                    case 'M':
                        buf.append("MMMMM");
                        break;
                    case 'm':
                        buf.append("MM");
                        break;
                    case 'p':
                        buf.append('a');
                        break;
                    case 'r':
                        buf.append("hh:mm:ss a");
                        break;
                    case 's':
                    case 'S':
                        buf.append("ss");
                        break;
                    case 'T':
                        buf.append("HH:mm:ss");
                        break;
                    case 'W':
                        buf.append("EEEEE");
                        break;
                    case 'w':
                        buf.append("u");
                        break;
                    case 'Y':
                        buf.append("yyyy");
                        break;
                    case 'y':
                        buf.append("yy");
                        break;
                    default:
                        return null;
                }
            } else {
                buf.append(ch);
            }
        }
        try {
            return new SimpleDateFormat(buf.toString(), Locale.ENGLISH);
        } catch (IllegalArgumentException ex) {
            // skip
            return null;
        }
    }

    public static java.util.Date parseDate(String str, TimeZone timeZone) {
        if (str == null) {
            return null;
        }
        final int length = str.length();
        if (length < 8) {
            return null;
        }
        ZoneId zoneId = timeZone == null ? ZoneId.systemDefault() : timeZone.toZoneId();
        char y0 = str.charAt(0);
        char y1 = str.charAt(1);
        char y2 = str.charAt(2);
        char y3 = str.charAt(3);
        char M0 = 0, M1 = 0, d0 = 0, d1 = 0;
        char h0 = 0, h1 = 0, m0 = 0, m1 = 0, s0 = 0, s1 = 0, S0 = '0', S1 = '0', S2 = '0';
        final char c4 = str.charAt(4);
        final char c5 = str.charAt(5);
        final char c6 = str.charAt(6);
        final char c7 = str.charAt(7);
        char c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18;
        int nanos = 0;
        switch(length) {
            case 8:
                // yyyyMMdd
                if (c4 == '-' && c6 == '-') {
                    M0 = '0';
                    M1 = c5;
                    d0 = '0';
                    d1 = c7;
                } else if (y2 == ':' && c5 == ':') {
                    h0 = y0;
                    h1 = y1;
                    m0 = y3;
                    m1 = c4;
                    s0 = c6;
                    s1 = c7;
                    y0 = '1';
                    y1 = '9';
                    y2 = '7';
                    y3 = '0';
                    M0 = '0';
                    M1 = '1';
                    d0 = '0';
                    d1 = '1';
                } else {
                    M0 = c4;
                    M1 = c5;
                    d0 = c6;
                    d1 = c7;
                }
                break;
            case 9:
                // yyyy-M-dd or yyyy-MM-d
                c8 = str.charAt(8);
                if (c4 != '-') {
                    return null;
                }
                if (c6 == '-') {
                    M0 = '0';
                    M1 = c5;
                    d0 = c7;
                    d1 = c8;
                } else if (c7 == '-') {
                    M0 = c5;
                    M1 = c6;
                    d0 = '0';
                    d1 = c8;
                } else {
                    return null;
                }
                break;
            case 10:
                c8 = str.charAt(8);
                c9 = str.charAt(9);
                // yyyy-MM-dd
                if (c4 != '-' || c7 != '-') {
                    return null;
                }
                M0 = c5;
                M1 = c6;
                d0 = c8;
                d1 = c9;
                break;
            case 14:
                c8 = str.charAt(8);
                c9 = str.charAt(9);
                c10 = str.charAt(10);
                c11 = str.charAt(11);
                c12 = str.charAt(12);
                c13 = str.charAt(13);
                if (c8 == ' ') {
                    // yyyy-M-d H:m:s
                    if (c4 == '-' && c6 == '-' & c10 == ':' && c12 == ':') {
                        M0 = '0';
                        M1 = c5;
                        d0 = '0';
                        d1 = c7;
                        h0 = '0';
                        h1 = c9;
                        m0 = '0';
                        m1 = c11;
                        s0 = '0';
                        s1 = c13;
                    } else {
                        return null;
                    }
                } else {
                    // yyyyMMddHHmmss
                    M0 = c4;
                    M1 = c5;
                    d0 = c6;
                    d1 = c7;
                    h0 = c8;
                    h1 = c9;
                    m0 = c10;
                    m1 = c11;
                    s0 = c12;
                    s1 = c13;
                }
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 29:
                if (length == 19 || length == 23 || length == 29) {
                    c8 = str.charAt(8);
                    c9 = str.charAt(9);
                    c10 = str.charAt(10);
                    c11 = str.charAt(11);
                    c12 = str.charAt(12);
                    c13 = str.charAt(13);
                    c14 = str.charAt(14);
                    c15 = str.charAt(15);
                    c16 = str.charAt(16);
                    c17 = str.charAt(17);
                    c18 = str.charAt(18);
                    // yyyy-MM-dd HH:mm:ss
                    // yyyy-MM-dd HH.mm.ss
                    if (c4 == '-' && c7 == '-' && (c10 == ' ' || c10 == 'T') && ((c13 == ':' && c16 == ':') || (c13 == '.' && c16 == '.'))) {
                        M0 = c5;
                        M1 = c6;
                        d0 = c8;
                        d1 = c9;
                        h0 = c11;
                        h1 = c12;
                        m0 = c14;
                        m1 = c15;
                        s0 = c17;
                        s1 = c18;
                        // yyyy-MM-dd HH:mm:ss.SSS
                        if (length == 23) {
                            final char c19 = str.charAt(19);
                            final char c20 = str.charAt(20);
                            final char c21 = str.charAt(21);
                            final char c22 = str.charAt(22);
                            if (c19 == '.') {
                                S0 = c20;
                                S1 = c21;
                                S2 = c22;
                            } else if (c19 == ' ' && c20 == 'U' && c21 == 'T' && c22 == 'C') {
                                // skip
                                zoneId = ZoneOffset.UTC;
                            } else {
                                return null;
                            }
                        } else if (length == 29) {
                            final char c19 = str.charAt(19);
                            final char c20 = str.charAt(20);
                            final char c21 = str.charAt(21);
                            final char c22 = str.charAt(22);
                            if (c19 == '.') {
                                S0 = c20;
                                S1 = c21;
                                S2 = c22;
                            } else {
                                return null;
                            }
                            final char c23 = str.charAt(23);
                            final char c24 = str.charAt(24);
                            final char c25 = str.charAt(25);
                            final char c26 = str.charAt(26);
                            final char c27 = str.charAt(27);
                            final char c28 = str.charAt(28);
                            if (c23 < '0' || c23 > '9' || c24 < '0' || c24 > '9' || c25 < '0' || c25 > '9' || c26 < '0' || c26 > '9' || c27 < '0' || c27 > '9' || c28 < '0' || c28 > '9') {
                                return null;
                            }
                            nanos = (c23 - '0') * 100000 + (c24 - '0') * 10000 + (c25 - '0') * 1000 + (c26 - '0') * 100 + (c27 - '0') * 10 + (c28 - '0');
                        }
                        break;
                    }
                }
                if (c4 != '-') {
                    return null;
                }
                int offset;
                if (c6 == '-') {
                    M0 = '0';
                    M1 = c5;
                    offset = 7;
                } else if (c7 == '-') {
                    M0 = c5;
                    M1 = c6;
                    offset = 8;
                } else {
                    return null;
                }
                {
                    char n0 = str.charAt(offset);
                    char n1, n2;
                    if ((n1 = str.charAt(offset + 1)) == ' ' || n1 == 'T') {
                        d0 = '0';
                        d1 = n0;
                        offset += 2;
                    } else if ((n2 = str.charAt(offset + 2)) == ' ' || n2 == 'T') {
                        d0 = n0;
                        d1 = n1;
                        offset += 3;
                    } else {
                        return null;
                    }
                }
                {
                    char n0 = str.charAt(offset);
                    char n1, n2;
                    if ((n1 = str.charAt(offset + 1)) == ':') {
                        h0 = '0';
                        h1 = n0;
                        offset += 2;
                    } else if ((n2 = str.charAt(offset + 2)) == ':') {
                        h0 = n0;
                        h1 = n1;
                        offset += 3;
                    } else {
                        return null;
                    }
                }
                {
                    char n0 = str.charAt(offset);
                    char n1, n2;
                    if ((n1 = str.charAt(offset + 1)) == ':') {
                        m0 = '0';
                        m1 = n0;
                        offset += 2;
                    } else if (offset + 2 < length && (n2 = str.charAt(offset + 2)) == ':') {
                        m0 = n0;
                        m1 = n1;
                        offset += 3;
                    } else {
                        return null;
                    }
                }
                if (offset == length - 1) {
                    s0 = '0';
                    s1 = str.charAt(offset);
                } else if (offset == length - 2) {
                    char n0 = str.charAt(offset);
                    char n1 = str.charAt(offset + 1);
                    if (n1 == '.') {
                        s0 = '0';
                        s1 = n0;
                    } else {
                        s0 = n0;
                        s1 = n1;
                    }
                } else {
                    char x0 = str.charAt(length - 1);
                    char x1 = str.charAt(length - 2);
                    char x2 = str.charAt(length - 3);
                    char x3 = str.charAt(length - 4);
                    int lastOff;
                    if (x0 == '.') {
                        // skip
                        lastOff = length - 2;
                    } else if (x1 == '.') {
                        S2 = x0;
                        lastOff = length - 3;
                    } else if (x2 == '.') {
                        S1 = x1;
                        S2 = x0;
                        lastOff = length - 4;
                    } else if (x3 == '.') {
                        S0 = x2;
                        S1 = x1;
                        S2 = x0;
                        lastOff = length - 5;
                    } else if ((x2 == '+' || x2 == '-') && length == offset + 5) {
                        String zoneIdStr = new String(new char[] { x2, x1, x0 });
                        zoneId = ZoneId.of(zoneIdStr);
                        lastOff = length - 4;
                    } else {
                        return null;
                    }
                    char k0 = str.charAt(lastOff);
                    char k1 = str.charAt(lastOff - 1);
                    char k2 = str.charAt(lastOff - 2);
                    if (k1 == ':') {
                        s0 = '0';
                        s1 = k0;
                    } else if (k2 == ':') {
                        s1 = k0;
                        s0 = k1;
                    } else {
                        return null;
                    }
                }
                break;
            default:
                return null;
        }
        if (y0 < '0' || y0 > '9' || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return null;
        }
        int year = (y0 - '0') * 1000 + (y1 - '0') * 100 + (y2 - '0') * 10 + (y3 - '0');
        if (year < 1970) {
            return null;
        }
        if (M0 < '0' || M0 > '1') {
            return null;
        }
        if (M1 < '0' || M1 > '9') {
            return null;
        }
        int month = (M0 - '0') * 10 + (M1 - '0');
        if (month < 1 || month > 12) {
            return null;
        }
        if (d0 < '0' || d0 > '9') {
            return null;
        }
        if (d1 < '0' || d1 > '9') {
            return null;
        }
        int dayOfMonth = (d0 - '0') * 10 + (d1 - '0');
        if (dayOfMonth < 1) {
            return null;
        }
        final int maxDayOfMonth;
        switch(month) {
            case 2:
                maxDayOfMonth = 29;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxDayOfMonth = 30;
                break;
            default:
                maxDayOfMonth = 31;
                break;
        }
        if (dayOfMonth > maxDayOfMonth) {
            return null;
        }
        ZonedDateTime zdt;
        if (h0 == 0) {
            zdt = LocalDate.of(year, month, dayOfMonth).atStartOfDay(zoneId);
        } else {
            int hour = (h0 - '0') * 10 + (h1 - '0');
            int minute = (m0 - '0') * 10 + (m1 - '0');
            int second = (s0 - '0') * 10 + (s1 - '0');
            int nanoSecond = ((S0 - '0') * 100 + (S1 - '0') * 10 + (S2 - '0')) * 1000000 + nanos;
            if (hour >= 24 || minute > 60 || second > 61) {
                return null;
            }
            zdt = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoSecond).atZone(zoneId);
        }
        return java.util.Date.from(zdt.toInstant());
    }

    public static long parseMillis(byte[] str, TimeZone timeZone) {
        // TODO
        return 0L;
    }

    public static long parseMillis(final byte[] str, final int off, final int len, final TimeZone timeZone) {
        // TODO
        return 0L;
    }

    public static ZonedDateTime parseDateTime(final byte[] str, final int off, final int len, ZoneId zoneId) {
        // TODO
        return null;
    }

    private static final String[] parseFormats = new String[] { "HH:mm:ss", "yyyyMMdd", "yyyyMMddHHmmss", "yyyy-M-d", "yyyy-M-d H:m:s", "yyyy-M-d H:m:s.S", "yyyy-M-d'T'H:m:s", "yyyy-M-d'T'H:m:s.S", "yyyy-MM-d", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS" };

    private static final long[] parseFormatCodes;

    static {
        long[] codes = new long[parseFormats.length];
        for (int i = 0; i < parseFormats.length; i++) {
            codes[i] = FnvHash.fnv1a_64(parseFormats[i]);
        }
        Arrays.sort(codes);
        parseFormatCodes = codes;
    }

    public static boolean isSupportParseDateformat(String str) {
        if (str == null) {
            return false;
        }
        return Arrays.binarySearch(parseFormatCodes, FnvHash.fnv1a_64(str)) >= 0;
    }

    public static TimeZone parseTimeZone(String str) {
        if ("SYSTEM".equalsIgnoreCase(str)) {
            return TimeZone.getDefault();
        }
        return TimeZone.getTimeZone(str);
    }

    public static String utf32(String hex) {
        byte[] bytes = HexBin.decode(hex);
        if (bytes.length == 2) {
            return new String(bytes, UTF16);
        }
        return new String(bytes, UTF32);
    }

    public static String utf16(String hex) {
        if (hex.length() % 2 == 1) {
            char[] chars = new char[hex.length() + 1];
            chars[0] = '0';
            hex.getChars(0, hex.length(), chars, 1);
            hex = new String(chars);
        }
        byte[] bytes = HexBin.decode(hex);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, UTF16);
    }

    public static String utf8(String hex) {
        byte[] bytes = HexBin.decode(hex);
        return new String(bytes, UTF8);
    }

    public static String gbk(String hex) {
        byte[] bytes = HexBin.decode(hex);
        return new String(bytes, GBK);
    }

    public static String big5(String hex) {
        byte[] bytes = HexBin.decode(hex);
        return new String(bytes, BIG5);
    }
}
