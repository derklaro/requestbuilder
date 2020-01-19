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
package de.derklaro.requestbuilder;

import de.derklaro.requestbuilder.common.Validate;
import de.derklaro.requestbuilder.method.RequestMethod;
import de.derklaro.requestbuilder.result.RequestResult;
import de.derklaro.requestbuilder.types.MimeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * This class represents any {@link RequestBuilder} which can be accessed by any method which returns
 * a {@link RequestBuilder}.
 * <br/>
 * With this class class you basically can create a connection to every web server you need and can
 * requested a parsed result from the input streams using plain java.
 * <br/>
 * By default a implementation of this class is the {@link DefaultRequestBuilder} which can be accessed
 * using {@link RequestBuilder#newBuilder(String, Proxy)}.
 *
 * To create a new request builder you can use:
 *
 * <pre>{@code
 * public static synchronized void main(String... args) {
 *     RequestBuilder builder = RequestBuilder.newBuilder("https://google.de", null);
 * }
 * }</pre>
 *
 * Now you can set your information you want to send to the host, for example a header:
 *
 * <pre>{@code builder.addHeader("Content-Length", "45");}</pre>
 *
 * or you can add a body to the builder using:
 *
 * <pre>{@code builder.addBody("value", "test");}</pre>
 *
 * Also you can enable user interaction
 * <pre>{@code builder.enableUserInteraction();}</pre>
 *
 * enable output
 * <pre>{@code builder.enableOutput();}</pre>
 *
 * or disable input
 * <pre>{@code builder.disableInput();}</pre>.
 *
 * To finally fire the request and get the result of the request use:
 * <pre>{@code
 * try (RequestResult result = builder.fireAndForget()) {
 *     // do something
 * } catch(final IOException ex) {
 *     throw new RuntimeException(ex);
 * }
 * }</pre>
 *
 * @see RequestBuilder#newBuilder(String, Proxy)
 * @see DefaultRequestBuilder
 *
 * @since RB 1.0
 * @version RB 1.1
 * @author derklaro
 */
public interface RequestBuilder extends AutoCloseable {

    /**
     * Creates a new request builder instance
     *
     * @see RequestBuilder#newBuilder(String, Proxy)
     *
     * @param url The target web url which you want to open
     * @return A new request builder instance with the given target url
     * @throws IllegalArgumentException If the given url is null
     */
    static RequestBuilder newBuilder(@Nonnull String url) {
        return newBuilder(url, null);
    }

    /**
     * Creates a new request builder instance
     *
     * @param url The target web url which you want to open
     * @param proxy The proxy through which the connection should go
     * @return A new request builder instance with the given target url
     * @throws IllegalArgumentException If the given url is null
     */
    @Nonnull
    static RequestBuilder newBuilder(@Nonnull String url, @Nullable Proxy proxy) {
        Validate.notNull(url, "Invalid url %s", url);

        return new DefaultRequestBuilder(url, proxy);
    }

    /**
     * Sets the request method of the connection
     *
     * @param requestMethod The request method which should be used
     * @return The current instance of the class
     * @throws IllegalArgumentException If the given method is null
     */
    @Nonnull
    RequestBuilder setRequestMethod(@Nonnull RequestMethod requestMethod);

    /**
     * Adds a body to the connection. Ensure you've enabled output by using {@link #enableOutput()}
     *
     * @param key The key of the body parameter
     * @param value The value of the body parameter
     * @return The current instance of the class
     * @throws IllegalArgumentException If the key or value is null
     */
    @Nonnull
    RequestBuilder addBody(@Nonnull String key, @Nonnull String value);

    /**
     * Adds a body to the connection. Ensure you've enabled output by using {@link #enableOutput()}
     *
     * @param body The complete body as one string
     * @return The current instance of the class
     * @throws IllegalArgumentException If the body string is null
     */
    @Nonnull
    RequestBuilder addBody(@Nonnull String body);

    /**
     * Adds a header to the connection
     *
     * @param key The key of the header
     * @param value The value of the header
     * @return The current instance of the class
     * @throws IllegalArgumentException If the key or value is null
     */
    @Nonnull
    RequestBuilder addHeader(@Nonnull String key, @Nonnull String value);

    /**
     * Sets the mime type of the connection which should get sent
     *
     * @see de.derklaro.requestbuilder.types.MimeTypes#getMimeType(String)
     * @param mimeType The mime type which should be used
     * @return The current instance of the class
     * @throws IllegalArgumentException If the mime type is null
     */
    @Nonnull
    RequestBuilder setMimeType(@Nonnull MimeType mimeType);

    /**
     * Sets the mime type which is accepted by the connection
     *
     * @see de.derklaro.requestbuilder.types.MimeTypes#getMimeType(String)
     *
     * @since RB 1.1
     * @param mimeType The mime type which should be accepted
     * @return The current instance of the class
     * @throws IllegalArgumentException If the mime type is null
     */
    @Nonnull
    RequestBuilder accepts(@Nonnull MimeType mimeType);

    /**
     * Sets the fixed stream length of the outgoing connection
     *
     * @param length The fixed stream length of the connection
     * @return The current instance of the class
     * @throws IllegalArgumentException if the length is {@code < 0}
     */
    @Nonnull
    RequestBuilder setFixedOutputStreamLength(int length);

    /**
     * Enables the follow of the redirects from the web server
     *
     * @return The current instance of the class
     */
    @Nonnull
    RequestBuilder enableRedirectFollow();

    /**
     * Disables the use of caches when connection and reading the data from the web server
     *
     * @return The current instance of this class
     */
    @Nonnull
    RequestBuilder disableCaches();

    /**
     * Enables the output sent to the remote host with the current connection
     *
     * @return The current instance oif this class
     */
    @Nonnull
    RequestBuilder enableOutput();

    /**
     * Disables the option that the remote host can send a result to the connection input
     *
     * @return The current instance of this class
     */
    @Nonnull
    RequestBuilder disableInput();

    /**
     * Enables the possibility for the user to interact with the connection
     *
     * @return The current instance of this class
     */
    @Nonnull
    RequestBuilder enableUserInteraction();

    /**
     * Sets the timeout time for the connection
     *
     * @param timeout The time which should be wait until the connection timout
     * @param timeUnit The time unit of the connect timout
     * @return The current instance of this class
     * @throws IllegalArgumentException If either the TimeUnit is null or timeout is {@code < 0}
     */
    @Nonnull
    RequestBuilder setConnectTimeout(int timeout, @Nonnull TimeUnit timeUnit);

    /**
     * Sets the read timeout of the connection (only needed if input is enabled)
     *
     * @param timeout The time which should be wait until the read timout
     * @param timeUnit The time unit of the read timout
     * @return The current instance of this class
     * @throws IllegalArgumentException If either the TimeUnit is null or timeout is {@code < 0}
     */
    @Nonnull
    RequestBuilder setReadTimeOut(int timeout, @Nonnull TimeUnit timeUnit);

    /**
     * Fires the request and waits for the result
     *
     * @return The result of the the request which got fired
     * @throws IOException If an connection error, read error etc. occurs
     */
    @Nonnull
    RequestResult fireAndForget() throws IOException;
}
