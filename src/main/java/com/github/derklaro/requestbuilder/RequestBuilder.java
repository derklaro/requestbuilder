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
package com.github.derklaro.requestbuilder;

import com.github.derklaro.requestbuilder.common.Validate;
import com.github.derklaro.requestbuilder.method.RequestMethod;
import com.github.derklaro.requestbuilder.result.RequestResult;
import com.github.derklaro.requestbuilder.types.MimeType;
import com.github.derklaro.requestbuilder.types.MimeTypes;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.Proxy;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This class represents any {@link RequestBuilder} which can be accessed by any method which returns a {@link
 * RequestBuilder}.
 * <br>
 * With this class you basically can create a connection to every web server you need and can request a parsed result
 * from the input streams using plain java.
 * <br>
 * By default a implementation of this class is the {@link DefaultRequestBuilder} which can be accessed using {@link
 * RequestBuilder#newBuilder(String, Proxy)}.
 * <p>
 * To create a new request builder you can use:
 *
 * <pre>{@code
 * public static synchronized void main(String... args) {
 *     RequestBuilder builder = RequestBuilder.newBuilder("https://google.de", null);
 * }
 * }</pre>
 * <p>
 * Now you can set your information you want to send to the host, for example a header:
 *
 * <pre>{@code builder.addHeader("Content-Length", "45");}</pre>
 * <p>
 * or you can add a body to the builder using:
 *
 * <pre>{@code builder.addBody("value", "test");}</pre>
 * <p>
 * Also you can enable user interaction
 * <pre>{@code builder.enableUserInteraction();}</pre>
 * <p>
 * enable output
 * <pre>{@code builder.enableOutput();}</pre>
 * <p>
 * or disable input
 * <pre>{@code builder.disableInput();}</pre>.
 * <p>
 * To finally fire the request and get the result of the request use:
 * <pre>{@code
 * try (RequestResult result = builder.fireAndForget()) {
 *     // do something
 * } catch(final IOException ex) {
 *     throw new RuntimeException(ex);
 * }
 * }</pre>
 *
 * @author derklaro
 * @version RB 1.0.1
 * @see RequestBuilder#newBuilder(String, Proxy)
 * @see DefaultRequestBuilder
 * @since RB 1.0.0
 */
public interface RequestBuilder extends AutoCloseable {

  /**
   * Creates a new request builder instance
   *
   * @param url The target web url which you want to open
   * @return A new request builder instance with the given target url
   * @throws IllegalArgumentException If the given url is null
   * @see RequestBuilder#newBuilder(String, Proxy)
   */
  @NotNull
  static RequestBuilder newBuilder(@NotNull String url) {
    return newBuilder(url, Proxy.NO_PROXY);
  }

  /**
   * Creates a new request builder instance
   *
   * @param url   The target web url which you want to open
   * @param proxy The proxy through which the connection should go
   * @return A new request builder instance with the given target url
   * @throws IllegalArgumentException If the given url is null
   */
  @NotNull
  static RequestBuilder newBuilder(@NotNull String url, @NotNull Proxy proxy) {
    Validate.notNull(url, "url");
    Validate.notNull(proxy, "proxy");

    return new DefaultRequestBuilder(url, proxy);
  }

  /**
   * Sets the request method of the connection
   *
   * @param requestMethod The request method which should be used
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException If the given method is null
   */
  @NotNull
  RequestBuilder requestMethod(@NotNull RequestMethod requestMethod);

  /**
   * Adds a body to the connection. This method calls {@link #enableOutput()}.
   *
   * @param body The complete body as one string
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException If the body string is null
   */
  @NotNull
  RequestBuilder addBody(@NotNull String body);

  /**
   * Adds a body to the connection. This method calls {@link #enableOutput()}.
   *
   * @param body The complete body as a byte array
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException If the body string is null
   */
  @NotNull
  RequestBuilder addBody(byte[] body);

  /**
   * Adds a header to the connection
   *
   * @param key   The key of the header
   * @param value The value of the header
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException If the key or value is null
   */
  @NotNull
  RequestBuilder addHeader(@NotNull String key, @NotNull String value);

  /**
   * Sets the mime type of the connection which should get sent
   *
   * @param mimeType The mime type which should be used
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException If the mime type is null
   * @see MimeTypes#getMimeType(String)
   */
  @NotNull
  RequestBuilder mimeType(@NotNull MimeType mimeType);

  /**
   * Sets the mime type which is accepted by the connection
   *
   * @param mimeType The mime type which should be accepted
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException If the mime type is null
   * @see MimeTypes#getMimeType(String)
   * @since RB 1.0.1
   */
  @NotNull
  RequestBuilder accepts(@NotNull MimeType mimeType);

  /**
   * Sets the fixed stream length of the outgoing connection
   *
   * @param length The fixed stream length of the connection
   * @return The current instance of the class, for chaining
   * @throws IllegalArgumentException if the length is {@code < 0}
   */
  @NotNull
  RequestBuilder fixedOutputStreamLength(@Range(from = 0, to = Integer.MAX_VALUE) int length);

  /**
   * Enables the follow of the redirects from the web server
   *
   * @return The current instance of the class, for chaining
   */
  @NotNull
  RequestBuilder enableRedirectFollow();

  /**
   * Disables the use of caches when connection and reading the data from the web server
   *
   * @return The current instance of this class
   */
  @NotNull
  RequestBuilder disableCaches();

  /**
   * Enables the output sent to the remote host with the current connection
   *
   * @return The current instance oif this class
   */
  @NotNull
  RequestBuilder enableOutput();

  /**
   * Disables the option that the remote host can send a result to the connection input
   *
   * @return The current instance of this class
   */
  @NotNull
  RequestBuilder disableInput();

  /**
   * Enables the possibility for the user to interact with the connection
   *
   * @return The current instance of this class
   */
  @NotNull
  RequestBuilder enableUserInteraction();

  /**
   * Sets the timeout time for the connection
   *
   * @param timeout  The time which should be wait until the connection timout
   * @param timeUnit The time unit of the connect timout
   * @return The current instance of this class
   * @throws IllegalArgumentException If either the TimeUnit is null or timeout is {@code < 0}
   */
  @NotNull
  RequestBuilder connectTimeout(@Range(from = 0, to = Integer.MAX_VALUE) int timeout, @NotNull TimeUnit timeUnit);

  /**
   * Sets the read timeout of the connection (only needed if input is enabled)
   *
   * @param timeout  The time which should be wait until the read timout
   * @param timeUnit The time unit of the read timout
   * @return The current instance of this class
   * @throws IllegalArgumentException If either the TimeUnit is null or timeout is {@code < 0}
   */
  @NotNull
  RequestBuilder readTimeout(int timeout, @NotNull TimeUnit timeUnit);

  /**
   * Adds a cookie to the request header
   *
   * @param name  The name of the cookie
   * @param value The value which the cookie should has
   * @return The current instance of this class
   * @since RB 1.0.2
   */
  @NotNull
  RequestBuilder addCookie(@NotNull String name, @NotNull String value);

  /**
   * Adds a bunch of cookies to the request. Note that only the key and value are used when firing the request.
   *
   * @param cookies The cookies which should get added to the request
   * @return The current instance of this class
   * @since RB 1.0.2
   */
  @NotNull
  RequestBuilder addCookies(@NotNull HttpCookie... cookies);

  /**
   * Adds a bunch of cookies to the request. Note that only the key and value are used when firing the request.
   *
   * @param cookies The cookies which should get added to the request
   * @return The current instance of this class
   * @since RB 1.0.2
   */
  @NotNull
  RequestBuilder addCookies(@NotNull Collection<HttpCookie> cookies);

  /**
   * Fires the request and waits for the result
   *
   * @return The result of the request which got fired
   * @throws IOException If an connection error, read error etc. occurs
   */
  @NotNull
  RequestResult fire() throws IOException;

  /**
   * Fires the request without reading the result.
   *
   * @throws IOException If an connection error, read error etc. occurs
   */
  void fireAndForget() throws IOException;

  /**
   * Fires the request async and returns the future of it
   *
   * @return The future of the result, filled after the request was successful
   */
  @NotNull
  CompletableFuture<RequestResult> fireAndForgetAsynchronously();
}
