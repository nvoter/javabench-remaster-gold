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

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author sandzhang[sandzhangtoo@gmail.com]
 */
public class StringUtils {

    private static final Log LOG = LogFactory.getLog(StringUtils.class);

    private StringUtils() {
    }

    /**
     * Example: subString("12345","1","4")=23
     *
     * @param src   the source string from which to extract the substring
     * @param start the starting point of the substring
     * @param to    the ending point of the substring
     * @return the extracted substring converted to an Integer, or null if any of the input strings is null
     */
    public static Integer subStringToInteger(String src, String start, String to) {
        // TODO
        return null;
    }

    /**
     * Example: subString("abcd","a","c")="b"
     *
     * @param src   the source string from which to extract the substring
     * @param start null while start from index=0
     * @param to    null while to index=src.length
     * @return the extracted substring, or null if any of the input strings is null
     */
    public static String subString(String src, String start, String to) {
        // TODO
        return null;
    }

    /**
     * Example: subString("abcdc","a","c",true)="bcd"
     *
     * @param src    the source string from which to extract the substring
     * @param start  null while start from index=0
     * @param to     null while to index=src.length
     * @param toLast true while to index=src.lastIndexOf(to)
     * @return the extracted substring, or null if any of the input strings is null, or if the start/end points are invalid
     */
    public static String subString(String src, String start, String to, boolean toLast) {
        // TODO
        return null;
    }

    /**
     * Converts a string representation of an integer to an Integer object.
     *
     * @param in the input string to be converted (can be null)
     * @return the Integer object parsed from the input string, or null if the input is null, empty, or not a valid integer
     */
    public static Integer stringToInteger(String in) {
        // TODO
        return null;
    }

    public static boolean equals(String a, String b) {
        // TODO
        return false;
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        // TODO
        return false;
    }

    public static boolean isEmpty(CharSequence value) {
        // TODO
        return false;
    }

    public static int lowerHashCode(String text) {
        // TODO
        return 0;
    }

    public static boolean isNumber(String str) {
        // TODO
        return false;
    }

    public static boolean isNumber(char[] chars) {
        // TODO
        return false;
    }

    public static String formatDateTime19(long millis, TimeZone timeZone) {
        Calendar cale = timeZone == null ? Calendar.getInstance() : Calendar.getInstance(timeZone);
        cale.setTimeInMillis(millis);
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int dayOfMonth = cale.get(Calendar.DAY_OF_MONTH);
        int hour = cale.get(Calendar.HOUR_OF_DAY);
        int minute = cale.get(Calendar.MINUTE);
        int second = cale.get(Calendar.SECOND);
        char[] chars = new char[19];
        chars[0] = (char) (year / 1000 + '0');
        chars[1] = (char) ((year / 100) % 10 + '0');
        chars[2] = (char) ((year / 10) % 10 + '0');
        chars[3] = (char) (year % 10 + '0');
        chars[4] = '-';
        chars[5] = (char) (month / 10 + '0');
        chars[6] = (char) (month % 10 + '0');
        chars[7] = '-';
        chars[8] = (char) (dayOfMonth / 10 + '0');
        chars[9] = (char) (dayOfMonth % 10 + '0');
        chars[10] = ' ';
        chars[11] = (char) (hour / 10 + '0');
        chars[12] = (char) (hour % 10 + '0');
        chars[13] = ':';
        chars[14] = (char) (minute / 10 + '0');
        chars[15] = (char) (minute % 10 + '0');
        chars[16] = ':';
        chars[17] = (char) (second / 10 + '0');
        chars[18] = (char) (second % 10 + '0');
        return new String(chars);
    }

    public static String removeNameQuotes(String s) {
        if (s == null || s.length() <= 1) {
            return null;
        }
        int len = s.length();
        char c0 = s.charAt(0);
        char last = s.charAt(len - 1);
        if (c0 == last && (c0 == '`' || c0 == '\'' || c0 == '\"')) {
            return s.substring(1, len - 1);
        }
        return s;
    }

    public static String replaceAll(String s, String src, String dest) {
        if (s == null || src == null || dest == null || src.length() == 0) {
            return s;
        }
        int pos = s.indexOf(src);
        if (pos < 0) {
            return s;
        }
        int capacity = dest.length() > src.length() ? s.length() * 2 : s.length();
        StringBuilder sb = new StringBuilder(capacity);
        int writen = 0;
        for (; pos >= 0; ) {
            sb.append(s, writen, pos);
            sb.append(dest);
            writen = pos + src.length();
            pos = s.indexOf(src, writen);
        }
        sb.append(s, writen, s.length());
        return sb.toString();
    }
}
