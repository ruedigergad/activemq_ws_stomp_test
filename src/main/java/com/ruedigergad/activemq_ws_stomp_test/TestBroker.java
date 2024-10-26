package com.ruedigergad.activemq_ws_stomp_test;

import java.net.URI;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.transport.TransportFactorySupport;
import org.apache.activemq.transport.TransportServer;

public class TestBroker {

	public static final String WS_ADDRESS = "ws://127.0.0.1:1701";

	private BrokerService brokerService;

	public TestBroker() throws Exception {
		brokerService = new BrokerService();
		brokerService.setPersistent(false);
		brokerService.setUseJmx(false);
		brokerService.setStartAsync(false);

		URI uri = new URI(WS_ADDRESS);
		TransportServer transportServer = TransportFactorySupport.bind(brokerService, uri);
		brokerService.addConnector(transportServer);
	}

	public void start() throws Exception {
		brokerService.start();
		brokerService.waitUntilStarted();
	}

}
