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
package org.apache.commons.lang3.util;

import java.io.Serializable;
import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * A fluent {@link BitSet} with additional operations.
 * <p>
 * Originally from Apache Commons VFS with more added to act as a fluent replacement for {@link java.util.BitSet}.
 * </p>
 * @since 3.13.0
 */
public final class FluentBitSet implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Working BitSet.
     */
    private final BitSet bitSet;

    /**
     * Creates a new bit set. All bits are initially {@code false}.
     */
    public FluentBitSet() {
        this(new BitSet());
        // TODO
    }

    /**
     * Creates a new instance for the given bit set.
     *
     * @param set The bit set to wrap.
     */
    public FluentBitSet(final BitSet set) {
        // TODO
    }

    /**
     * Creates a bit set whose initial size is large enough to explicitly represent bits with indices in the range {@code 0}
     * through {@code nbits-1}. All bits are initially {@code false}.
     *
     * @param nbits the initial size of the bit set.
     * @throws NegativeArraySizeException if the specified initial size is negative.
     */
    public FluentBitSet(final int nbits) {
        this(new BitSet(nbits));
        // TODO
    }

    /**
     * Performs a logical <b>AND</b> of this target bit set with the argument bit set. This bit set is modified so that each
     * bit in it has the value {@code true} if and only if it both initially had the value {@code true} and the
     * corresponding bit in the bit set argument also had the value {@code true}.
     *
     * @param set a bit set.
     * @return {@code this} instance.
     */
    public FluentBitSet and(final BitSet set) {
        // TODO
        return null;
    }

    /**
     * Performs a logical <b>AND</b> of this target bit set with the argument bit set. This bit set is modified so that each
     * bit in it has the value {@code true} if and only if it both initially had the value {@code true} and the
     * corresponding bit in the bit set argument also had the value {@code true}.
     *
     * @param set a bit set.
     * @return {@code this} instance.
     */
    public FluentBitSet and(final FluentBitSet set) {
        // TODO
        return null;
    }

    /**
     * Clears all of the bits in this {@link BitSet} whose corresponding bit is set in the specified {@link BitSet}.
     *
     * @param set the {@link BitSet} with which to mask this {@link BitSet}.
     * @return {@code this} instance.
     */
    public FluentBitSet andNot(final BitSet set) {
        // TODO
        return null;
    }

    /**
     * Clears all of the bits in this {@link BitSet} whose corresponding bit is set in the specified {@link BitSet}.
     *
     * @param set the {@link BitSet} with which to mask this {@link BitSet}.
     * @return {@code this} instance.
     */
    public FluentBitSet andNot(final FluentBitSet set) {
        // TODO
        return null;
    }

    /**
     * Gets the wrapped bit set.
     *
     * @return the wrapped bit set.
     */
    public BitSet bitSet() {
        // TODO
        return null;
    }

    /**
     * Returns the number of bits set to {@code true} in this {@link BitSet}.
     *
     * @return the number of bits set to {@code true} in this {@link BitSet}.
     */
    public int cardinality() {
        // TODO
        return 0;
    }

    /**
     * Sets all of the bits in this BitSet to {@code false}.
     *
     * @return {@code this} instance.
     */
    public FluentBitSet clear() {
        // TODO
        return null;
    }

    /**
     * Sets the bits specified by the indexes to {@code false}.
     *
     * @param bitIndexArray the index of the bit to be cleared.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     * @return {@code this} instance.
     */
    public FluentBitSet clear(final int... bitIndexArray) {
        // TODO
        return null;
    }

    /**
     * Sets the bit specified by the index to {@code false}.
     *
     * @param bitIndex the index of the bit to be cleared.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     * @return {@code this} instance.
     */
    public FluentBitSet clear(final int bitIndex) {
        // TODO
        return null;
    }

    /**
     * Sets the bits from the specified {@code fromIndex} (inclusive) to the specified {@code toIndex} (exclusive) to
     * {@code false}.
     *
     * @param fromIndex index of the first bit to be cleared.
     * @param toIndex index after the last bit to be cleared.
     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or {@code toIndex} is negative, or
     *         {@code fromIndex} is larger than {@code toIndex}.
     * @return {@code this} instance.
     */
    public FluentBitSet clear(final int fromIndex, final int toIndex) {
        // TODO
        return null;
    }

    /**
     * Cloning this {@link BitSet} produces a new {@link BitSet} that is equal to it. The clone of the bit set is another
     * bit set that has exactly the same bits set to {@code true} as this bit set.
     *
     * @return a clone of this bit set
     * @see #size()
     */
    @Override
    public Object clone() {
        // TODO
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        // TODO
        return false;
    }

    /**
     * Sets the bit at the specified index to the complement of its current value.
     *
     * @param bitIndex the index of the bit to flip.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     * @return {@code this} instance.
     */
    public FluentBitSet flip(final int bitIndex) {
        // TODO
        return null;
    }

    /**
     * Sets each bit from the specified {@code fromIndex} (inclusive) to the specified {@code toIndex} (exclusive) to the
     * complement of its current value.
     *
     * @param fromIndex index of the first bit to flip.
     * @param toIndex index after the last bit to flip.
     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or {@code toIndex} is negative, or
     *         {@code fromIndex} is larger than {@code toIndex}.
     * @return {@code this} instance.
     */
    public FluentBitSet flip(final int fromIndex, final int toIndex) {
        // TODO
        return null;
    }

    /**
     * Returns the value of the bit with the specified index. The value is {@code true} if the bit with the index
     * {@code bitIndex} is currently set in this {@link BitSet}; otherwise, the result is {@code false}.
     *
     * @param bitIndex the bit index.
     * @return the value of the bit with the specified index.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     */
    public boolean get(final int bitIndex) {
        // TODO
        return false;
    }

    /**
     * Returns a new {@link BitSet} composed of bits from this {@link BitSet} from {@code fromIndex} (inclusive) to
     * {@code toIndex} (exclusive).
     *
     * @param fromIndex index of the first bit to include.
     * @param toIndex index after the last bit to include.
     * @return a new {@link BitSet} from a range of this {@link BitSet}.
     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or {@code toIndex} is negative, or
     *         {@code fromIndex} is larger than {@code toIndex}.
     */
    public FluentBitSet get(final int fromIndex, final int toIndex) {
        // TODO
        return null;
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    /**
     * Returns true if the specified {@link BitSet} has any bits set to {@code true} that are also set to {@code true} in
     * this {@link BitSet}.
     *
     * @param set {@link BitSet} to intersect with.
     * @return boolean indicating whether this {@link BitSet} intersects the specified {@link BitSet}.
     */
    public boolean intersects(final BitSet set) {
        // TODO
        return false;
    }

    /**
     * Returns true if the specified {@link BitSet} has any bits set to {@code true} that are also set to {@code true} in
     * this {@link BitSet}.
     *
     * @param set {@link BitSet} to intersect with.
     * @return boolean indicating whether this {@link BitSet} intersects the specified {@link BitSet}.
     */
    public boolean intersects(final FluentBitSet set) {
        // TODO
        return false;
    }

    /**
     * Returns true if this {@link BitSet} contains no bits that are set to {@code true}.
     *
     * @return boolean indicating whether this {@link BitSet} is empty.
     */
    public boolean isEmpty() {
        // TODO
        return false;
    }

    /**
     * Returns the "logical size" of this {@link BitSet}: the index of the highest set bit in the {@link BitSet} plus one.
     * Returns zero if the {@link BitSet} contains no set bits.
     *
     * @return the logical size of this {@link BitSet}.
     */
    public int length() {
        // TODO
        return 0;
    }

    /**
     * Returns the index of the first bit that is set to {@code false} that occurs on or after the specified starting index.
     *
     * @param fromIndex the index to start checking from (inclusive).
     * @return the index of the next clear bit.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     */
    public int nextClearBit(final int fromIndex) {
        // TODO
        return 0;
    }

    /**
     * Returns the index of the first bit that is set to {@code true} that occurs on or after the specified starting index.
     * If no such bit exists then {@code -1} is returned.
     * <p>
     * To iterate over the {@code true} bits in a {@link BitSet}, use the following loop:
     * </p>
     *
     * <pre>
     * {@code
     * for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
     *     // operate on index i here
     *     if (i == Integer.MAX_VALUE) {
     *         break; // or (i+1) would overflow
     *     }
     * }}
     * </pre>
     *
     * @param fromIndex the index to start checking from (inclusive).
     * @return the index of the next set bit, or {@code -1} if there is no such bit.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     */
    public int nextSetBit(final int fromIndex) {
        // TODO
        return 0;
    }

    /**
     * Performs a logical <b>OR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it
     * has the value {@code true} if and only if it either already had the value {@code true} or the corresponding bit in
     * the bit set argument has the value {@code true}.
     *
     * @param set a bit set.
     * @return {@code this} instance.
     */
    public FluentBitSet or(final BitSet set) {
        // TODO
        return null;
    }

    /**
     * Performs a logical <b>OR</b> of this bit set with the bit set arguments. This bit set is modified so that a bit in it
     * has the value {@code true} if and only if it either already had the value {@code true} or the corresponding bit in
     * the bit set argument has the value {@code true}.
     *
     * @param set a bit set.
     * @return {@code this} instance.
     */
    public FluentBitSet or(final FluentBitSet... set) {
        // TODO
        return null;
    }

    /**
     * Performs a logical <b>OR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it
     * has the value {@code true} if and only if it either already had the value {@code true} or the corresponding bit in
     * the bit set argument has the value {@code true}.
     *
     * @param set a bit set.
     * @return {@code this} instance.
     */
    public FluentBitSet or(final FluentBitSet set) {
        // TODO
        return null;
    }

    /**
     * Returns the index of the nearest bit that is set to {@code false} that occurs on or before the specified starting
     * index. If no such bit exists, or if {@code -1} is given as the starting index, then {@code -1} is returned.
     *
     * @param fromIndex the index to start checking from (inclusive).
     * @return the index of the previous clear bit, or {@code -1} if there is no such bit.
     * @throws IndexOutOfBoundsException if the specified index is less than {@code -1}.
     */
    public int previousClearBit(final int fromIndex) {
        // TODO
        return 0;
    }

    /**
     * Returns the index of the nearest bit that is set to {@code true} that occurs on or before the specified starting
     * index. If no such bit exists, or if {@code -1} is given as the starting index, then {@code -1} is returned.
     *
     * <p>
     * To iterate over the {@code true} bits in a {@link BitSet}, use the following loop:
     *
     * <pre>
     *  {@code
     * for (int i = bs.length(); (i = bs.previousSetBit(i-1)) >= 0; ) {
     *     // operate on index i here
     * }}
     * </pre>
     *
     * @param fromIndex the index to start checking from (inclusive)
     * @return the index of the previous set bit, or {@code -1} if there is no such bit
     * @throws IndexOutOfBoundsException if the specified index is less than {@code -1}
     */
    public int previousSetBit(final int fromIndex) {
        // TODO
        return 0;
    }

    /**
     * Sets the bit at the specified indexes to {@code true}.
     *
     * @param bitIndexArray a bit index array.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     * @return {@code this} instance.
     */
    public FluentBitSet set(final int... bitIndexArray) {
        // TODO
        return null;
    }

    /**
     * Sets the bit at the specified index to {@code true}.
     *
     * @param bitIndex a bit index
     * @throws IndexOutOfBoundsException if the specified index is negative
     * @return {@code this} instance.
     */
    public FluentBitSet set(final int bitIndex) {
        // TODO
        return null;
    }

    /**
     * Sets the bit at the specified index to the specified value.
     *
     * @param bitIndex a bit index.
     * @param value a boolean value to set.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     * @return {@code this} instance.
     */
    public FluentBitSet set(final int bitIndex, final boolean value) {
        // TODO
        return null;
    }

    /**
     * Sets the bits from the specified {@code fromIndex} (inclusive) to the specified {@code toIndex} (exclusive) to
     * {@code true}.
     *
     * @param fromIndex index of the first bit to be set.
     * @param toIndex index after the last bit to be set.
     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or {@code toIndex} is negative, or
     *         {@code fromIndex} is larger than {@code toIndex}.
     * @return {@code this} instance.
     */
    public FluentBitSet set(final int fromIndex, final int toIndex) {
        // TODO
        return null;
    }

    /**
     * Sets the bits from the specified {@code fromIndex} (inclusive) to the specified {@code toIndex} (exclusive) to the
     * specified value.
     *
     * @param fromIndex index of the first bit to be set.
     * @param toIndex index after the last bit to be set.
     * @param value value to set the selected bits to.
     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or {@code toIndex} is negative, or
     *         {@code fromIndex} is larger than {@code toIndex}.
     * @return {@code this} instance.
     */
    public FluentBitSet set(final int fromIndex, final int toIndex, final boolean value) {
        // TODO
        return null;
    }

    /**
     * Sets the bits from the specified {@code fromIndex} (inclusive) to the specified {@code toIndex} (inclusive) to
     * {@code true}.
     *
     * @param fromIndex index of the first bit to be set
     * @param toIndex index of the last bit to be set
     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative, or {@code toIndex} is negative, or
     *         {@code fromIndex} is larger than {@code toIndex}
     * @return {@code this} instance.
     */
    public FluentBitSet setInclusive(final int fromIndex, final int toIndex) {
        // TODO
        return null;
    }

    /**
     * Returns the number of bits of space actually in use by this {@link BitSet} to represent bit values. The maximum
     * element in the set is the size - 1st element.
     *
     * @return the number of bits currently in this bit set.
     */
    public int size() {
        // TODO
        return 0;
    }

    /**
     * Returns a stream of indices for which this {@link BitSet} contains a bit in the set state. The indices are returned
     * in order, from lowest to highest. The size of the stream is the number of bits in the set state, equal to the value
     * returned by the {@link #cardinality()} method.
     *
     * <p>
     * The bit set must remain constant during the execution of the terminal stream operation. Otherwise, the result of the
     * terminal stream operation is undefined.
     * </p>
     *
     * @return a stream of integers representing set indices.
     * @since 1.8
     */
    public IntStream stream() {
        // TODO
        return null;
    }

    /**
     * Returns a new byte array containing all the bits in this bit set.
     *
     * <p>
     * More precisely, if:
     * </p>
     * <ol>
     * <li>{@code byte[] bytes = s.toByteArray();}</li>
     * <li>then {@code bytes.length == (s.length()+7)/8} and</li>
     * <li>{@code s.get(n) == ((bytes[n/8] & (1<<(n%8))) != 0)}</li>
     * <li>for all {@code n < 8 * bytes.length}.</li>
     * </ol>
     *
     * @return a byte array containing a little-endian representation of all the bits in this bit set
     */
    public byte[] toByteArray() {
        // TODO
        return null;
    }

    /**
     * Returns a new byte array containing all the bits in this bit set.
     *
     * <p>
     * More precisely, if:
     * </p>
     * <ol>
     * <li>{@code long[] longs = s.toLongArray();}</li>
     * <li>then {@code longs.length == (s.length()+63)/64} and</li>
     * <li>{@code s.get(n) == ((longs[n/64] & (1L<<(n%64))) != 0)}</li>
     * <li>for all {@code n < 64 * longs.length}.</li>
     * </ol>
     *
     * @return a byte array containing a little-endian representation of all the bits in this bit set
     */
    public long[] toLongArray() {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }

    /**
     * Performs a logical <b>XOR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it
     * has the value {@code true} if and only if one of the following statements holds:
     * <ul>
     * <li>The bit initially has the value {@code true}, and the corresponding bit in the argument has the value
     * {@code false}.
     * <li>The bit initially has the value {@code false}, and the corresponding bit in the argument has the value
     * {@code true}.
     * </ul>
     *
     * @param set a bit set
     * @return {@code this} instance.
     */
    public FluentBitSet xor(final BitSet set) {
        // TODO
        return null;
    }

    /**
     * Performs a logical <b>XOR</b> of this bit set with the bit set argument. This bit set is modified so that a bit in it
     * has the value {@code true} if and only if one of the following statements holds:
     * <ul>
     * <li>The bit initially has the value {@code true}, and the corresponding bit in the argument has the value
     * {@code false}.
     * <li>The bit initially has the value {@code false}, and the corresponding bit in the argument has the value
     * {@code true}.
     * </ul>
     *
     * @param set a bit set
     * @return {@code this} instance.
     */
    public FluentBitSet xor(final FluentBitSet set) {
        // TODO
        return null;
    }
}
