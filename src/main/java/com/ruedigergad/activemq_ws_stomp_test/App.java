package com.ruedigergad.activemq_ws_stomp_test;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    	
        System.out.println( "Creating broker..." );
        TestBroker broker = new TestBroker();
        System.out.println("Starting broker...");
        broker.start();
        System.out.println("Broker started.");
        
        System.out.println("Creating client...");
        TestClient client = new TestClient();
        System.out.println("Connecting client...");
        client.connect();
    }
}
