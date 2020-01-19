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
package de.derklaro.requestbuilder.result;

import de.derklaro.requestbuilder.RequestBuilder;
import de.derklaro.requestbuilder.common.Validate;
import de.derklaro.requestbuilder.result.stream.StreamType;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collection;

/**
 * This class represents any connection to a web host created by {@link RequestBuilder#fireAndForget()}.
 *
 * This class is used to wrap the result of a request. Let's say you are opening a connection to
 * {@code https://gist.github.com/yaotest/4064031} by using:
 * <pre>{@code
 * public static synchronized void main(String... args) {
 *     RequestResult result = RequestBuilder.newBuilder("https://gist.github.com/yaotest/4064031", null).fireAndForget();
 * }
 * }</pre>
 *
 * If you now want to the the full site output you have to check the result code first:
 * <pre>{@code
 * if (result.getStatusCode() == 200) {
 *     // success
 *     return;
 * }
 *
 * // error
 * }</pre>
 *
 * or to simplify it:
 * <pre>{@code
 * if (!result.hasFailed()) {
 *     // success
 *     return;
 * }
 *
 * // error
 * }</pre>
 *
 * With this information you can either get the correct input stream of the connection using:
 * <pre>{@code
 * if (!result.hasFailed()) {
 *     InputStream stream = result.getStream(StreamType.DEFAULT); // default stream
 *     return;
 * }
 *
 * InputStream stream = result.getStream(StreamType.ERROR); // error stream
 * }</pre>
 *
 * Simplify this by using the chooser:
 * <pre>{@code
 * InputStream stream = result.getStream(StreamType.CHOOSE); // default or error stream
 * }</pre>
 *
 * The other way to get the result as string is to use the implemented methods:
 * <pre>{@code
 * if (!result.hasFailed()) {
 *     String resultMessage = result.getSuccessResultAsString(); // success
 *     return;
 * }
 *
 * String errorMessage = result.getErrorResultAsString(); // error
 * }</pre>
 *
 * or simplified use:
 * <pre>{@code
 * String resultMessage = result.getResultAsString(); // error or success
 * }</pre>
 *
 * This class is extremely useful to prevent a lot of code coming up by the time and prevents messy
 * classes full connection code.
 *
 * @see RequestBuilder#fireAndForget()
 * @see DefaultRequestResult
 *
 * @since RB 1.0
 * @version RB 1.1
 * @author derklaro
 */
public interface RequestResult extends AutoCloseable {

    static RequestResult create(@Nonnull HttpURLConnection httpURLConnection, @Nonnull Collection<String> body) {
        Validate.notNull(httpURLConnection, "Pleas provide a non-null connection");
        Validate.notNull(body, "Body cannot be null");

        return new DefaultRequestResult(httpURLConnection, body);
    }

    /**
     * Returns the needed {@link InputStream} of the given {@link StreamType}.
     * This will not work on successful connection when using {@link StreamType#ERROR} and not on
     * failed connection ({@link #getStatusCode()} != {@code 200}) when using {@link StreamType#DEFAULT}.
     * For more safety in programs use {@link StreamType#CHOOSE}.
     *
     * @param streamType The stream type which should get used when choosing the stream.
     * @return The input stream of the given type or on the type given by the input stream when using {@link StreamType#CHOOSE}
     * @throws RuntimeException if the connection is not connected or already closed
     */
    @Nonnull
    InputStream getStream(@Nonnull StreamType streamType);

    /**
     * Returns the output stream of the connection if the connection is marked for output using
     * {@link RequestBuilder#enableOutput()}.
     *
     * @return The {@link OutputStream} of the connection to the host
     * @throws RuntimeException if the connection is not connected or already closed
     */
    @Nonnull
    OutputStream getOutputStream();

    /**
     * @return If the connection is still connected
     */
    boolean isConnected();

    /**
     * @return Checks if the request to the web server succeeded
     */
    boolean hasFailed();

    /**
     * Reads the input stream of the connection and returns the result as string.
     *
     * @see #hasFailed()
     * @see #isConnected()
     *
     * @return The string created from the default input stream of the connection to the host.
     * @throws RuntimeException if the connection is not connected, already closed or the request wasn't successful
     */
    @Nonnull
    String getSuccessResultAsString();

    /**
     * Reads the error input stream of the connection and returns the result as string.
     *
     * @see #hasFailed()
     * @see #isConnected()
     *
     * @return The string created from the error input stream of the connection to the host.
     * @throws RuntimeException if the connection is not connected, already closed or the request was successful
     */
    @Nonnull
    String getErrorResultAsString();

    /**
     * Returns either the success result or the error result.
     *
     * @see #hasFailed()
     * @see #isConnected()
     * @see #getSuccessResultAsString()
     * @see #getErrorResultAsString()
     *
     * @return The string created from the error or default input stream of the connection to the host.
     * @throws RuntimeException if the connection is not connected or already closed
     */
    @Nonnull
    String getResultAsString();

    /**
     * @return The status code of the connection or {@code -1} if the connection is not connected or already closed
     */
    int getStatusCode();
}
