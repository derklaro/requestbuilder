/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019-2020 Pasqual K. <https://derklaro.de>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.derklaro.requestbuilder.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Static methods that helps to check if a constructor or method is used correctly.
 * If the condition cannot be met it will throw an unchecked exception to signal the user that the
 * method is used incorrectly. It's helping developers, too:
 * <p>
 * Instead of:
 * <pre>{@code
 * public static void println(@Nonnull String text) {
 *     if (text == null) {
 *         throw new IllegalArgumentException(String.format("Invalid usage of parameter %s", "text"));
 *     }
 *
 *     // do something
 * }
 * }</pre>
 * <p>
 * you can simply use
 *
 * <pre>{@code
 * public static void println(@Nonnull String text) {
 *     Validate.notNull(text, "Invalid usage of parameter %s", "text");
 *     // do something
 * }
 * }</pre>
 * <p>
 * Basically it will flag the use of illegal parameters, for example:
 *
 * <pre>{@code
 * public static synchronized void main(String... args) {
 *     println(null);
 * }
 * }</pre>
 * <p>
 * will cause the throw of the exception to flag the invalid usage:
 * <p>Exception in thread "main" java.lang.IllegalArgumentException: Invalid usage of parameter text</p>
 *
 * @author derklaro
 * @since RB 1.0.1
 */
public final class Validate {

    /* Ensures that the class cannot get initialized */
    private Validate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Ensures that the given object is {@code non-null}.
     *
     * @param check        The object which should get checked if it's null.
     * @param message      The message which should get printed in the console or simply {@code null}
     *                     then the message is {@code 'null'}.
     * @param replacements The replacements for the formatted string.
     * @throws IllegalArgumentException If the given object is {@code null}.
     */
    public static void notNull(@Nullable Object check, @Nullable Object message, @Nonnull Object... replacements) {
        if (check == null) {
            throw new IllegalArgumentException(String.format(String.valueOf(message), replacements));
        }
    }

    /**
     * Checks if the given argument is true
     *
     * @param check        The argument which should get checked
     * @param message      The message which should get printed in the console or simply {@code null}
     *                     then the message is {@code 'null'}.
     * @param replacements The replacements for the formatted string.
     * @throws IllegalArgumentException if the given argument is {@code false}
     */
    public static void checkArgument(boolean check, @Nullable Object message, @Nonnull Object... replacements) {
        if (!check) {
            throw new IllegalArgumentException(String.format(String.valueOf(message), replacements));
        }
    }
}
