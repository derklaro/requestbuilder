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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the default implementation of a {@link RequestResult}.
 *
 * @author derklaro
 * @see RequestBuilder#fireAndForget()
 * @since RB 1.0
 */
class DefaultRequestResult implements RequestResult {

    DefaultRequestResult(@Nonnull HttpURLConnection httpURLConnection, @Nonnull Collection<String> body) {
        Validate.notNull(httpURLConnection, "Pleas provide a non-null connection");
        Validate.notNull(body, "Body cannot be null");

        this.httpURLConnection = httpURLConnection;
        body.forEach(s -> {
            try {
                httpURLConnection.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
                httpURLConnection.getOutputStream().flush();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private final HttpURLConnection httpURLConnection;

    @Nonnull
    @Override
    public InputStream getStream(@Nonnull StreamType streamType) {
        Validate.notNull(streamType, "Cannot use null stream type");

        try {
            if (streamType.equals(StreamType.DEFAULT)) {
                return httpURLConnection.getInputStream();
            }

            if (streamType.equals(StreamType.CHOOSE)) {
                if (getStatusCode() == 200) {
                    return httpURLConnection.getInputStream();
                }

                return httpURLConnection.getErrorStream();
            }

            return httpURLConnection.getErrorStream();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Nonnull
    @Override
    public OutputStream getOutputStream() {
        try {
            return httpURLConnection.getOutputStream();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            int code = httpURLConnection.getResponseCode();
            return code != -1;
        } catch (final Throwable throwable) {
            return false;
        }
    }

    @Override
    public boolean hasFailed() {
        return getStatusCode() != 200;
    }

    @Nonnull
    @Override
    public String getSuccessResultAsString() {
        try {
            return readStream(httpURLConnection.getInputStream());
        } catch (final Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Nonnull
    @Override
    public String getErrorResultAsString() {
        try {
            return readStream(httpURLConnection.getErrorStream());
        } catch (final Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Nonnull
    @Override
    public String getResultAsString() {
        if (getStatusCode() == 200) {
            return getSuccessResultAsString();
        }

        return getErrorResultAsString();
    }

    @Nonnull
    @Override
    public Collection<HttpCookie> getCookies() {
        return this.getCookies("Set-Cookie");
    }

    @Nonnull
    @Override
    public Collection<HttpCookie> getCookies(@Nonnull String cookiesHeader) {
        String headerField = this.httpURLConnection.getHeaderField(cookiesHeader);
        if (headerField == null) {
            return new ArrayList<>();
        }

        return HttpCookie.parse(headerField);
    }

    @Override
    public int getStatusCode() {
        try {
            return httpURLConnection.getResponseCode();
        } catch (final Throwable throwable) {
            return -1;
        }
    }

    private String readStream(InputStream stream) {
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
        httpURLConnection.disconnect();
    }
}
