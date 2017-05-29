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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.SocketAddress;

import org.junit.Test;

/**
 * Unit tests for class {@link MockSocket org.elasticsearch.mocksocket.MockSocket}.
 *
 * @author Michael Hausegger, hausegger.michael@googlemail.com
 * @date 29.05.2017
 * @see MockSocket
 **/

public class MockSocketTest {


	@Test
	public void testCreatesMockSocketTakingProxy() {

		Proxy proxy = Proxy.NO_PROXY;
		MockSocket mockSocket = new MockSocket(proxy);

		assertEquals(Proxy.Type.DIRECT, proxy.type());
		assertEquals("DIRECT", proxy.toString());

		assertFalse(mockSocket.isInputShutdown());
		assertFalse(mockSocket.isBound());

		assertEquals("Socket[unconnected]", mockSocket.toString());
		assertFalse(mockSocket.isOutputShutdown());

		assertFalse(mockSocket.isClosed());
		assertFalse(mockSocket.isConnected());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFailsToCreateMockSocketTaking4ArgumentsThrowsIllegalArgumentException() throws IOException {

		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
		MockSocket mockSocket = new MockSocket(inetAddress, (-1), inetAddress, (-1));
	}


	@Test(expected = ConnectException.class)
	public void testFailsToCreateMockSocketTakingThreeArgumentsThrowsConnectExceptionOne() throws IOException {

		MockSocket mockSocket = new MockSocket((String) null, 668);
	}


	@Test
	public void testGetLocalSocketAddress() {

		MockSocket mockSocket = new MockSocket();
		SocketAddress socketAddress = mockSocket.getLocalSocketAddress();

		assertNull(socketAddress);
		assertFalse(mockSocket.isConnected());

		assertEquals("Socket[unconnected]", mockSocket.toString());
		assertFalse(mockSocket.isInputShutdown());

		assertFalse(mockSocket.isBound());
		assertFalse(mockSocket.isClosed());

		assertFalse(mockSocket.isOutputShutdown());
	}


	@Test(expected = ConnectException.class)
	public void testFailsToCreateMockSocketTakingThreeArgumentsThrowsConnectExceptionTwo() throws IOException {

		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
		MockSocket mockSocket = new MockSocket(inetAddress, 0);
	}


	@Test
	public void testGetInetAddress() {

		MockSocket mockSocket = new MockSocket();
		InetAddress inetAddress = mockSocket.getInetAddress();

		assertNull(inetAddress);
		assertFalse(mockSocket.isBound());

		assertEquals("Socket[unconnected]", mockSocket.toString());
		assertFalse(mockSocket.isInputShutdown());

		assertFalse(mockSocket.isConnected());
		assertFalse(mockSocket.isOutputShutdown());

		assertFalse(mockSocket.isClosed());
	}
}