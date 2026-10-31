package com.alibaba.druid.pool.vendor;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.Set;

public class OceanBaseOracleExceptionSorter extends AbstractOracleExceptionSorter implements Serializable {

    private static final long serialVersionUID = -9146226891418913175L;

    public OceanBaseOracleExceptionSorter() {
        // TODO
    }

    public Set<Integer> getFatalErrorCodes() {
        return fatalErrorCodes;
    }

    public void setFatalErrorCodes(Set<Integer> fatalErrorCodes) {
        this.fatalErrorCodes = fatalErrorCodes;
    }

    @Override
    public boolean isExceptionFatal(SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        final String sqlState = e.getSQLState();
        // I can't remember if the errors are negative or positive.
        final int error_code = Math.abs(e.getErrorCode());
        if (sqlState != null && sqlState.startsWith("08")) {
            return true;
        }
        switch(error_code) {
            // your session has been killed
            case 28:
            // Internal oracle error
            case 600:
            // not logged on
            case 1012:
            // Oracle shutdown in progress
            case 1014:
            // Oracle initialization or shutdown in progress
            case 1033:
            // Oracle not available
            case 1034:
            // ORACLE only available to users with RESTRICTED SESSION privilege
            case 1035:
            // immediate shutdown in progress - no operations are permitted
            case 1089:
            // shutdown in progress - connection is not permitted
            case 1090:
            // ORACLE instance terminated. Disconnection forced
            case 1092:
            // ALTER DATABASE CLOSE in progress. Connections not permitted
            case 1094:
            // exceeded maximum idle time, please connect again
            case 2396:
            // fatal two-task communication protocol error
            case 3106:
            // break received on communication channel
            case 3111:
            // end-of-file on communication channel
            case 3113:
            // not connected to ORACLE
            case 3114:
            // Connections to this server version are no longer supported.
            case 3134:
            // connection lost contact
            case 3135:
            // inbound connection timed out
            case 3136:
            // Connection terminated due to security policy violation
            case 3138:
            // Connection was lost for the specified session and serial number. This is either due to session
            case 3142:
            // being killed or network problems.
            // Connection was lost for the specified process ID and thread ID. This is either due to session
            case 3143:
            // being killed or network problems.
            // Connection was lost for the specified process ID. This is either due to session being killed
            case 3144:
            // or network problems.
            // I/O streaming direction error
            case 3145:
            // Invalid Oracle error code, Cause: An invalid Oracle error code was received by the server.
            case 3149:
            // TLI Driver: listen for SPX server reconnect failed
            case 6801:
            // TLI Driver: could not open the /etc/netware/yellowpages file
            case 6802:
            // TLI Driver: could not send datagram SAP packet for SPX
            case 6805:
            // Unable to get user privileges from SQL*Net
            case 9918:
            // Unable to get sensitivity label from connection
            case 9920:
            // Unable to get information label from connection
            case 9921:
            // TTC(Two-Task Common) ERROR CODE
            // Internal Error
            case 17001:
            // Io exception
            case 17002:
            // Closed Connection
            case 17008:
            // Closed Statement
            case 17009:
            // No data read
            case 17024:
            // internal error
            case 17089:
            // invalid buffer length
            case 17409:
            // Protocol violation
            case 17401:
            // No more data to read from socket
            case 17410:
            // FATAl
            case 17416:
            // Internal - Unexpected value
            case 17438:
            // Refcursor value is invalid
            case 17442:
            // connection terminated
            case 25407:
            // can not safely replay call
            case 25408:
            // failover happened during the network operation,cannot continue
            case 25409:
            // connection lost during rollback
            case 25425:
            // transfer timeout
            case 29276:
            case // socket read or write failed
            30676:
                return true;
            default:
                if (error_code >= 12100 && error_code <= 12299) {
                    // TNS issues
                    return true;
                }
                break;
        }
        final String error_text = (e.getMessage()).toUpperCase();
        // Exclude oracle user defined error codes (20000 through 20999) from consideration when looking for
        // certain strings.
        if ((error_code < 20000 || error_code >= 21000)) {
            if (// for control socket error
            (error_text.contains("SOCKET")) || // for control socket error
            (error_text.contains("套接字")) || //
            (error_text.contains("CONNECTION HAS ALREADY BEEN CLOSED")) || //
            (error_text.contains("BROKEN PIPE")) || //
            (error_text.contains("管道已结束"))) {
                return true;
            }
        }
        if (error_text.indexOf("COMMUNICATIONS LINK FAILURE") > -1 || error_text.indexOf("COULD NOT CREATE CONNECTION") > -1 || error_text.indexOf("ACCESS DENIED FOR USER") > -1 || error_text.indexOf("NO DATASOURCE") > -1 || error_text.indexOf("NO ALIVE DATASOURCE") > -1) {
            // errorCode忽略并且异常信息为连接出错
            return true;
        }
        return fatalErrorCodes.contains(error_code);
    }
}
