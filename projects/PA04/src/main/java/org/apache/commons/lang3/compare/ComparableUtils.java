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
package org.apache.commons.lang3.compare;

import java.util.function.Predicate;

/**
 * Utility library to provide helper methods for translating {@link Comparable#compareTo} result into a boolean.
 *
 * <p>Example: {@code boolean x = is(myComparable).lessThanOrEqualTo(otherComparable)}</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 3.10
 */
public class ComparableUtils {

    /**
     * Provides access to the available methods
     *
     * @param <A> the type of objects that this object may be compared against.
     */
    public static class ComparableCheckBuilder<A extends Comparable<A>> {

        private final A a;

        private ComparableCheckBuilder(final A a) {
            this.a = a;
        }

        /**
         * Checks if {@code [b <= a <= c]} or {@code [b >= a >= c]} where the {@code a} is object passed to {@link #is}.
         *
         * @param b the object to compare to the base object
         * @param c the object to compare to the base object
         * @return true if the base object is between b and c
         */
        public boolean between(final A b, final A c) {
            // TODO
            return false;
        }

        /**
         * Checks if {@code (b < a < c)} or {@code (b > a > c)} where the {@code a} is object passed to {@link #is}.
         *
         * @param b the object to compare to the base object
         * @param c the object to compare to the base object
         * @return true if the base object is between b and c and not equal to those
         */
        public boolean betweenExclusive(final A b, final A c) {
            // TODO
            return false;
        }

        private boolean betweenOrdered(final A b, final A c) {
            return greaterThanOrEqualTo(b) && lessThanOrEqualTo(c);
        }

        private boolean betweenOrderedExclusive(final A b, final A c) {
            return greaterThan(b) && lessThan(c);
        }

        /**
         * Checks if the object passed to {@link #is} is equal to {@code b}
         *
         * @param b the object to compare to the base object
         * @return true if the value returned by {@link Comparable#compareTo} is equal to {@code 0}
         */
        public boolean equalTo(final A b) {
            // TODO
            return false;
        }

        /**
         * Checks if the object passed to {@link #is} is greater than {@code b}
         *
         * @param b the object to compare to the base object
         * @return true if the value returned by {@link Comparable#compareTo} is greater than {@code 0}
         */
        public boolean greaterThan(final A b) {
            // TODO
            return false;
        }

        /**
         * Checks if the object passed to {@link #is} is greater than or equal to {@code b}
         *
         * @param b the object to compare to the base object
         * @return true if the value returned by {@link Comparable#compareTo} is greater than or equal to {@code 0}
         */
        public boolean greaterThanOrEqualTo(final A b) {
            // TODO
            return false;
        }

        /**
         * Checks if the object passed to {@link #is} is less than {@code b}
         *
         * @param b the object to compare to the base object
         * @return true if the value returned by {@link Comparable#compareTo} is less than {@code 0}
         */
        public boolean lessThan(final A b) {
            // TODO
            return false;
        }

        /**
         * Checks if the object passed to {@link #is} is less than or equal to {@code b}
         *
         * @param b the object to compare to the base object
         * @return true if the value returned by {@link Comparable#compareTo} is less than or equal to {@code 0}
         */
        public boolean lessThanOrEqualTo(final A b) {
            // TODO
            return false;
        }
    }

    /**
     * Checks if {@code [b <= a <= c]} or {@code [b >= a >= c]} where the {@code a} is the tested object.
     *
     * @param b the object to compare to the tested object
     * @param c the object to compare to the tested object
     * @param <A> type of the test object
     * @return a predicate for true if the tested object is between b and c
     */
    public static <A extends Comparable<A>> Predicate<A> between(final A b, final A c) {
        // TODO
        return null;
    }

    /**
     * Checks if {@code (b < a < c)} or {@code (b > a > c)} where the {@code a} is the tested object.
     *
     * @param b the object to compare to the tested object
     * @param c the object to compare to the tested object
     * @param <A> type of the test object
     * @return a predicate for true if the tested object is between b and c and not equal to those
     */
    public static <A extends Comparable<A>> Predicate<A> betweenExclusive(final A b, final A c) {
        // TODO
        return null;
    }

    /**
     * Checks if the tested object is greater than or equal to {@code b}
     *
     * @param b the object to compare to the tested object
     * @param <A> type of the test object
     * @return a predicate for true if the value returned by {@link Comparable#compareTo}
     * is greater than or equal to {@code 0}
     */
    public static <A extends Comparable<A>> Predicate<A> ge(final A b) {
        // TODO
        return null;
    }

    /**
     * Checks if the tested object is greater than {@code b}
     *
     * @param b the object to compare to the tested object
     * @param <A> type of the test object
     * @return a predicate for true if the value returned by {@link Comparable#compareTo} is greater than {@code 0}
     */
    public static <A extends Comparable<A>> Predicate<A> gt(final A b) {
        // TODO
        return null;
    }

    /**
     * Provides access to the available methods
     *
     * @param a base object in the further comparison
     * @param <A> type of the base object
     * @return a builder object with further methods
     */
    public static <A extends Comparable<A>> ComparableCheckBuilder<A> is(final A a) {
        // TODO
        return null;
    }

    /**
     * Checks if the tested object is less than or equal to {@code b}
     *
     * @param b the object to compare to the tested object
     * @param <A> type of the test object
     * @return a predicate for true if the value returned by {@link Comparable#compareTo}
     * is less than or equal to {@code 0}
     */
    public static <A extends Comparable<A>> Predicate<A> le(final A b) {
        // TODO
        return null;
    }

    /**
     * Checks if the tested object is less than {@code b}
     *
     * @param b the object to compare to the tested object
     * @param <A> type of the test object
     * @return a predicate for true if the value returned by {@link Comparable#compareTo} is less than {@code 0}
     */
    public static <A extends Comparable<A>> Predicate<A> lt(final A b) {
        // TODO
        return null;
    }

    /**
     * Returns the greater of two {@link Comparable} values, ignoring null.
     * <p>
     * For three or more values, use {@link ObjectUtils#max(Comparable...)}.
     * </p>
     *
     * @param <A> Type of what we are comparing.
     * @param comparable1 the first comparable, may be null.
     * @param comparable2 the second comparable, may be null.
     * @return the largest of {@code comparable1} and {@code comparable2}.
     * @see ObjectUtils#max(Comparable...)
     * @since 3.13.0
     */
    public static <A extends Comparable<A>> A max(final A comparable1, final A comparable2) {
        // TODO
        return null;
    }

    /**
     * Returns the lesser of two {@link Comparable} values, ignoring null.
     * <p>
     * For three or more values, use {@link ObjectUtils#min(Comparable...)}.
     * </p>
     *
     * @param <A> Type of what we are comparing.
     * @param comparable1 the first comparable, may be null.
     * @param comparable2 the second comparable, may be null.
     * @return the smallest of {@code comparable1} and {@code comparable2}.
     * @see ObjectUtils#min(Comparable...)
     * @since 3.13.0
     */
    public static <A extends Comparable<A>> A min(final A comparable1, final A comparable2) {
        // TODO
        return null;
    }

    private ComparableUtils() {
        // empty
    }
}
