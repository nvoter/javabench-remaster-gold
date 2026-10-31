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
package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

/**
 * Operations to assist when working with a {@link Locale}.
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behavior in more detail.</p>
 *
 * @since 2.2
 */
public class LocaleUtils {

    // class to avoid synchronization (Init on demand)
    static class SyncAvoid {

        /**
         * Unmodifiable list of available locales.
         */
        private static final List<Locale> AVAILABLE_LOCALE_LIST;

        /**
         * Unmodifiable set of available locales.
         */
        private static final Set<Locale> AVAILABLE_LOCALE_SET;

        static {
            // extra safe
            final List<Locale> list = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<>(list));
        }
    }

    /**
     * The underscore character {@code '}{@value}{@code '}.
     */
    private static final char UNDERSCORE = '_';

    /**
     * The undetermined language {@value}.
     */
    private static final String UNDETERMINED = "und";

    /**
     * The dash character {@code '}{@value}{@code '}.
     */
    private static final char DASH = '-';

    /**
     * Concurrent map of language locales by country.
     */
    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<>();

    /**
     * Concurrent map of country locales by language.
     */
    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<>();

    /**
     * Obtains an unmodifiable list of installed locales.
     *
     * <p>This method is a wrapper around {@link Locale#getAvailableLocales()}.
     * It is more efficient, as the JDK method must create a new array each
     * time it is called.</p>
     *
     * @return the unmodifiable list of available locales
     */
    public static List<Locale> availableLocaleList() {
        // TODO
        return null;
    }

    private static List<Locale> availableLocaleList(final Predicate<Locale> predicate) {
        // TODO
        return null;
    }

    /**
     * Obtains an unmodifiable set of installed locales.
     *
     * <p>This method is a wrapper around {@link Locale#getAvailableLocales()}.
     * It is more efficient, as the JDK method must create a new array each
     * time it is called.</p>
     *
     * @return the unmodifiable set of available locales
     */
    public static Set<Locale> availableLocaleSet() {
        // TODO
        return null;
    }

    /**
     * Obtains the list of countries supported for a given language.
     *
     * <p>This method takes a language code and searches to find the
     * countries available for that language. Variant locales are removed.</p>
     *
     * @param languageCode  the 2 letter language code, null returns empty
     * @return an unmodifiable List of Locale objects, not null
     */
    public static List<Locale> countriesByLanguage(final String languageCode) {
        // TODO
        return null;
    }

    /**
     * Checks if the locale specified is in the set of available locales.
     *
     * @param locale the Locale object to check if it is available
     * @return true if the locale is a known locale
     */
    public static boolean isAvailableLocale(final Locale locale) {
        // TODO
        return false;
    }

    /**
     * Checks whether the given String is a ISO 3166 alpha-2 country code.
     *
     * @param str the String to check
     * @return true, is the given String is a ISO 3166 compliant country code.
     */
    private static boolean isISO3166CountryCode(final String str) {
        return StringUtils.isAllUpperCase(str) && str.length() == 2;
    }

    /**
     * Checks whether the given String is a ISO 639 compliant language code.
     *
     * @param str the String to check.
     * @return true, if the given String is a ISO 639 compliant language code.
     */
    private static boolean isISO639LanguageCode(final String str) {
        return StringUtils.isAllLowerCase(str) && (str.length() == 2 || str.length() == 3);
    }

    /**
     * Tests whether a Locale's language is undetermined.
     * <p>
     * A Locale's language tag is undetermined if it's value is {@code "und"}. If a language is empty, or not well-formed (for example, "a" or"e2"), it will be
     * equal to {@code "und"}.
     * </p>
     *
     * @param locale the locale to test.
     * @return whether a Locale's language is undetermined.
     * @see Locale#toLanguageTag()
     * @since 3.14.0
     */
    public static boolean isLanguageUndetermined(final Locale locale) {
        // TODO
        return false;
    }

    /**
     * Checks whether the given String is a UN M.49 numeric area code.
     *
     * @param str the String to check
     * @return true, is the given String is a UN M.49 numeric area code.
     */
    private static boolean isNumericAreaCode(final String str) {
        return StringUtils.isNumeric(str) && str.length() == 3;
    }

    /**
     * Obtains the list of languages supported for a given country.
     *
     * <p>This method takes a country code and searches to find the
     * languages available for that country. Variant locales are removed.</p>
     *
     * @param countryCode  the 2-letter country code, null returns empty
     * @return an unmodifiable List of Locale objects, not null
     */
    public static List<Locale> languagesByCountry(final String countryCode) {
        // TODO
        return null;
    }

    /**
     * Obtains the list of locales to search through when performing
     * a locale search.
     *
     * <pre>
     * localeLookupList(Locale("fr", "CA", "xxx"))
     *   = [Locale("fr", "CA", "xxx"), Locale("fr", "CA"), Locale("fr")]
     * </pre>
     *
     * @param locale  the locale to start from
     * @return the unmodifiable list of Locale objects, 0 being locale, not null
     */
    public static List<Locale> localeLookupList(final Locale locale) {
        // TODO
        return null;
    }

    /**
     * Obtains the list of locales to search through when performing
     * a locale search.
     *
     * <pre>
     * localeLookupList(Locale("fr", "CA", "xxx"), Locale("en"))
     *   = [Locale("fr", "CA", "xxx"), Locale("fr", "CA"), Locale("fr"), Locale("en"]
     * </pre>
     *
     * <p>The result list begins with the most specific locale, then the
     * next more general and so on, finishing with the default locale.
     * The list will never contain the same locale twice.</p>
     *
     * @param locale  the locale to start from, null returns empty list
     * @param defaultLocale  the default locale to use if no other is found
     * @return the unmodifiable list of Locale objects, 0 being locale, not null
     */
    public static List<Locale> localeLookupList(final Locale locale, final Locale defaultLocale) {
        // TODO
        return null;
    }

    /**
     * Tries to parse a Locale from the given String.
     * <p>
     * See {@Link Locale} for the format.
     * </p>
     *
     * @param str the String to parse as a Locale.
     * @return a Locale parsed from the given String.
     * @throws IllegalArgumentException if the given String can not be parsed.
     * @see Locale
     */
    private static Locale parseLocale(final String str) {
        if (isISO639LanguageCode(str)) {
            return new Locale(str);
        }
        final int limit = 3;
        final char separator = str.indexOf(UNDERSCORE) != -1 ? UNDERSCORE : DASH;
        final String[] segments = str.split(String.valueOf(separator), 3);
        final String language = segments[0];
        if (segments.length == 2) {
            final String country = segments[1];
            if (isISO639LanguageCode(language) && isISO3166CountryCode(country) || isNumericAreaCode(country)) {
                return new Locale(language, country);
            }
        } else if (segments.length == limit) {
            final String country = segments[1];
            final String variant = segments[2];
            if (isISO639LanguageCode(language) && (country.isEmpty() || isISO3166CountryCode(country) || isNumericAreaCode(country)) && !variant.isEmpty()) {
                return new Locale(language, country, variant);
            }
        }
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    /**
     * Returns the given locale if non-{@code null}, otherwise {@link Locale#getDefault()}.
     *
     * @param locale a locale or {@code null}.
     * @return the given locale if non-{@code null}, otherwise {@link Locale#getDefault()}.
     * @since 3.12.0
     */
    public static Locale toLocale(final Locale locale) {
        // TODO
        return null;
    }

    /**
     * Converts a String to a Locale.
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.</p>
     *
     * <pre>
     *   LocaleUtils.toLocale("")           = new Locale("", "")
     *   LocaleUtils.toLocale("en")         = new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en-GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_001")     = new Locale("en", "001")
     *   LocaleUtils.toLocale("en_GB_xxx")  = new Locale("en", "GB", "xxx")   (#)
     * </pre>
     *
     * <p>(#) The behavior of the JDK variant constructor changed between JDK1.3 and JDK1.4.
     * In JDK1.3, the constructor upper cases the variant, in JDK1.4, it doesn't.
     * Thus, the result from getVariant() may vary depending on your JDK.</p>
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore or a dash.
     * The length must be correct.
     * </p>
     *
     * @param str  the locale String to convert, null returns null
     * @return a Locale, null if null input
     * @throws IllegalArgumentException if the string is an invalid format
     * @see Locale#forLanguageTag(String)
     */
    public static Locale toLocale(final String str) {
        // TODO
        return null;
    }

    /**
     * {@link LocaleUtils} instances should NOT be constructed in standard programming.
     * Instead, the class should be used as {@code LocaleUtils.toLocale("en_GB");}.
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     *
     * @deprecated TODO Make private in 4.0.
     */
    @Deprecated
    public LocaleUtils() {
        // TODO
    }
}
