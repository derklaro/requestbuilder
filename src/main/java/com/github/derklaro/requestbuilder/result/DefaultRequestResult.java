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
package com.github.derklaro.requestbuilder.result;

import com.github.derklaro.requestbuilder.RequestBuilder;
import com.github.derklaro.requestbuilder.common.Validate;
import com.github.derklaro.requestbuilder.result.stream.StreamType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

/**
 * This is the default implementation of a {@link RequestResult}.
 *
 * @author derklaro
 * @see RequestBuilder#fireAndForget()
 * @since RB 1.0.0
 */
public class DefaultRequestResult implements RequestResult {

    private final HttpURLConnection httpURLConnection;

    protected DefaultRequestResult(@NotNull HttpURLConnection httpURLConnection, @Nullable Collection<byte[]> body) {
        Validate.notNull(httpURLConnection, "The connection may not be null");

        this.httpURLConnection = httpURLConnection;
        if (body != null) {
            body.forEach(bytes -> {
                try {
                    httpURLConnection.getOutputStream().write(bytes);
                    httpURLConnection.getOutputStream().flush();
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    @NotNull
    @Override
    public InputStream getStream(@NotNull StreamType streamType) {
        Validate.notNull(streamType, "Cannot use null stream type");

        try {
            if (streamType.equals(StreamType.DEFAULT)) {
                return this.httpURLConnection.getInputStream();
            }

            if (streamType.equals(StreamType.CHOOSE)) {
                if (this.getStatusCode() == 200) {
                    return this.httpURLConnection.getInputStream();
                }

                return this.httpURLConnection.getErrorStream();
            }

            return this.httpURLConnection.getErrorStream();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NotNull
    @Override
    public OutputStream getOutputStream() {
        try {
            return this.httpURLConnection.getOutputStream();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return this.httpURLConnection.getResponseCode() != -1;
        } catch (IOException exception) {
            return false;
        }
    }

    @Override
    public boolean hasFailed() {
        return this.getStatusCode() != 200;
    }

    @NotNull
    @Override
    public String getSuccessResultAsString() {
        try {
            return this.readStream(this.httpURLConnection.getInputStream());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @NotNull
    @Override
    public String getErrorResultAsString() {
        try {
            return this.readStream(this.httpURLConnection.getErrorStream());
        } catch (final Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @NotNull
    @Override
    public String getResultAsString() {
        if (this.getStatusCode() == 200) {
            return this.getSuccessResultAsString();
        }

        return this.getErrorResultAsString();
    }

    @NotNull
    @Override
    public Collection<HttpCookie> getCookies() {
        return this.getCookies("Set-Cookie");
    }

    @NotNull
    @Override
    public Collection<HttpCookie> getCookies(@NotNull String cookiesHeader) {
        String headerField = this.httpURLConnection.getHeaderField(cookiesHeader);
        if (headerField == null) {
            return Collections.emptyList();
        }

        return HttpCookie.parse(headerField);
    }

    @Override
    public int getStatusCode() {
        try {
            return this.httpURLConnection.getResponseCode();
        } catch (final Throwable throwable) {
            return -1;
        }
    }

    @NotNull
    private String readStream(@NotNull InputStream stream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            final char[] chars = new char[4 * 1024];
            int len;
            while ((len = inputStreamReader.read(chars)) >= 0) {
                stringBuilder.append(chars, 0, len);
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return stringBuilder.toString();
    }

    @Override
    public void close() {
        this.httpURLConnection.disconnect();
    }
}
