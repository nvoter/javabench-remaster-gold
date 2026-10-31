/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.time;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Custom time zone that contains offset from GMT.
 *
 * @since 3.7
 */
final class GmtTimeZone extends TimeZone {

    private static final int MILLISECONDS_PER_MINUTE = 60 * 1000;

    private static final int MINUTES_PER_HOUR = 60;

    private static final int HOURS_PER_DAY = 24;

    // Serializable!
    static final long serialVersionUID = 1L;

    private static StringBuilder twoDigits(final StringBuilder sb, final int n) {
        return sb.append((char) ('0' + n / 10)).append((char) ('0' + n % 10));
    }

    private final int offset;

    private final String zoneId;

    GmtTimeZone(final boolean negate, final int hours, final int minutes) {
        // TODO
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GmtTimeZone)) {
            return false;
        }
        final GmtTimeZone other = (GmtTimeZone) obj;
        return offset == other.offset && Objects.equals(zoneId, other.zoneId);
    }

    @Override
    public String getID() {
        // TODO
        return null;
    }

    @Override
    public int getOffset(final int era, final int year, final int month, final int day, final int dayOfWeek, final int milliseconds) {
        // TODO
        return 0;
    }

    @Override
    public int getRawOffset() {
        // TODO
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, zoneId);
    }

    @Override
    public boolean inDaylightTime(final Date date) {
        return false;
    }

    @Override
    public void setRawOffset(final int offsetMillis) {
        // TODO
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }

    @Override
    public boolean useDaylightTime() {
        // TODO
        return false;
    }
}
