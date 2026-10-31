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

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.AppendableJoiner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

/**
 * Utility methods focusing on type inspection, particularly with regard to generics.
 *
 * @since 3.0
 */
public class TypeUtils {

    /**
     * GenericArrayType implementation class.
     */
    private static final class GenericArrayTypeImpl implements GenericArrayType {

        private final Type componentType;

        /**
         * Constructor
         *
         * @param componentType of this array type
         */
        private GenericArrayTypeImpl(final Type componentType) {
            this.componentType = componentType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof GenericArrayType && TypeUtils.equals(this, (GenericArrayType) obj);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type getGenericComponentType() {
            return componentType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            int result = 67 << 4;
            result |= componentType.hashCode();
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    /**
     * ParameterizedType implementation class.
     */
    private static final class ParameterizedTypeImpl implements ParameterizedType {

        private final Class<?> raw;

        private final Type useOwner;

        private final Type[] typeArguments;

        /**
         * Constructor
         *
         * @param rawClass      type
         * @param useOwner      owner type to use, if any
         * @param typeArguments formal type arguments
         */
        private ParameterizedTypeImpl(final Class<?> rawClass, final Type useOwner, final Type[] typeArguments) {
            this.raw = rawClass;
            this.useOwner = useOwner;
            this.typeArguments = Arrays.copyOf(typeArguments, typeArguments.length, Type[].class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof ParameterizedType && TypeUtils.equals(this, (ParameterizedType) obj);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type[] getActualTypeArguments() {
            return typeArguments.clone();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type getOwnerType() {
            return useOwner;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type getRawType() {
            return raw;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            int result = 71 << 4;
            result |= raw.hashCode();
            result <<= 4;
            result |= Objects.hashCode(useOwner);
            result <<= 8;
            result |= Arrays.hashCode(typeArguments);
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    /**
     * {@link WildcardType} builder.
     *
     * @since 3.2
     */
    public static class WildcardTypeBuilder implements Builder<WildcardType> {

        private Type[] upperBounds;

        private Type[] lowerBounds;

        /**
         * Constructor
         */
        private WildcardTypeBuilder() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WildcardType build() {
            // TODO
            return null;
        }

        /**
         * Specify lower bounds of the wildcard type to build.
         *
         * @param bounds to set
         * @return {@code this}
         */
        public WildcardTypeBuilder withLowerBounds(final Type... bounds) {
            // TODO
            return null;
        }

        /**
         * Specify upper bounds of the wildcard type to build.
         *
         * @param bounds to set
         * @return {@code this}
         */
        public WildcardTypeBuilder withUpperBounds(final Type... bounds) {
            // TODO
            return null;
        }
    }

    /**
     * WildcardType implementation class.
     */
    private static final class WildcardTypeImpl implements WildcardType {

        private final Type[] upperBounds;

        private final Type[] lowerBounds;

        /**
         * Constructor
         *
         * @param upperBounds of this type
         * @param lowerBounds of this type
         */
        private WildcardTypeImpl(final Type[] upperBounds, final Type[] lowerBounds) {
            this.upperBounds = ObjectUtils.defaultIfNull(upperBounds, ArrayUtils.EMPTY_TYPE_ARRAY);
            this.lowerBounds = ObjectUtils.defaultIfNull(lowerBounds, ArrayUtils.EMPTY_TYPE_ARRAY);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof WildcardType && TypeUtils.equals(this, (WildcardType) obj);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type[] getLowerBounds() {
            return lowerBounds.clone();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Type[] getUpperBounds() {
            return upperBounds.clone();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            int result = 73 << 8;
            result |= Arrays.hashCode(upperBounds);
            result <<= 8;
            result |= Arrays.hashCode(lowerBounds);
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
    }

    /**
     * Ampersand sign joiner.
     */
    // @formatter:off
    private static final AppendableJoiner<Type> AMP_JOINER = AppendableJoiner.<Type>builder().setDelimiter(" & ").setElementAppender((a, e) -> a.append(TypeUtils.toString(e))).get();

    // @formatter:on
    /**
     * Method classToString joiner.
     */
    // @formatter:off
    private static final AppendableJoiner<TypeVariable<Class<?>>> CTJ_JOINER = AppendableJoiner.<TypeVariable<Class<?>>>builder().setDelimiter(", ").setElementAppender((a, e) -> a.append(TypeUtils.anyToString(e))).get();

    // @formatter:on
    /**
     * Greater than and lesser than sign joiner.
     */
    // @formatter:off
    private static final AppendableJoiner<Object> GT_JOINER = AppendableJoiner.builder().setPrefix("<").setSuffix(">").setDelimiter(", ").setElementAppender((a, e) -> a.append(TypeUtils.anyToString(e))).get();

    // @formatter:on
    /**
     * A wildcard instance matching {@code ?}.
     *
     * @since 3.2
     */
    public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(Object.class).build();

    private static <T> String anyToString(final T object) {
        return object instanceof Type ? toString((Type) object) : object.toString();
    }

    private static void appendRecursiveTypes(final StringBuilder builder, final int[] recursiveTypeIndexes, final Type[] argumentTypes) {
        for (int i = 0; i < recursiveTypeIndexes.length; i++) {
            // toString() or SO
            GT_JOINER.join(builder, argumentTypes[i].toString());
        }
        final Type[] argumentsFiltered = ArrayUtils.removeAll(argumentTypes, recursiveTypeIndexes);
        if (argumentsFiltered.length > 0) {
            GT_JOINER.join(builder, (Object[]) argumentsFiltered);
        }
    }

    /**
     * Formats a {@link Class} as a {@link String}.
     *
     * @param cls {@link Class} to format
     * @return String
     */
    private static <T> String classToString(final Class<T> cls) {
        if (cls.isArray()) {
            return toString(cls.getComponentType()) + "[]";
        }
        if (isCyclical(cls)) {
            return cls.getSimpleName() + "(cycle)";
        }
        final StringBuilder buf = new StringBuilder();
        if (cls.getEnclosingClass() != null) {
            buf.append(classToString(cls.getEnclosingClass())).append('.').append(cls.getSimpleName());
        } else {
            buf.append(cls.getName());
        }
        if (cls.getTypeParameters().length > 0) {
            // AppendableJoiner.joinSB(buf, null, null, ", ", TypeUtils::anyToString, cls.getTypeParameters());
            CTJ_JOINER.join(buf, (TypeVariable[]) cls.getTypeParameters());
        }
        return buf.toString();
    }

    /**
     * Tests, recursively, whether any of the type parameters associated with {@code type} are bound to variables.
     *
     * @param type the type to check for type variables
     * @return boolean
     * @since 3.2
     */
    public static boolean containsTypeVariables(final Type type) {
        // TODO
        return false;
    }

    private static boolean containsVariableTypeSameParametrizedTypeBound(final TypeVariable<?> typeVariable, final ParameterizedType parameterizedType) {
        return ArrayUtils.contains(typeVariable.getBounds(), parameterizedType);
    }

    /**
     * Tries to determine the type arguments of a class/interface based on a super parameterized type's type arguments. This method is the inverse of
     * {@link #getTypeArguments(Type, Class)} which gets a class/interface's type arguments based on a subtype. It is far more limited in determining the type
     * arguments for the subject class's type variables in that it can only determine those parameters that map from the subject {@link Class} object to the
     * supertype.
     *
     * <p>
     * Example: {@link java.util.TreeSet TreeSet} sets its parameter as the parameter for {@link java.util.NavigableSet NavigableSet}, which in turn sets the
     * parameter of {@link java.util.SortedSet}, which in turn sets the parameter of {@link Set}, which in turn sets the parameter of
     * {@link java.util.Collection}, which in turn sets the parameter of {@link Iterable}. Since {@link TreeSet}'s parameter maps (indirectly) to
     * {@link Iterable}'s parameter, it will be able to determine that based on the super type {@code Iterable<? extends
     * Map<Integer, ? extends Collection<?>>>}, the parameter of {@link TreeSet} is {@code ? extends Map<Integer, ? extends
     * Collection<?>>}.
     * </p>
     *
     * @param cls                    the class whose type parameters are to be determined, not {@code null}
     * @param superParameterizedType the super type from which {@code cls}'s type arguments are to be determined, not {@code null}
     * @return a {@link Map} of the type assignments that could be determined for the type variables in each type in the inheritance hierarchy from {@code type}
     *         to {@code toClass} inclusive.
     * @throws NullPointerException if either {@code cls} or {@code superParameterizedType} is {@code null}
     */
    public static Map<TypeVariable<?>, Type> determineTypeArguments(final Class<?> cls, final ParameterizedType superParameterizedType) {
        // TODO
        return null;
    }

    /**
     * Tests whether {@code t} equals {@code a}.
     *
     * @param genericArrayType LHS
     * @param type             RHS
     * @return boolean
     */
    private static boolean equals(final GenericArrayType genericArrayType, final Type type) {
        // TODO
        return false;
    }

    /**
     * Tests whether {@code t} equals {@code p}.
     *
     * @param parameterizedType LHS
     * @param type              RHS
     * @return boolean
     */
    private static boolean equals(final ParameterizedType parameterizedType, final Type type) {
        // TODO
        return false;
    }

    /**
     * Tests equality of types.
     *
     * @param type1 the first type
     * @param type2 the second type
     * @return boolean
     * @since 3.2
     */
    public static boolean equals(final Type type1, final Type type2) {
        // TODO
        return false;
    }

    /**
     * Tests whether {@code t1} equals {@code t2}.
     *
     * @param type1 LHS
     * @param type2 RHS
     * @return boolean
     */
    private static boolean equals(final Type[] type1, final Type[] type2) {
        // TODO
        return false;
    }

    /**
     * Tests whether {@code t} equals {@code w}.
     *
     * @param wildcardType LHS
     * @param type         RHS
     * @return boolean
     */
    private static boolean equals(final WildcardType wildcardType, final Type type) {
        // TODO
        return false;
    }

    /**
     * Helper method to establish the formal parameters for a parameterized type.
     *
     * @param mappings  map containing the assignments
     * @param variables expected map keys
     * @return array of map values corresponding to specified keys
     */
    private static Type[] extractTypeArgumentsFrom(final Map<TypeVariable<?>, Type> mappings, final TypeVariable<?>[] variables) {
        final Type[] result = new Type[variables.length];
        int index = 0;
        for (final TypeVariable<?> var : variables) {
            Validate.isTrue(mappings.containsKey(var), "missing argument mapping for %s", toString(var));
            result[index++] = mappings.get(var);
        }
        return result;
    }

    private static int[] findRecursiveTypes(final ParameterizedType parameterizedType) {
        final Type[] filteredArgumentTypes = Arrays.copyOf(parameterizedType.getActualTypeArguments(), parameterizedType.getActualTypeArguments().length);
        int[] indexesToRemove = {};
        for (int i = 0; i < filteredArgumentTypes.length; i++) {
            if (filteredArgumentTypes[i] instanceof TypeVariable<?> && containsVariableTypeSameParametrizedTypeBound((TypeVariable<?>) filteredArgumentTypes[i], parameterizedType)) {
                indexesToRemove = ArrayUtils.add(indexesToRemove, i);
            }
        }
        return indexesToRemove;
    }

    /**
     * Creates a generic array type instance.
     *
     * @param componentType the type of the elements of the array. For example the component type of {@code boolean[]} is {@code boolean}
     * @return {@link GenericArrayType}
     * @since 3.2
     */
    public static GenericArrayType genericArrayType(final Type componentType) {
        // TODO
        return null;
    }

    /**
     * Formats a {@link GenericArrayType} as a {@link String}.
     *
     * @param genericArrayType {@link GenericArrayType} to format
     * @return String
     */
    private static String genericArrayTypeToString(final GenericArrayType genericArrayType) {
        return String.format("%s[]", toString(genericArrayType.getGenericComponentType()));
    }

    /**
     * Gets the array component type of {@code type}.
     *
     * @param type the type to be checked
     * @return component type or null if type is not an array type
     */
    public static Type getArrayComponentType(final Type type) {
        // TODO
        return null;
    }

    /**
     * Gets the closest parent type to the super class specified by {@code superClass}.
     *
     * @param cls        the class in question
     * @param superClass the super class
     * @return the closes parent type
     */
    private static Type getClosestParentType(final Class<?> cls, final Class<?> superClass) {
        // only look at the interfaces if the super class is also an interface
        if (superClass.isInterface()) {
            // get the generic interfaces of the subject class
            final Type[] interfaceTypes = cls.getGenericInterfaces();
            // will hold the best generic interface match found
            Type genericInterface = null;
            // find the interface closest to the super class
            for (final Type midType : interfaceTypes) {
                final Class<?> midClass;
                if (midType instanceof ParameterizedType) {
                    midClass = getRawType((ParameterizedType) midType);
                } else if (midType instanceof Class<?>) {
                    midClass = (Class<?>) midType;
                } else {
                    throw new IllegalStateException("Unexpected generic" + " interface type found: " + midType);
                }
                // check if this interface is further up the inheritance chain
                // than the previously found match
                if (isAssignable(midClass, superClass) && isAssignable(genericInterface, (Type) midClass)) {
                    genericInterface = midType;
                }
            }
            // found a match?
            if (genericInterface != null) {
                return genericInterface;
            }
        }
        // none of the interfaces were descendants of the target class, so the
        // super class has to be one, instead
        return cls.getGenericSuperclass();
    }

    /**
     * Gets an array containing the sole type of {@link Object} if {@link TypeVariable#getBounds()} returns an empty array. Otherwise, it returns the result of
     * {@link TypeVariable#getBounds()} passed into {@link #normalizeUpperBounds}.
     *
     * @param typeVariable the subject type variable, not {@code null}
     * @return a non-empty array containing the bounds of the type variable.
     * @throws NullPointerException if {@code typeVariable} is {@code null}
     */
    public static Type[] getImplicitBounds(final TypeVariable<?> typeVariable) {
        Objects.requireNonNull(typeVariable, "typeVariable");
        final Type[] bounds = typeVariable.getBounds();
        return bounds.length == 0 ? new Type[] { Object.class } : normalizeUpperBounds(bounds);
    }

    /**
     * Gets an array containing a single value of {@code null} if {@link WildcardType#getLowerBounds()} returns an empty array. Otherwise, it returns the result
     * of {@link WildcardType#getLowerBounds()}.
     *
     * @param wildcardType the subject wildcard type, not {@code null}
     * @return a non-empty array containing the lower bounds of the wildcard type.
     * @throws NullPointerException if {@code wildcardType} is {@code null}
     */
    public static Type[] getImplicitLowerBounds(final WildcardType wildcardType) {
        // TODO
        return null;
    }

    /**
     * Gets an array containing the sole value of {@link Object} if {@link WildcardType#getUpperBounds()} returns an empty array. Otherwise, it returns the
     * result of {@link WildcardType#getUpperBounds()} passed into {@link #normalizeUpperBounds}.
     *
     * @param wildcardType the subject wildcard type, not {@code null}
     * @return a non-empty array containing the upper bounds of the wildcard type.
     * @throws NullPointerException if {@code wildcardType} is {@code null}
     */
    public static Type[] getImplicitUpperBounds(final WildcardType wildcardType) {
        // TODO
        return null;
    }

    /**
     * Transforms the passed in type to a {@link Class} object. Type-checking method of convenience.
     *
     * @param parameterizedType the type to be converted
     * @return the corresponding {@link Class} object
     * @throws IllegalStateException if the conversion fails
     */
    private static Class<?> getRawType(final ParameterizedType parameterizedType) {
        // TODO
        return null;
    }

    /**
     * Gets the raw type of a Java type, given its context. Primarily for use with {@link TypeVariable}s and {@link GenericArrayType}s, or when you do not know
     * the runtime type of {@code type}: if you know you have a {@link Class} instance, it is already raw; if you know you have a {@link ParameterizedType}, its
     * raw type is only a method call away.
     *
     * @param type          to resolve
     * @param assigningType type to be resolved against
     * @return the resolved {@link Class} object or {@code null} if the type could not be resolved
     */
    public static Class<?> getRawType(final Type type, final Type assigningType) {
        // TODO
        return null;
    }

    /**
     * Gets a map of the type arguments of a class in the context of {@code toClass}.
     *
     * @param cls               the class in question
     * @param toClass           the context class
     * @param subtypeVarAssigns a map with type variables
     * @return the {@link Map} with type arguments
     */
    private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        // TODO
        return null;
    }

    /**
     * Gets all the type arguments for this parameterized type including owner hierarchy arguments such as {@code Outer<K, V>.Inner<T>.DeepInner<E>} . The
     * arguments are returned in a {@link Map} specifying the argument type for each {@link TypeVariable}.
     *
     * @param type specifies the subject parameterized type from which to harvest the parameters.
     * @return a {@link Map} of the type arguments to their respective type variables.
     */
    public static Map<TypeVariable<?>, Type> getTypeArguments(final ParameterizedType type) {
        // TODO
        return null;
    }

    /**
     * Gets a map of the type arguments of a parameterized type in the context of {@code toClass}.
     *
     * @param parameterizedType the parameterized type
     * @param toClass           the class
     * @param subtypeVarAssigns a map with type variables
     * @return the {@link Map} with type arguments
     */
    private static Map<TypeVariable<?>, Type> getTypeArguments(final ParameterizedType parameterizedType, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        // TODO
        return null;
    }

    /**
     * Gets the type arguments of a class/interface based on a subtype. For instance, this method will determine that both of the parameters for the interface
     * {@link Map} are {@link Object} for the subtype {@link java.util.Properties Properties} even though the subtype does not directly implement the
     * {@link Map} interface.
     *
     * <p>
     * This method returns {@code null} if {@code type} is not assignable to {@code toClass}. It returns an empty map if none of the classes or interfaces in
     * its inheritance hierarchy specify any type arguments.
     * </p>
     *
     * <p>
     * A side effect of this method is that it also retrieves the type arguments for the classes and interfaces that are part of the hierarchy between
     * {@code type} and {@code toClass}. So with the above example, this method will also determine that the type arguments for {@link java.util.Hashtable
     * Hashtable} are also both {@link Object}. In cases where the interface specified by {@code toClass} is (indirectly) implemented more than once (e.g. where
     * {@code toClass} specifies the interface {@link Iterable Iterable} and {@code type} specifies a parameterized type that implements both
     * {@link java.util.Set Set} and {@link java.util.Collection Collection}), this method will look at the inheritance hierarchy of only one of the
     * implementations/subclasses; the first interface encountered that isn't a subinterface to one of the others in the {@code type} to {@code toClass}
     * hierarchy.
     * </p>
     *
     * @param type    the type from which to determine the type parameters of {@code toClass}
     * @param toClass the class whose type parameters are to be determined based on the subtype {@code type}
     * @return a {@link Map} of the type assignments for the type variables in each type in the inheritance hierarchy from {@code type} to {@code toClass}
     *         inclusive.
     */
    public static Map<TypeVariable<?>, Type> getTypeArguments(final Type type, final Class<?> toClass) {
        // TODO
        return null;
    }

    /**
     * Gets a map of the type arguments of {@code type} in the context of {@code toClass}.
     *
     * @param type              the type in question
     * @param toClass           the class
     * @param subtypeVarAssigns a map with type variables
     * @return the {@link Map} with type arguments
     */
    private static Map<TypeVariable<?>, Type> getTypeArguments(final Type type, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        // TODO
        return null;
    }

    /**
     * Tests whether the specified type denotes an array type.
     *
     * @param type the type to be checked
     * @return {@code true} if {@code type} is an array class or a {@link GenericArrayType}.
     */
    public static boolean isArrayType(final Type type) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target class following the Java generics rules.
     *
     * @param type    the subject type to be assigned to the target type
     * @param toClass the target class
     * @return {@code true} if {@code type} is assignable to {@code toClass}.
     */
    private static boolean isAssignable(final Type type, final Class<?> toClass) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target generic array type following the Java generics rules.
     *
     * @param type               the subject type to be assigned to the target type
     * @param toGenericArrayType the target generic array type
     * @param typeVarAssigns     a map with type variables
     * @return {@code true} if {@code type} is assignable to {@code toGenericArrayType}.
     */
    private static boolean isAssignable(final Type type, final GenericArrayType toGenericArrayType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target parameterized type following the Java generics rules.
     *
     * @param type                the subject type to be assigned to the target type
     * @param toParameterizedType the target parameterized type
     * @param typeVarAssigns      a map with type variables
     * @return {@code true} if {@code type} is assignable to {@code toType}.
     */
    private static boolean isAssignable(final Type type, final ParameterizedType toParameterizedType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target type following the Java generics rules. If both types are {@link Class} objects, the
     * method returns the result of {@link ClassUtils#isAssignable(Class, Class)}.
     *
     * @param type   the subject type to be assigned to the target type
     * @param toType the target type
     * @return {@code true} if {@code type} is assignable to {@code toType}.
     */
    public static boolean isAssignable(final Type type, final Type toType) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target type following the Java generics rules.
     *
     * @param type           the subject type to be assigned to the target type
     * @param toType         the target type
     * @param typeVarAssigns optional map of type variable assignments
     * @return {@code true} if {@code type} is assignable to {@code toType}.
     */
    private static boolean isAssignable(final Type type, final Type toType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target type variable following the Java generics rules.
     *
     * @param type           the subject type to be assigned to the target type
     * @param toTypeVariable the target type variable
     * @param typeVarAssigns a map with type variables
     * @return {@code true} if {@code type} is assignable to {@code toTypeVariable}.
     */
    private static boolean isAssignable(final Type type, final TypeVariable<?> toTypeVariable, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        // TODO
        return false;
    }

    /**
     * Tests if the subject type may be implicitly cast to the target wildcard type following the Java generics rules.
     *
     * @param type           the subject type to be assigned to the target type
     * @param toWildcardType the target wildcard type
     * @param typeVarAssigns a map with type variables
     * @return {@code true} if {@code type} is assignable to {@code toWildcardType}.
     */
    private static boolean isAssignable(final Type type, final WildcardType toWildcardType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        // TODO
        return false;
    }

    /**
     * Tests whether the class contains a cyclical reference in the qualified name of a class. If any of the type parameters of A class is extending X class
     * which is in scope of A class, then it forms cycle.
     *
     * @param cls the class to test.
     * @return whether the class contains a cyclical reference.
     */
    private static boolean isCyclical(final Class<?> cls) {
        for (final TypeVariable<?> typeParameter : cls.getTypeParameters()) {
            for (final AnnotatedType annotatedBound : typeParameter.getAnnotatedBounds()) {
                if (annotatedBound.getType().getTypeName().contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tests if the given value can be assigned to the target type following the Java generics rules.
     *
     * @param value the value to be checked
     * @param type  the target type
     * @return {@code true} if {@code value} is an instance of {@code type}.
     */
    public static boolean isInstance(final Object value, final Type type) {
        // TODO
        return false;
    }

    /**
     * Maps type variables.
     *
     * @param <T>               the generic type of the class in question
     * @param cls               the class in question
     * @param parameterizedType the parameterized type
     * @param typeVarAssigns    the map to be filled
     */
    private static <T> void mapTypeVariablesToArguments(final Class<T> cls, final ParameterizedType parameterizedType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        // capture the type variables from the owner type that have assignments
        final Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            // recursion to make sure the owner's owner type gets processed
            mapTypeVariablesToArguments(cls, (ParameterizedType) ownerType, typeVarAssigns);
        }
        // parameterizedType is a generic interface/class (or it's in the owner
        // hierarchy of said interface/class) implemented/extended by the class
        // cls. Find out which type variables of cls are type arguments of
        // parameterizedType:
        final Type[] typeArgs = parameterizedType.getActualTypeArguments();
        // of the cls's type variables that are arguments of parameterizedType,
        // find out which ones can be determined from the super type's arguments
        final TypeVariable<?>[] typeVars = getRawType(parameterizedType).getTypeParameters();
        // use List view of type parameters of cls so the contains() method can be used:
        final List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls.getTypeParameters());
        for (int i = 0; i < typeArgs.length; i++) {
            final TypeVariable<?> typeVar = typeVars[i];
            final Type typeArg = typeArgs[i];
            // argument of parameterizedType is a type variable of cls
            if (typeVarList.contains(typeArg) && // type variable of parameterizedType has an assignment in
            // the super type.
            typeVarAssigns.containsKey(typeVar)) {
                // map the assignment to the cls's type variable
                typeVarAssigns.put((TypeVariable<?>) typeArg, typeVarAssigns.get(typeVar));
            }
        }
    }

    /**
     * Strips out the redundant upper bound types in type variable types and wildcard types (or it would with wildcard types if multiple upper bounds were
     * allowed).
     *
     * <p>
     * Example, with the variable type declaration:
     * </p>
     *
     * <pre>{@code
     * <K extends java.util.Collection<String> & java.util.List<String>>
     * }</pre>
     *
     * <p>
     * since {@link List} is a subinterface of {@link Collection}, this method will return the bounds as if the declaration had been:
     * </p>
     *
     * <pre>{@code
     * <K extends java.util.List<String>>
     * }</pre>
     *
     * @param bounds an array of types representing the upper bounds of either {@link WildcardType} or {@link TypeVariable}, not {@code null}.
     * @return an array containing the values from {@code bounds} minus the redundant types.
     * @throws NullPointerException if {@code bounds} is {@code null}
     */
    public static Type[] normalizeUpperBounds(final Type[] bounds) {
        // TODO
        return null;
    }

    /**
     * Creates a parameterized type instance.
     *
     * @param rawClass        the raw class to create a parameterized type instance for
     * @param typeVariableMap the map used for parameterization
     * @return {@link ParameterizedType}
     * @throws NullPointerException if either {@code rawClass} or {@code typeVariableMap} is {@code null}
     * @since 3.2
     */
    public static final ParameterizedType parameterize(final Class<?> rawClass, final Map<TypeVariable<?>, Type> typeVariableMap) {
        // TODO
        return null;
    }

    /**
     * Creates a parameterized type instance.
     *
     * @param rawClass      the raw class to create a parameterized type instance for
     * @param typeArguments the types used for parameterization
     * @return {@link ParameterizedType}
     * @throws NullPointerException if {@code rawClass} is {@code null}
     * @since 3.2
     */
    public static final ParameterizedType parameterize(final Class<?> rawClass, final Type... typeArguments) {
        // TODO
        return null;
    }

    /**
     * Formats a {@link ParameterizedType} as a {@link String}.
     *
     * @param parameterizedType {@link ParameterizedType} to format
     * @return String
     */
    private static String parameterizedTypeToString(final ParameterizedType parameterizedType) {
        final StringBuilder builder = new StringBuilder();
        final Type useOwner = parameterizedType.getOwnerType();
        final Class<?> raw = (Class<?>) parameterizedType.getRawType();
        if (useOwner == null) {
            builder.append(raw.getName());
        } else {
            if (useOwner instanceof Class<?>) {
                builder.append(((Class<?>) useOwner).getName());
            } else {
                builder.append(useOwner);
            }
            builder.append('.').append(raw.getSimpleName());
        }
        final int[] recursiveTypeIndexes = findRecursiveTypes(parameterizedType);
        if (recursiveTypeIndexes.length > 0) {
            appendRecursiveTypes(builder, recursiveTypeIndexes, parameterizedType.getActualTypeArguments());
        } else {
            GT_JOINER.join(builder, parameterizedType.getActualTypeArguments());
        }
        return builder.toString();
    }

    /**
     * Creates a parameterized type instance.
     *
     * @param owner           the owning type
     * @param rawClass        the raw class to create a parameterized type instance for
     * @param typeVariableMap the map used for parameterization
     * @return {@link ParameterizedType}
     * @throws NullPointerException if either {@code rawClass} or {@code typeVariableMap} is {@code null}
     * @since 3.2
     */
    public static final ParameterizedType parameterizeWithOwner(final Type owner, final Class<?> rawClass, final Map<TypeVariable<?>, Type> typeVariableMap) {
        // TODO
        return null;
    }

    /**
     * Creates a parameterized type instance.
     *
     * @param owner         the owning type
     * @param rawClass      the raw class to create a parameterized type instance for
     * @param typeArguments the types used for parameterization
     *
     * @return {@link ParameterizedType}
     * @throws NullPointerException if {@code rawClass} is {@code null}
     * @since 3.2
     */
    public static final ParameterizedType parameterizeWithOwner(final Type owner, final Class<?> rawClass, final Type... typeArguments) {
        // TODO
        return null;
    }

    /**
     * Finds the mapping for {@code type} in {@code typeVarAssigns}.
     *
     * @param type           the type to be replaced
     * @param typeVarAssigns the map with type variables
     * @return the replaced type
     * @throws IllegalArgumentException if the type cannot be substituted
     */
    private static Type substituteTypeVariables(final Type type, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type instanceof TypeVariable<?> && typeVarAssigns != null) {
            final Type replacementType = typeVarAssigns.get(type);
            if (replacementType == null) {
                throw new IllegalArgumentException("missing assignment type for type variable " + type);
            }
            return replacementType;
        }
        return type;
    }

    /**
     * Formats a {@link TypeVariable} including its {@link GenericDeclaration}.
     *
     * @param typeVariable the type variable to create a String representation for, not {@code null}
     * @return String
     * @throws NullPointerException if {@code typeVariable} is {@code null}
     * @since 3.2
     */
    public static String toLongString(final TypeVariable<?> typeVariable) {
        // TODO
        return null;
    }

    /**
     * Formats a given type as a Java-esque String.
     *
     * @param type the type to create a String representation for, not {@code null}
     * @return String
     * @throws NullPointerException if {@code type} is {@code null}
     * @since 3.2
     */
    public static String toString(final Type type) {
        // TODO
        return null;
    }

    /**
     * Determines whether or not specified types satisfy the bounds of their mapped type variables. When a type parameter extends another (such as
     * {@code <T, S extends T>}), uses another as a type parameter (such as {@code <T, S extends Comparable>>}), or otherwise depends on another type variable
     * to be specified, the dependencies must be included in {@code typeVarAssigns}.
     *
     * @param typeVariableMap specifies the potential types to be assigned to the type variables, not {@code null}.
     * @return whether or not the types can be assigned to their respective type variables.
     * @throws NullPointerException if {@code typeVariableMap} is {@code null}
     */
    public static boolean typesSatisfyVariables(final Map<TypeVariable<?>, Type> typeVariableMap) {
        // TODO
        return false;
    }

    /**
     * Formats a {@link TypeVariable} as a {@link String}.
     *
     * @param typeVariable {@link TypeVariable} to format
     * @return String
     */
    private static String typeVariableToString(final TypeVariable<?> typeVariable) {
        final StringBuilder builder = new StringBuilder(typeVariable.getName());
        final Type[] bounds = typeVariable.getBounds();
        if (bounds.length > 0 && !(bounds.length == 1 && Object.class.equals(bounds[0]))) {
            builder.append(" extends ");
            AMP_JOINER.join(builder, typeVariable.getBounds());
        }
        return builder.toString();
    }

    /**
     * Unrolls variables in a type bounds array.
     *
     * @param typeArguments assignments {@link Map}
     * @param bounds        in which to expand variables
     * @return {@code bounds} with any variables reassigned
     */
    private static Type[] unrollBounds(final Map<TypeVariable<?>, Type> typeArguments, final Type[] bounds) {
        Type[] result = bounds;
        int i = 0;
        for (; i < result.length; i++) {
            final Type unrolled = unrollVariables(typeArguments, result[i]);
            if (unrolled == null) {
                result = ArrayUtils.remove(result, i--);
            } else {
                result[i] = unrolled;
            }
        }
        return result;
    }

    /**
     * Looks up {@code typeVariable} in {@code typeVarAssigns} <em>transitively</em>, i.e. keep looking until the value found is <em>not</em> a type variable.
     *
     * @param typeVariable   the type variable to look up
     * @param typeVarAssigns the map used for the look-up
     * @return Type or {@code null} if some variable was not in the map
     */
    private static Type unrollVariableAssignments(TypeVariable<?> typeVariable, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        Type result;
        do {
            result = typeVarAssigns.get(typeVariable);
            if (!(result instanceof TypeVariable<?>) || result.equals(typeVariable)) {
                break;
            }
            typeVariable = (TypeVariable<?>) result;
        } while (true);
        return result;
    }

    /**
     * Gets a type representing {@code type} with variable assignments "unrolled."
     *
     * @param typeArguments as from {@link TypeUtils#getTypeArguments(Type, Class)}
     * @param type          the type to unroll variable assignments for
     * @return Type
     * @since 3.2
     */
    public static Type unrollVariables(Map<TypeVariable<?>, Type> typeArguments, final Type type) {
        // TODO
        return null;
    }

    /**
     * Gets a {@link WildcardTypeBuilder}.
     *
     * @return {@link WildcardTypeBuilder}
     * @since 3.2
     */
    public static WildcardTypeBuilder wildcardType() {
        // TODO
        return null;
    }

    /**
     * Formats a {@link WildcardType} as a {@link String}.
     *
     * @param wildcardType {@link WildcardType} to format
     * @return String
     */
    private static String wildcardTypeToString(final WildcardType wildcardType) {
        final StringBuilder builder = new StringBuilder().append('?');
        final Type[] lowerBounds = wildcardType.getLowerBounds();
        final Type[] upperBounds = wildcardType.getUpperBounds();
        if (lowerBounds.length > 1 || lowerBounds.length == 1 && lowerBounds[0] != null) {
            AMP_JOINER.join(builder.append(" super "), lowerBounds);
        } else if (upperBounds.length > 1 || upperBounds.length == 1 && !Object.class.equals(upperBounds[0])) {
            AMP_JOINER.join(builder.append(" extends "), upperBounds);
        }
        return builder.toString();
    }

    /**
     * Wraps the specified {@link Class} in a {@link Typed} wrapper.
     *
     * @param <T>  generic type
     * @param type to wrap
     * @return {@code Typed<T>}
     * @since 3.2
     */
    public static <T> Typed<T> wrap(final Class<T> type) {
        // TODO
        return null;
    }

    /**
     * Wraps the specified {@link Type} in a {@link Typed} wrapper.
     *
     * @param <T>  inferred generic type
     * @param type to wrap
     * @return {@code Typed<T>}
     * @since 3.2
     */
    public static <T> Typed<T> wrap(final Type type) {
        // TODO
        return null;
    }

    /**
     * {@link TypeUtils} instances should NOT be constructed in standard programming. Instead, the class should be used as
     * {@code TypeUtils.isAssignable(cls, toClass)}.
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance to operate.
     * </p>
     *
     * @deprecated TODO Make private in 4.0.
     */
    @Deprecated
    public TypeUtils() {
        // empty
    }
}
