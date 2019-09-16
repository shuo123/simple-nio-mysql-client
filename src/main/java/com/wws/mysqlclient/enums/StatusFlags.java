package com.wws.mysqlclient.enums;

/**
 * 状态标识
 *
 * @see <a href="https://dev.mysql.com/doc/internals/en/status-flags.html">Status Flags</a>
 *
 * @author wws
 * @version 1.0.0
 * @date 2019-09-11 10:56
 **/
public class StatusFlags {

    private static final short SERVER_STATUS_IN_TRANS = 0x0001;
    private static final short SERVER_STATUS_AUTOCOMMIT = 0x0002;
    private static final short SERVER_MORE_RESULTS_EXISTS = 0x0008;

    private static final short SERVER_STATUS_NO_GOOD_INDEX_USED = 0x0010;
    private static final short SERVER_STATUS_NO_INDEX_USED = 0x0020;
    private static final short SERVER_STATUS_CURSOR_EXISTS = 0x0040;
    private static final short SERVER_STATUS_LAST_ROW_SENT = 0x0080;

    private static final short SERVER_STATUS_DB_DROPPED = 0x0100;
    private static final short SERVER_STATUS_NO_BACKSLASH_ESCAPES = 0x0200;
    private static final short SERVER_STATUS_METADATA_CHANGED = 0x0400;
    private static final short SERVER_QUERY_WAS_SLOW = 0x0800;

    private static final short SERVER_PS_OUT_PARAMS = 0x1000;
    private static final short SERVER_STATUS_IN_TRANS_READONLY = 0x2000;
    private static final short SERVER_SESSION_STATE_CHANGED = 0x4000;

}
