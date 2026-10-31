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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;

public class Histogram {

    private final long[] ranges;

    private final AtomicLongArray rangeCounters;

    public Histogram(long... ranges) {
        // TODO
    }

    public static Histogram makeHistogram(int rangeCount) {
        // TODO
        return null;
    }

    public Histogram(TimeUnit timeUnit, long... ranges) {
        // TODO
    }

    public void reset() {
        for (int i = 0; i < rangeCounters.length(); i++) {
            rangeCounters.set(i, 0);
        }
    }

    public void record(long millis) {
        // TODO
    }

    public long get(int index) {
        // TODO
        return 0L;
    }

    public long[] toArray() {
        long[] array = new long[rangeCounters.length()];
        for (int i = 0; i < rangeCounters.length(); i++) {
            array[i] = rangeCounters.get(i);
        }
        return array;
    }

    public long[] toArrayAndReset() {
        long[] array = new long[rangeCounters.length()];
        for (int i = 0; i < rangeCounters.length(); i++) {
            array[i] = rangeCounters.getAndSet(i, 0);
        }
        return array;
    }

    public long[] getRanges() {
        // TODO
        return null;
    }

    public long getValue(int index) {
        return rangeCounters.get(index);
    }

    public long getSum() {
        long sum = 0;
        for (int i = 0; i < rangeCounters.length(); ++i) {
            sum += rangeCounters.get(i);
        }
        return sum;
    }

    public String toString() {
        // TODO
        return null;
    }
}
