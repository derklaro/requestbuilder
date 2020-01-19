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
package de.derklaro.requestbuilder.method;

import de.derklaro.requestbuilder.result.RequestResult;

/**
 * Represents any request method which can be used in the {@link de.derklaro.requestbuilder.RequestBuilder}
 * when creating a request to a web host.
 *
 * Basically you can use this snippet to use this methods:
 *
 * <pre>{@code
 * public static synchronized void main(String... args) {
 *     RequestBuilder builder = RequestBuilder.newBuilder("https://google.de", null).setRequestMethod(RequestMethod.GET);
 * }
 * }</pre>
 *
 * The web server should handle the request method in the specified else it will throw a
 * <a href="https://docs.oracle.com/javaee/7/api/javax/ws/rs/BadRequestException.html">
 *     BadRequestException</a>.
 * Please check the {@link RequestResult#getStatusCode()} if it's a {@code 400}.
 *
 * @see RequestResult#getStatusCode()
 * @see de.derklaro.requestbuilder.RequestBuilder#setRequestMethod(RequestMethod)
 *
 * @since RB 1.0
 * @author derklaro
 */
public enum RequestMethod {

    /**
     * The GET method requests a representation of the specified resource. Requests using GET should
     * only retrieve data.
     */
    GET,

    /**
     * The POST method is used to submit an entity to the specified resource, often causing a change
     * in state or side effects on the server.
     */
    POST,

    /**
     * The HEAD method asks for a response identical to that of a GET request, but without the
     * response body.
     */
    HEAD,

    /**
     * The OPTIONS method is used to describe the communication options for the target resource.
     */
    OPTIONS,

    /**
     * The PUT method replaces all current representations of the target resource with the request
     * payload.
     */
    PUT,

    /**
     * The DELETE method deletes the specified resource.
     */
    DELETE,

    /**
     * The TRACE method performs a message loop-back test along the path to the target resource.
     */
    TRACE
}
