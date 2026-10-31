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
package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Utilities for working with {@link Field}s by reflection. Adapted and refactored from the dormant [reflect] Commons
 * sandbox component.
 * <p>
 * The ability is provided to break the scoping restrictions coded by the programmer. This can allow fields to be
 * changed that shouldn't be. This facility should be used with care.
 * </p>
 * @since 2.5
 */
public class FieldUtils {

    /**
     * Gets all fields of the given class and its parents (if any).
     *
     * @param cls
     *            the {@link Class} to query
     * @return an array of Fields (possibly empty).
     * @throws NullPointerException
     *             if the class is {@code null}
     * @since 3.2
     */
    public static Field[] getAllFields(final Class<?> cls) {
        // TODO
        return null;
    }

    /**
     * Gets all fields of the given class and its parents (if any).
     *
     * @param cls
     *            the {@link Class} to query
     * @return a list of Fields (possibly empty).
     * @throws NullPointerException
     *             if the class is {@code null}
     * @since 3.2
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        // TODO
        return null;
    }

    /**
     * Gets an accessible {@link Field} by name respecting scope. Only the specified class will be considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @return the Field object
     * @throws NullPointerException
     *             if the class is {@code null}
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, or empty
     */
    public static Field getDeclaredField(final Class<?> cls, final String fieldName) {
        // TODO
        return null;
    }

