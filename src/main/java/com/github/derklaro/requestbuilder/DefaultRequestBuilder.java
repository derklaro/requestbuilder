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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class is the default implementation of the {@link RequestBuilder}.
 *
 * @author derklaro
 * @see RequestBuilder#newBuilder(String, Proxy)
 * @since RB 1.0.0
 */
public class DefaultRequestBuilder implements RequestBuilder {

    private final String url;
    private final Proxy proxy;

    private Collection<byte[]> bodies;
    private Collection<HttpCookie> cookies;

    private Map<String, String> headers;

    private MimeType mimeType;
    private MimeType accept;

    private Integer fixedOutputStreamLength;
    private Integer readTimeout;
    private Integer connectTimeout;

    private RequestMethod requestMethod = RequestMethod.GET;

    private boolean followRedirects = Boolean.FALSE;
    private boolean useCaches = Boolean.TRUE;
    private boolean useOutput = Boolean.FALSE;
    private boolean useInput = Boolean.TRUE;
    private boolean useUserInteraction = Boolean.FALSE;

    protected DefaultRequestBuilder(@NotNull String url, @NotNull Proxy proxy) {
        Validate.notNull(url, null);

        this.url = url;
        this.proxy = proxy;
    }

    @NotNull
    @Override
    public RequestBuilder requestMethod(@NotNull RequestMethod requestMethod) {
        Validate.notNull(requestMethod, "Invalid request method %s", requestMethod);

        this.requestMethod = requestMethod;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder addBody(@NotNull String key, @NotNull String value) {
        Validate.notNull(key, "Invalid key for body %s", key);
        Validate.notNull(value, "Invalid value for header %s", value);

        return this.addBody(key + "=" + value);
    }

    @NotNull
    @Override
    public RequestBuilder addBody(@NotNull String body) {
        Validate.notNull(body, "Invalid body string %s", body);

        return this.addBody(body.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public @NotNull RequestBuilder addBody(@NotNull byte[] body) {
        Validate.notNull(body, "The body of a connection may not be null");

        if (this.bodies == null) {
            this.enableOutput();
            this.bodies = new ArrayList<>();
        }

        this.bodies.add(body);
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder addHeader(@NotNull String key, @NotNull String value) {
        Validate.notNull(key, "Invalid key for header %s", key);
        Validate.notNull(value, "Invalid value for header %s", value);

        if (this.headers == null) {
            this.headers = new HashMap<>();
        }

        this.headers.put(key, value);
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder mimeType(@NotNull MimeType mimeType) {
        Validate.notNull(mimeType, "Invalid mime type %s", mimeType);

        this.mimeType = mimeType;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder accepts(@NotNull MimeType mimeType) {
        Validate.notNull(mimeType, "Invalid accept-mime-type %s", mimeType);

        this.accept = mimeType;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder fixedOutputStreamLength(int length) {
        Validate.checkArgument(length > 0, "The fixed stream length must be greater than 0 (%d)", length);

        this.fixedOutputStreamLength = length;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder enableRedirectFollow() {
        this.followRedirects = Boolean.TRUE;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder disableCaches() {
        this.useCaches = Boolean.FALSE;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder enableOutput() {
        this.useOutput = Boolean.TRUE;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder disableInput() {
        this.useInput = Boolean.FALSE;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder enableUserInteraction() {
        this.useUserInteraction = Boolean.TRUE;
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder connectTimeout(int timeout, @NotNull TimeUnit timeUnit) {
        Validate.notNull(timeout, "Invalid timeout unit %s", timeout);
        Validate.checkArgument(timeout > 0, "Invalid timeout time %d", timeout);

        this.connectTimeout = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder readTimeout(int timeout, @NotNull TimeUnit timeUnit) {
        Validate.notNull(timeout, "Invalid timeout %s", timeout);
        Validate.checkArgument(timeout > 0, "Invalid timeout time %d", timeout);

        this.readTimeout = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder addCookie(@NotNull String name, @NotNull String value) {
        Validate.notNull(name, "Cookie name can not be null");
        Validate.notNull(value, "Cookie value can not be null");

        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }

        this.cookies.add(new HttpCookie(name, value));
        return this;
    }

    @NotNull
    @Override
    public RequestBuilder addCookies(@NotNull HttpCookie... cookies) {
        Validate.notNull(cookies, "Cookies can not be null");
        return this.addCookies(Arrays.asList(cookies));
    }

    @NotNull
    @Override
    public RequestBuilder addCookies(@NotNull Collection<HttpCookie> cookies) {
        Validate.notNull(cookies, "Cookies can not be null");

        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }

        this.cookies.addAll(cookies);
        return this;
    }

    @NotNull
    @Override
    public RequestResult fireAndForget() throws IOException {
        final HttpURLConnection httpURLConnection = this.createHttpUrlConnection();
        Validate.notNull(httpURLConnection, "Could not choose connection type or cannot open connection");

        httpURLConnection.setRequestMethod(this.requestMethod.name());

        httpURLConnection.setDoInput(this.useInput);
        httpURLConnection.setDoOutput(this.useOutput);
        httpURLConnection.setUseCaches(this.useCaches);
        httpURLConnection.setAllowUserInteraction(this.useUserInteraction);
        httpURLConnection.setInstanceFollowRedirects(this.followRedirects);

        if (this.headers != null) {
            this.headers.forEach(httpURLConnection::setRequestProperty);
        }

        if (this.fixedOutputStreamLength != null && this.fixedOutputStreamLength > 0) {
            httpURLConnection.setFixedLengthStreamingMode(this.fixedOutputStreamLength);
        }

        if (this.readTimeout != null && this.readTimeout > 0) {
            httpURLConnection.setReadTimeout(this.readTimeout);
        }

        if (this.connectTimeout != null && this.connectTimeout > 0) {
            httpURLConnection.setConnectTimeout(this.connectTimeout);
        }

        if (this.mimeType != null) {
            httpURLConnection.setRequestProperty("Content-Type", this.mimeType.getValue());
        }

        if (this.accept != null) {
            httpURLConnection.setRequestProperty("Accept", this.accept.getValue());
        }

        if (this.cookies != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (HttpCookie cookie : this.cookies) {
                stringBuilder.append(cookie.getName()).append("=").append(cookie.getValue()).append(",");
            }

            httpURLConnection.setRequestProperty("Cookie", stringBuilder.substring(0, stringBuilder.length() - 1));
        }

        httpURLConnection.connect();
        return RequestResult.createDefault(httpURLConnection, this.bodies);
    }

    @NotNull
    @Override
    public CompletableFuture<RequestResult> fireAndForgetAsynchronously() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.fireAndForget();
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Nullable
    private HttpURLConnection createHttpUrlConnection() {
        try {
            return (HttpURLConnection) new URL(this.url).openConnection(this.proxy);
        } catch (final IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
    }
}
