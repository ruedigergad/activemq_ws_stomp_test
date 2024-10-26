package com.ruedigergad.activemq_ws_stomp_test;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class TestClient {

	private WebSocketClient wsClient;
	
	public TestClient() {
		wsClient = new WebSocketClient();
	}
	
	public void connect() throws Exception {
		wsClient.start();
		JettyWebSocketClient jwsc = new JettyWebSocketClient(wsClient);
		WebSocketStompClient wssc = new WebSocketStompClient(jwsc);
		wssc.start();

		WebSocketHttpHeaders wsHttpHeaders = new WebSocketHttpHeaders();
		wsHttpHeaders.add(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL.asString(), "stomp");

		wssc.connect(TestBroker.WS_ADDRESS, wsHttpHeaders, new StompSessionHandlerAdapter() {});
	}
}
