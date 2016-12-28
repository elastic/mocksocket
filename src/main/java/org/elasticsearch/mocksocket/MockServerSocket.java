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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

public class MockServerSocket extends ServerSocket {

    public MockServerSocket() throws IOException {
        super();
    }

    public MockServerSocket(int port) throws IOException {
        super(port);
    }

    public MockServerSocket(int port, int backlog) throws IOException {
        super(port, backlog);
    }

    public MockServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        super(port, backlog, bindAddr);
    }

    @Override
    public Socket accept() throws IOException {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Socket>) MockServerSocket.super::accept);
        } catch (PrivilegedActionException e) {
            throw (IOException) e.getCause();
        }
    }

    @Override
    public void bind(SocketAddress endpoint) throws IOException {
        try {
            AccessController.doPrivileged((PrivilegedExceptionAction<Void>) () -> {
                MockServerSocket.super.bind(endpoint);
                return null;
            });
        } catch (PrivilegedActionException e) {
            throw (IOException) e.getCause();
        }
    }

    @Override
    public InetAddress getInetAddress() {
        return AccessController.doPrivileged((PrivilegedAction<InetAddress>) super::getInetAddress);
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return AccessController.doPrivileged((PrivilegedAction<SocketAddress>) super::getLocalSocketAddress);
    }

}