    /**
     * Gets an accessible {@link Field} by name, breaking scope if requested. Only the specified class will be
     * considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @return the Field object
     * @throws NullPointerException
     *             if the class is {@code null}
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, or empty
     */
    public static Field getDeclaredField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        // TODO
        return null;
    }

    /**
     * Gets an accessible {@link Field} by name respecting scope. Superclasses/interfaces will be considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @return the Field object
     * @throws NullPointerException
     *             if the class is {@code null}
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, or empty
     */
    public static Field getField(final Class<?> cls, final String fieldName) {
        // TODO
        return null;
    }

    /**
     * Gets an accessible {@link Field} by name, breaking scope if requested. Superclasses/interfaces will be
     * considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @return the Field object
     * @throws NullPointerException if the class is {@code null}
     * @throws IllegalArgumentException if the field name is blank or empty or is matched at multiple places
     * in the inheritance hierarchy
     */
    public static Field getField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        // TODO
        return null;
    }

    /**
     * Gets all fields of the given class and its parents (if any) that are annotated with the given annotation.
     * @param cls
     *            the {@link Class} to query
     * @param annotationCls
     *            the {@link Annotation} that must be present on a field to be matched
     * @return a list of Fields (possibly empty).
     * @throws NullPointerException
     *            if the class or annotation are {@code null}
     * @since 3.4
     */
    public static List<Field> getFieldsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        // TODO
        return null;
    }

    /**
     * Gets all fields of the given class and its parents (if any) that are annotated with the given annotation.
     * @param cls
     *            the {@link Class} to query
     * @param annotationCls
     *            the {@link Annotation} that must be present on a field to be matched
     * @return an array of Fields (possibly empty).
     * @throws NullPointerException
     *            if the class or annotation are {@code null}
     * @since 3.4
     */
    public static Field[] getFieldsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        // TODO
        return null;
    }

    /**
     * Reads the named {@code public} {@link Field}. Only the class of the specified object will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @return the value of the field
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank or empty, or could not be found
     * @throws IllegalAccessException
     *             if the named field is not {@code public}
     */
    public static Object readDeclaredField(final Object target, final String fieldName) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Gets a {@link Field} value by name. Only the class of the specified object will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match public fields.
     * @return the Field object
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank or empty, or could not be found
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static Object readDeclaredField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Gets the value of a {@code static} {@link Field} by name. The field must be {@code public}. Only the specified
     * class will be considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @return the value of the field
     * @throws NullPointerException
     *             if the class is {@code null}, or the field could not be found
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, empty, or is not {@code static}
     * @throws IllegalAccessException
     *             if the field is not accessible
     */
    public static Object readDeclaredStaticField(final Class<?> cls, final String fieldName) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Gets the value of a {@code static} {@link Field} by name. Only the specified class will be considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @return the Field object
     * @throws NullPointerException
     *             if the class is {@code null}, or the field could not be found
     * @throws IllegalArgumentException
     *             if the field name is blank or empty, is not {@code static}
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static Object readDeclaredStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads an accessible {@link Field}.
     *
     * @param field
     *            the field to use
     * @param target
     *            the object to call on, may be {@code null} for {@code static} fields
     * @return the field value
     * @throws NullPointerException
     *             if the field is {@code null}
     * @throws IllegalAccessException
     *             if the field is not accessible
     */
    public static Object readField(final Field field, final Object target) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads a {@link Field}.
     *
     * @param field
     *            the field to use
     * @param target
     *            the object to call on, may be {@code null} for {@code static} fields
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
     * @return the field value
     * @throws NullPointerException
     *             if the field is {@code null}
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static Object readField(final Field field, final Object target, final boolean forceAccess) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads the named {@code public} {@link Field}. Superclasses will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @return the value of the field
     * @throws NullPointerException
     *             if the target is {@code null}
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, empty, or could not be found
     * @throws IllegalAccessException
     *             if the named field is not {@code public}
     */
    public static Object readField(final Object target, final String fieldName) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads the named {@link Field}. Superclasses will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @return the field value
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, empty, or could not be found
     * @throws IllegalAccessException
     *             if the named field is not made accessible
     */
    public static Object readField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads the named {@code public static} {@link Field}. Superclasses will be considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @return the value of the field
     * @throws NullPointerException
     *             if the class is {@code null}, or the field could not be found
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank or empty, or is not {@code static}
     * @throws IllegalAccessException
     *             if the field is not accessible
     */
    public static Object readStaticField(final Class<?> cls, final String fieldName) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads the named {@code static} {@link Field}. Superclasses will be considered.
     *
     * @param cls
     *            the {@link Class} to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @return the Field object
     * @throws NullPointerException
     *             if the class is {@code null}, or the field could not be found
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank or empty, or is not {@code static}
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static Object readStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads an accessible {@code static} {@link Field}.
     *
     * @param field
     *            to read
     * @return the field value
     * @throws NullPointerException
     *             if the field is {@code null}
     * @throws IllegalArgumentException
     *             if the field is not {@code static}
     * @throws IllegalAccessException
     *             if the field is not accessible
     */
    public static Object readStaticField(final Field field) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Reads a static {@link Field}.
     *
     * @param field
     *            to read
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method.
     * @return the field value
     * @throws NullPointerException
     *             if the field is {@code null}
     * @throws IllegalArgumentException
     *             if the field is not {@code static}
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static Object readStaticField(final Field field, final boolean forceAccess) throws IllegalAccessException {
        // TODO
        return null;
    }

    /**
     * Removes the final modifier from a {@link Field}.
     *
     * @param field
     *            to remove the final modifier
     * @throws NullPointerException
     *             if the field is {@code null}
     * @since 3.2
     */
    public static void removeFinalModifier(final Field field) {
        // TODO
    }

    /**
     * Removes the final modifier from a {@link Field}.
     *
     * @param field
     *            to remove the final modifier
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @throws NullPointerException
     *             if the field is {@code null}
     * @deprecated As of Java 12, we can no longer drop the {@code final} modifier, thus
     *             rendering this method obsolete. The JDK discussion about this change can be found
     *             here: https://mail.openjdk.java.net/pipermail/core-libs-dev/2018-November/056486.html
     * @since 3.3
     */
    @Deprecated
    public static void removeFinalModifier(final Field field, final boolean forceAccess) {
        // TODO
    }

    /**
     * Writes a {@code public} {@link Field}. Only the specified class will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param value
     *            to set
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank or empty, or could not be found,
     *             or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static void writeDeclaredField(final Object target, final String fieldName, final Object value) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a {@code public} {@link Field}. Only the specified class will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param value
     *            to set
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank or empty, or could not be found,
     *             or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static void writeDeclaredField(final Object target, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a named {@code public static} {@link Field}. Only the specified class will be considered.
     *
     * @param cls
     *            {@link Class} on which the field is to be found
     * @param fieldName
     *            to write
     * @param value
     *            to set
     * @throws NullPointerException
     *             if {@code cls} is {@code null} or the field cannot be located
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, empty, not {@code static}, or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not {@code public} or is {@code final}
     */
    public static void writeDeclaredStaticField(final Class<?> cls, final String fieldName, final Object value) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a named {@code static} {@link Field}. Only the specified class will be considered.
     *
     * @param cls
     *            {@link Class} on which the field is to be found
     * @param fieldName
     *            to write
     * @param value
     *            to set
     * @param forceAccess
     *            whether to break scope restrictions using the {@code AccessibleObject#setAccessible(boolean)} method.
     *            {@code false} will only match {@code public} fields.
     * @throws NullPointerException
     *             if {@code cls} is {@code null} or the field cannot be located
     * @throws IllegalArgumentException
     *             if the field name is {@code null}, blank, empty, not {@code static}, or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible or is {@code final}
     */
    public static void writeDeclaredStaticField(final Class<?> cls, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes an accessible {@link Field}.
     *
     * @param field
     *            to write
     * @param target
     *            the object to call on, may be {@code null} for {@code static} fields
     * @param value
     *            to set
     * @throws NullPointerException
     *             if the field is {@code null}
     * @throws IllegalArgumentException
     *             if {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not accessible or is {@code final}
     */
    public static void writeField(final Field field, final Object target, final Object value) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a {@link Field}.
     *
     * @param field
     *            to write
     * @param target
     *            the object to call on, may be {@code null} for {@code static} fields
     * @param value
     *            to set
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @throws NullPointerException
     *             if the field is {@code null}
     * @throws IllegalArgumentException
     *             if {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible or is {@code final}
     */
    public static void writeField(final Field field, final Object target, final Object value, final boolean forceAccess) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a {@code public} {@link Field}. Superclasses will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param value
     *            to set
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank, empty, or could not be found,
     *             or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not accessible
     */
    public static void writeField(final Object target, final String fieldName, final Object value) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a {@link Field}. Superclasses will be considered.
     *
     * @param target
     *            the object to reflect, must not be {@code null}
     * @param fieldName
     *            the field name to obtain
     * @param value
     *            to set
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank, empty, or could not be found,
     *             or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible
     */
    public static void writeField(final Object target, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a named {@code public static} {@link Field}. Superclasses will be considered.
     *
     * @param cls
     *            {@link Class} on which the field is to be found
     * @param fieldName
     *            to write
     * @param value
     *            to set
     * @throws NullPointerException
     *             if {@code target} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank or empty, the field cannot be located or is
     *             not {@code static}, or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not {@code public} or is {@code final}
     */
    public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a named {@code static} {@link Field}. Superclasses will be considered.
     *
     * @param cls
     *            {@link Class} on which the field is to be found
     * @param fieldName
     *            to write
     * @param value
     *            to set
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @throws NullPointerException
     *             if {@code cls} is {@code null} or the field cannot be located
     * @throws IllegalArgumentException
     *             if {@code fieldName} is {@code null}, blank or empty, the field not {@code static}, or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible or is {@code final}
     */
    public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a {@code public static} {@link Field}.
     *
     * @param field
     *            to write
     * @param value
     *            to set
     * @throws NullPointerException
     *              if the field is {@code null}
     * @throws IllegalArgumentException
     *              if the field is not {@code static}, or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not {@code public} or is {@code final}
     */
    public static void writeStaticField(final Field field, final Object value) throws IllegalAccessException {
        // TODO
    }

    /**
     * Writes a static {@link Field}.
     *
     * @param field
     *            to write
     * @param value
     *            to set
     * @param forceAccess
     *            whether to break scope restrictions using the
     *            {@link java.lang.reflect.AccessibleObject#setAccessible(boolean)} method. {@code false} will only
     *            match {@code public} fields.
     * @throws NullPointerException
     *              if the field is {@code null}
     * @throws IllegalArgumentException
     *              if the field is not {@code static}, or {@code value} is not assignable
     * @throws IllegalAccessException
     *             if the field is not made accessible or is {@code final}
     */
    public static void writeStaticField(final Field field, final Object value, final boolean forceAccess) throws IllegalAccessException {
        // TODO
    }

    /**
     * {@link FieldUtils} instances should NOT be constructed in standard programming.
     * <p>
     * This constructor is {@code public} to permit tools that require a JavaBean instance to operate.
     * </p>
     *
     * @deprecated TODO Make private in 4.0.
     */
    @Deprecated
    public FieldUtils() {
        // TODO
    }
}
