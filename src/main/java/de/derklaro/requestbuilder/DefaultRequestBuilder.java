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
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This class is the default implementation of the {@link RequestBuilder}.
 *
 * @author derklaro
 * @see RequestBuilder#newBuilder(String, Proxy)
 * @since RB 1.0
 */
class DefaultRequestBuilder implements RequestBuilder {

    DefaultRequestBuilder(@Nonnull String url, @Nullable Proxy proxy) {
        Validate.notNull(url, null);

        this.url = url;
        this.proxy = proxy;
    }

    private final String url;

    private final Proxy proxy;

    private RequestMethod requestMethod = RequestMethod.GET;

    private final Collection<String> body = new ArrayList<>();

    private final Map<String, String> headers = new ConcurrentHashMap<>();

    private final Collection<HttpCookie> cookies = new ArrayList<>();

    private MimeType mimeType;

    private MimeType accept;

    private Integer fixedOutputStreamLength;

    private boolean followRedirects = Boolean.FALSE;

    private boolean useCaches = Boolean.TRUE;

    private boolean useOutput = Boolean.FALSE;

    private boolean useInput = Boolean.TRUE;

    private boolean useUserInteraction = Boolean.FALSE;

    private Integer readTimeout;

    private Integer connectTimeout;

    @Nonnull
    @Override
    public RequestBuilder setRequestMethod(@Nonnull RequestMethod requestMethod) {
        Validate.notNull(requestMethod, "Invalid request method %s", requestMethod);

        this.requestMethod = requestMethod;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder addBody(@Nonnull String key, @Nonnull String value) {
        Validate.notNull(key, "Invalid key for body %s", key);
        Validate.notNull(value, "Invalid value for header %s", value);

        body.add(key + "=" + value);
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder addBody(@Nonnull String body) {
        Validate.notNull(body, "Invalid body string %s", body);

        this.body.add(body);
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder addHeader(@Nonnull String key, @Nonnull String value) {
        Validate.notNull(key, "Invalid key for header %s", key);
        Validate.notNull(value, "Invalid value for header %s", value);

        headers.put(key, value);
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder setMimeType(@Nonnull MimeType mimeType) {
        Validate.notNull(mimeType, "Invalid mime type %s", mimeType);

        this.mimeType = mimeType;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder accepts(@Nonnull MimeType mimeType) {
        Validate.notNull(mimeType, "Invalid accept-mime-type %s", mimeType);

        this.accept = mimeType;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder setFixedOutputStreamLength(int length) {
        Validate.checkArgument(length > 0, "The fixed stream length must be greater than 0 (%d)", length);

        this.fixedOutputStreamLength = length;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder enableRedirectFollow() {
        this.followRedirects = Boolean.TRUE;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder disableCaches() {
        this.useCaches = Boolean.FALSE;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder enableOutput() {
        this.useOutput = Boolean.TRUE;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder disableInput() {
        this.useInput = Boolean.FALSE;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder enableUserInteraction() {
        this.useUserInteraction = Boolean.TRUE;
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder setConnectTimeout(int timeout, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeout, "Invalid timeout unit %s", timeout);
        Validate.checkArgument(timeout > 0, "Invalid timeout time %d", timeout);

        this.connectTimeout = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder setReadTimeOut(int timeout, @Nonnull TimeUnit timeUnit) {
        Validate.notNull(timeout, "Invalid timeout %s", timeout);
        Validate.checkArgument(timeout > 0, "Invalid timeout time %d", timeout);

        this.readTimeout = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder addCookie(@Nonnull String name, @Nonnull String value) {
        this.cookies.add(new HttpCookie(name, value));
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder addCookies(@Nonnull HttpCookie... cookies) {
        this.cookies.addAll(Arrays.asList(cookies));
        return this;
    }

    @Nonnull
    @Override
    public RequestBuilder addCookies(@Nonnull Collection<HttpCookie> cookies) {
        this.cookies.addAll(cookies);
        return this;
    }

    @Nonnull
    @Override
    public RequestResult fireAndForget() throws IOException {
        final HttpURLConnection httpURLConnection = choose();
        Validate.notNull(httpURLConnection, "Could not choose connection type or cannot open connection");

        this.headers.forEach(httpURLConnection::setRequestProperty);

        httpURLConnection.setRequestMethod(this.requestMethod.name());

        httpURLConnection.setDoInput(this.useInput);
        httpURLConnection.setDoOutput(this.useOutput);
        httpURLConnection.setUseCaches(this.useCaches);
        httpURLConnection.setAllowUserInteraction(this.useUserInteraction);
        httpURLConnection.setInstanceFollowRedirects(this.followRedirects);

        if (fixedOutputStreamLength != null && fixedOutputStreamLength > 0) {
            httpURLConnection.setFixedLengthStreamingMode(fixedOutputStreamLength);
        }

        if (readTimeout != null && readTimeout > 0) {
            httpURLConnection.setReadTimeout(readTimeout);
        }

        if (connectTimeout != null && connectTimeout > 0) {
            httpURLConnection.setConnectTimeout(connectTimeout);
        }

        if (this.mimeType != null) {
            httpURLConnection.setRequestProperty("Content-Type", mimeType.getValue());
        }

        if (this.accept != null) {
            httpURLConnection.setRequestProperty("Accept", accept.getValue());
        }

        if (this.cookies.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (HttpCookie cookie : this.cookies) {
                stringBuilder.append(cookie.getName()).append("=").append(cookie.getValue()).append(",");
            }

            httpURLConnection.setRequestProperty("Cookie", stringBuilder.substring(0, stringBuilder.length() - 1));
        }

        httpURLConnection.connect();
        return RequestResult.create(httpURLConnection, body);
    }

    @Nonnull
    @Override
    public Future<RequestResult> fireAndForgetAsynchronously() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return this.fireAndForget();
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Nullable
    private HttpURLConnection choose() {
        try {
            if (this.proxy == null) {
                return (HttpURLConnection) new URL(this.url).openConnection();
            }

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
