/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.mocksocket;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Executor;

public class MockHttpServer {

    public static HttpsServer createHttps(InetSocketAddress addr, int backlog) throws IOException {
        HttpsServer httpsServer = HttpsServer.create(addr, backlog);
        return new DelegatingHttpsServer(httpsServer);
    }

    public static HttpServer createHttp(InetSocketAddress addr, int backlog) throws IOException {
        HttpServer httpServer = HttpServer.create(addr, backlog);
        return new DelegatingHttpServer(httpServer);
    }

    private static void privilegeBind(HttpServer httpsServer, InetSocketAddress inetSocketAddress, int i) throws IOException {
        try {
            AccessController.doPrivileged((PrivilegedExceptionAction<Void>) () -> {
                httpsServer.bind(inetSocketAddress, i);
                return null;
            });
        } catch (PrivilegedActionException e) {
            throw (IOException) e.getCause();
        }
    }

    private static void privilegeStart(HttpServer httpsServer) {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            httpsServer.start();
            return null;
        });
    }

    private static class DelegatingHttpsServer extends HttpsServer {

        private final HttpsServer httpsServer;

        private DelegatingHttpsServer(HttpsServer httpsServer) {
            this.httpsServer = httpsServer;
        }

        @Override
        public void setHttpsConfigurator(HttpsConfigurator httpsConfigurator) {
            httpsServer.setHttpsConfigurator(httpsConfigurator);
        }

        @Override
        public HttpsConfigurator getHttpsConfigurator() {
            return httpsServer.getHttpsConfigurator();
        }

        @Override
        public void bind(InetSocketAddress inetSocketAddress, int i) throws IOException {
            privilegeBind(httpsServer, inetSocketAddress, i);
        }

        @Override
        public void start() {
            privilegeStart(httpsServer);
        }

        @Override
        public void setExecutor(Executor executor) {
            httpsServer.setExecutor(executor);
        }

        @Override
        public Executor getExecutor() {
            return httpsServer.getExecutor();
        }

        @Override
        public void stop(int i) {
            httpsServer.stop(i);
        }

        @Override
        public HttpContext createContext(String s, HttpHandler httpHandler) {
            return httpsServer.createContext(s, httpHandler);
        }

        @Override
        public HttpContext createContext(String s) {
            return httpsServer.createContext(s);
        }

        @Override
        public void removeContext(String s) throws IllegalArgumentException {
            httpsServer.removeContext(s);
        }

        @Override
        public void removeContext(HttpContext httpContext) {
            httpsServer.removeContext(httpContext);
        }

        @Override
        public InetSocketAddress getAddress() {
            return httpsServer.getAddress();
        }
    }

    private static class DelegatingHttpServer extends HttpServer {

        private final HttpServer httpServer;

        private DelegatingHttpServer(HttpServer httpServer) {
            this.httpServer = httpServer;
        }

        @Override
        public void bind(InetSocketAddress inetSocketAddress, int i) throws IOException {
            privilegeBind(httpServer, inetSocketAddress, i);
        }

        @Override
        public void start() {
            privilegeStart(httpServer);
        }

        @Override
        public void setExecutor(Executor executor) {
            httpServer.setExecutor(executor);
        }

        @Override
        public Executor getExecutor() {
            return httpServer.getExecutor();
        }

        @Override
        public void stop(int i) {
            httpServer.stop(i);
        }

        @Override
        public HttpContext createContext(String s, HttpHandler httpHandler) {
            return httpServer.createContext(s, httpHandler);
        }

        @Override
        public HttpContext createContext(String s) {
            return httpServer.createContext(s);
        }

        @Override
        public void removeContext(String s) throws IllegalArgumentException {
            httpServer.removeContext(s);
        }

        @Override
        public void removeContext(HttpContext httpContext) {
            httpServer.removeContext(httpContext);
        }

        @Override
        public InetSocketAddress getAddress() {
            return httpServer.getAddress();
        }
    }

}
