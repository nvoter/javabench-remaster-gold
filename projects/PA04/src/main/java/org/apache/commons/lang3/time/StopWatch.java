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

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * {@link StopWatch} provides a convenient API for timings.
 *
 * <p>
 * To start the watch, call {@link #start()} or {@link StopWatch#createStarted()}. At this point you can:
 * </p>
 * <ul>
 * <li>{@link #split()} the watch to get the time whilst the watch continues in the background. {@link #unsplit()} will remove the effect of the split. At this
 * point, these three options are available again.</li>
 * <li>{@link #suspend()} the watch to pause it. {@link #resume()} allows the watch to continue. Any time between the suspend and resume will not be counted in
 * the total. At this point, these three options are available again.</li>
 * <li>{@link #stop()} the watch to complete the timing session.</li>
 * </ul>
 *
 * <p>
 * It is intended that the output methods {@link #toString()} and {@link #getTime()} should only be called after stop, split or suspend, however a suitable
 * result will be returned at other points.
 * </p>
 *
 * <p>
 * NOTE: As from v2.1, the methods protect against inappropriate calls. Thus you cannot now call stop before start, resume before suspend or unsplit before
 * split.
 * </p>
 *
 * <ol>
 * <li>{@link #split()}, {@link #suspend()}, or {@link #stop()} cannot be invoked twice</li>
 * <li>{@link #unsplit()} may only be called if the watch has been {@link #split()}</li>
 * <li>{@link #resume()} may only be called if the watch has been {@link #suspend()}</li>
 * <li>{@link #start()} cannot be called twice without calling {@link #reset()}</li>
 * </ol>
 *
 * <p>
 * This class is not thread-safe
 * </p>
 *
 * @see DurationUtils#of(FailableRunnable)
 * @see DurationUtils#of(FailableConsumer)
 *
 * @since 2.0
 */
public class StopWatch {

    /**
     * Enumeration type which indicates the split status of a StopWatch.
     */
    private enum SplitState {

        SPLIT, UNSPLIT
    }

    /**
     * Enumeration type which indicates the status of a StopWatch.
     */
    private enum State {

        RUNNING {

            @Override
            boolean isStarted() {
                return true;
            }

            @Override
            boolean isStopped() {
                return false;
            }

            @Override
            boolean isSuspended() {
                return false;
            }
        }
        , STOPPED {

            @Override
            boolean isStarted() {
                return false;
            }

            @Override
            boolean isStopped() {
                return true;
            }

            @Override
            boolean isSuspended() {
                return false;
            }
        }
        , SUSPENDED {

            @Override
            boolean isStarted() {
                return true;
            }

            @Override
            boolean isStopped() {
                return false;
            }

            @Override
            boolean isSuspended() {
                return true;
            }
        }
        , UNSTARTED {

            @Override
            boolean isStarted() {
                return false;
            }

            @Override
            boolean isStopped() {
                return true;
            }

            @Override
            boolean isSuspended() {
                return false;
            }
        }
        ;

        /**
         * Tests whether the StopWatch is started. A suspended StopWatch is also started.
         *
         * @return boolean If the StopWatch is started.
         */
        abstract boolean isStarted();

        /**
         * Tests whether the StopWatch is stopped. A StopWatch which is not yet started and explicitly stopped is considered stopped.
         *
         * @return boolean If the StopWatch is stopped.
         */
        abstract boolean isStopped();

        /**
         * Tests whether the StopWatch is suspended.
         *
         * @return boolean If the StopWatch is suspended.
         */
        abstract boolean isSuspended();
    }

    private static final long NANO_2_MILLIS = 1000000L;

    /**
     * Creates a StopWatch.
     *
     * @return StopWatch a StopWatch.
     *
     * @since 3.10
     */
    public static StopWatch create() {
        // TODO
        return null;
    }

    /**
     * Creates and starts a StopWatch.
     *
     * @return StopWatch a started StopWatch.
     *
     * @since 3.5
     */
    public static StopWatch createStarted() {
        // TODO
        return null;
    }

    /**
     * A message for string presentation.
     *
     * @since 3.10
     */
    private final String message;

    /**
     * The current running state of the StopWatch.
     */
    private State runningState = State.UNSTARTED;

    /**
     * Whether the StopWatch has a split time recorded.
     */
    private SplitState splitState = SplitState.UNSPLIT;

    /**
     * The start time in nanoseconds.
     *
     * This field can be removed once we move off of Java 8.
     */
    private long startTimeNanos;

    /**
     * The start Instant.
     * <p>
     * nanoTime is only for elapsed time so we need to also store the currentTimeMillis to maintain the old getStartTime API.
     * </p>
     * <p>
     * On Java 8, Instant has millisecond precision, only later versions use nanoseconds.
     * </p>
     */
    private Instant startInstant;

    /**
     * The end Instant.
     * <p>
     * nanoTime is only for elapsed time so we need to also store the currentTimeMillis to maintain the old getStartTime API.
     * </p>
     * <p>
     * On Java 8, Instant has millisecond precision, only later versions use nanoseconds.
     * </p>
     */
    private Instant stopInstant;

    /**
     * The stop time in nanoseconds.
     *
     * This field can be removed once we move off of Java 8.
     */
    private long stopTimeNanos;

    /**
     * Constructs a new instance.
     */
    public StopWatch() {
        this(null);
        // TODO
    }

    /**
     * Constructs a new instance.
     *
     * @param message A message for string presentation.
     * @since 3.10
     */
    public StopWatch(final String message) {
        // TODO
    }

    /**
     * Formats the split time with {@link DurationFormatUtils#formatDurationHMS}.
     *
     * @return the split time formatted by {@link DurationFormatUtils#formatDurationHMS}.
     * @since 3.10
     */
    public String formatSplitTime() {
        // TODO
        return null;
    }

    /**
     * Formats the time formatted with {@link DurationFormatUtils#formatDurationHMS}.
     *
     * @return the time formatted by {@link DurationFormatUtils#formatDurationHMS}.
     * @since 3.10
     */
    public String formatTime() {
        // TODO
        return null;
    }

    /**
     * Gets the Duration on the StopWatch.
     *
     * <p>
     * This is either the Duration between the start and the moment this method is called, or the Duration between start and stop.
     * </p>
     *
     * @return the Duration.
     * @since 3.16.0
     */
    public Duration getDuration() {
        // TODO
        return null;
    }

    /**
     * Gets the message for string presentation.
     *
     * @return the message for string presentation.
     * @since 3.10
     */
    public String getMessage() {
        // TODO
        return null;
    }

    /**
     * Gets the <em>elapsed</em> time in nanoseconds.
     *
     * <p>
     * This is either the time between the start and the moment this method is called, or the amount of time between start and stop.
     * </p>
     *
     * @return the <em>elapsed</em> time in nanoseconds.
     * @see System#nanoTime()
     * @since 3.0
     */
    public long getNanoTime() {
        if (runningState == State.STOPPED || runningState == State.SUSPENDED) {
            return stopTimeNanos - startTimeNanos;
        }
        if (runningState == State.UNSTARTED) {
            return 0;
        }
        if (runningState == State.RUNNING) {
            return System.nanoTime() - startTimeNanos;
        }
        throw new IllegalStateException("Illegal running state has occurred.");
    }

    /**
     * Gets the split Duration on the StopWatch.
     *
     * <p>
     * This is the Duration between start and latest split.
     * </p>
     *
     * @return the split Duration
     *
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 3.16.0
     */
    public Duration getSplitDuration() {
        // TODO
        return null;
    }

    /**
     * Gets the split time in nanoseconds.
     *
     * <p>
     * This is the time between start and latest split.
     * </p>
     *
     * @return the split time in nanoseconds
     *
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 3.0
     */
    public long getSplitNanoTime() {
        if (splitState != SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time.");
        }
        return stopTimeNanos - startTimeNanos;
    }

    /**
     * Gets the split time on the StopWatch.
     *
     * <p>
     * This is the time between start and latest split.
     * </p>
     *
     * @return the split time in milliseconds
     *
     * @throws IllegalStateException if the StopWatch has not yet been split.
     * @since 2.1
     * @deprecated Use {@link #getSplitDuration()}.
     */
    @Deprecated
    public long getSplitTime() {
        // TODO
        return 0L;
    }

    /**
     * Gets the Instant this StopWatch was started, between the current time and midnight, January 1, 1970 UTC.
     *
     * @return the Instant this StopWatch was started, between the current time and midnight, January 1, 1970 UTC.
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 3.16.0
     */
    public Instant getStartInstant() {
        // TODO
        return null;
    }

    /**
     * Gets the time this StopWatch was started in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     *
     * @return the time this StopWatch was started in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 2.4
     * @deprecated Use {@link #getStartInstant()}.
     */
    @Deprecated
    public long getStartTime() {
        // TODO
        return 0L;
    }

    /**
     * Gets the Instant this StopWatch was stopped, between the current time and midnight, January 1, 1970 UTC.
     *
     * @return the Instant this StopWatch was stopped in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 3.16.0
     */
    public Instant getStopInstant() {
        // TODO
        return null;
    }

    /**
     * Gets the time this StopWatch was stopped in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     *
     * @return the time this StopWatch was stopped in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     * @throws IllegalStateException if this StopWatch has not been started
     * @since 3.12.0
     * @deprecated Use {@link #getStopInstant()}.
     */
    @Deprecated
    public long getStopTime() {
        // TODO
        return 0L;
    }

    /**
     * Gets the time on the StopWatch.
     *
     * <p>
     * This is either the time between the start and the moment this method is called, or the amount of time between start and stop.
     * </p>
     *
     * @return the time in milliseconds
     * @deprecated Use {@link #getDuration()}.
     */
    @Deprecated
    public long getTime() {
        // TODO
        return 0L;
    }

    /**
     * Gets the time in the specified TimeUnit.
     *
     * <p>
     * This is either the time between the start and the moment this method is called, or the amount of time between start and stop. The resulting time will be
     * expressed in the desired TimeUnit with any remainder rounded down. For example, if the specified unit is {@code TimeUnit.HOURS} and the StopWatch time is
     * 59 minutes, then the result returned will be {@code 0}.
     * </p>
     *
     * @param timeUnit the unit of time, not null
     * @return the time in the specified TimeUnit, rounded down
     * @since 3.5
     */
    public long getTime(final TimeUnit timeUnit) {
        // TODO
        return 0L;
    }

    /**
     * Tests whether the StopWatch is started. A suspended StopWatch is also started watch.
     *
     * @return boolean If the StopWatch is started.
     * @since 3.2
     */
    public boolean isStarted() {
        // TODO
        return false;
    }

    /**
     * Tests whether StopWatch is stopped. The StopWatch which's not yet started and explicitly stopped StopWatch is considered as stopped.
     *
     * @return boolean If the StopWatch is stopped.
     * @since 3.2
     */
    public boolean isStopped() {
        // TODO
        return false;
    }

    /**
     * Tests whether the StopWatch is suspended.
     *
     * @return boolean If the StopWatch is suspended.
     * @since 3.2
     */
    public boolean isSuspended() {
        // TODO
        return false;
    }

    /**
     * Converts nanoseconds to milliseconds.
     *
     * @param nanos nanoseconds to convert.
     * @return milliseconds conversion result.
     */
    private long nanosToMillis(final long nanos) {
        return nanos / NANO_2_MILLIS;
    }

    /**
     * Resets the StopWatch. Stops it if need be.
     *
     * <p>
     * This method clears the internal values to allow the object to be reused.
     * </p>
     */
    public void reset() {
        // TODO
    }

    /**
     * Resumes the StopWatch after a suspend.
     *
     * <p>
     * This method resumes the watch after it was suspended. The watch will not include time between the suspend and resume calls in the total time.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch has not been suspended.
     */
    public void resume() {
        // TODO
    }

    /**
     * Splits the time.
     *
     * <p>
     * This method sets the stop time of the watch to allow a time to be extracted. The start time is unaffected, enabling {@link #unsplit()} to continue the
     * timing from the original start point.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void split() {
        // TODO
    }

    /**
     * Starts the StopWatch.
     *
     * <p>
     * This method starts a new timing session, clearing any previous values.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is already running.
     */
    public void start() {
        // TODO
    }

    /**
     * Stops the StopWatch.
     *
     * <p>
     * This method ends a new timing session, allowing the time to be retrieved.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not running.
     */
    public void stop() {
        // TODO
    }

    /**
     * Suspends the StopWatch for later resumption.
     *
     * <p>
     * This method suspends the watch until it is resumed. The watch will not include time between the suspend and resume calls in the total time.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch is not currently running.
     */
    public void suspend() {
        // TODO
    }

    /**
     * Gets a summary of the split time that the StopWatch recorded as a string.
     *
     * <p>
     * The format used is ISO 8601-like, [<i>message</i> ]<i>hours</i>:<i>minutes</i>:<i>seconds</i>.<i>milliseconds</i>.
     * </p>
     *
     * @return the split time as a String
     * @since 2.1
     * @since 3.10 Returns the prefix {@code "message "} if the message is set.
     */
    public String toSplitString() {
        // TODO
        return null;
    }

    /**
     * Gets a summary of the time that the StopWatch recorded as a string.
     *
     * <p>
     * The format used is ISO 8601-like, [<i>message</i> ]<i>hours</i>:<i>minutes</i>:<i>seconds</i>.<i>milliseconds</i>.
     * </p>
     *
     * @return the time as a String
     * @since 3.10 Returns the prefix {@code "message "} if the message is set.
     */
    @Override
    public String toString() {
        // TODO
        return null;
    }

    /**
     * Removes a split.
     *
     * <p>
     * This method clears the stop time. The start time is unaffected, enabling timing from the original start point to continue.
     * </p>
     *
     * @throws IllegalStateException if the StopWatch has not been split.
     */
    public void unsplit() {
        // TODO
    }
}
