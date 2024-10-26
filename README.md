# About

This is a small example of a problem I came across when trying to use STOMP over WebSockets with ActiveMQ 6.x.

# How to Run

```
mvn compile exec:java -Dexec.mainClass="com.ruedigergad.activemq_ws_stomp_test.App"
```

# Analysis

## First Idea

I think the problem is that somehow, there is no factory for upgrading the protocol to websocket on the server side.

Below is an excerpt with a bit of context:

```
[qtp1661882384-126] DEBUG org.eclipse.jetty.server.AsyncContentProducer - reopening AsyncContentProducer@37686927[r=null,t=null,i=null,error=false,c=HttpChannelOverHttp@465a52b7{s=HttpChannelState@754ebdf8{s=IDLE rs=BLOCKING os=OPEN is=IDLE awp=false se=false i=true al=0},r=1,c=false/false,a=IDLE,uri=null,age=8}]
[qtp1661882384-126] DEBUG org.eclipse.jetty.server.HttpChannel - REQUEST for / on HttpChannelOverHttp@465a52b7{s=HttpChannelState@754ebdf8{s=IDLE rs=BLOCKING os=OPEN is=IDLE awp=false se=false i=true al=0},r=1,c=false/false,a=IDLE,uri=http://127.0.0.1:33444/,age=8}
GET / HTTP/1.1
Accept-Encoding: gzip
User-Agent: Jetty/11.0.22
Host: 127.0.0.1:33444
Sec-WebSocket-Version: 13
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: y8/5wzKUle0jv9CbHDJsAA==
Pragma: no-cache
Cache-Control: no-cache


[qtp1661882384-126] DEBUG org.eclipse.jetty.server.HttpChannelOverHttp - upgrade HttpChannelOverHttp@465a52b7{s=HttpChannelState@754ebdf8{s=IDLE rs=BLOCKING os=OPEN is=IDLE awp=false se=false i=true al=0},r=1,c=false/false,a=IDLE,uri=http://127.0.0.1:33444/,age=8} Upgrade: websocket
[qtp1661882384-126] DEBUG org.eclipse.jetty.server.HttpChannelOverHttp - No factory for Upgrade: websocket in ServerConnector@514f2020{HTTP/1.1, (http/1.1)}{localhost:33444}
[qtp1661882384-126] DEBUG org.eclipse.jetty.server.HttpConnection - HttpConnection@655e38b1::SocketChannelEndPoint@64a7cff9[{l=/127.0.0.1:33444,r=/127.0.0.1:43170,OPEN,fill=-,flush=-,to=13/30000}{io=0/0,kio=0,kro=1}]->[HttpConnection@655e38b1[p=HttpParser{s=CONTENT,0 of -1},g=HttpGenerator@600dd472{s=START}]=>HttpChannelOverHttp@465a52b7{s=HttpChannelState@754ebdf8{s=IDLE rs=BLOCKING os=OPEN is=IDLE awp=false se=false i=true al=0},r=1,c=false/false,a=IDLE,uri=http://127.0.0.1:33444/,age=10}] parsed true HttpParser{s=CONTENT,0 of -1}
```

And the key message, I think, is:


```
[qtp1661882384-126] DEBUG org.eclipse.jetty.server.HttpChannelOverHttp - No factory for Upgrade: websocket in ServerConnector@514f2020{HTTP/1.1, (http/1.1)}{localhost:33444}
```

## Second Idea

Maybe the problem is not related to the first idea.
One reason the first idea came up was the point of the upgrade not working.
Related to this is also one of the reported exceptions:

```
[qtp101478235-29] DEBUG org.eclipse.jetty.websocket.core.server.internal.AbstractHandshaker - negotiation RFC6455Negotiation@2dc8a103{uri=/,oe=null,op=null}
[qtp101478235-29] TRACE org.apache.activemq.thread.TaskRunnerFactory - Execute[ActiveMQ BrokerService[localhost] Task] runnable: org.apache.activemq.broker.TransportConnector$1$1@70049d6a
[qtp101478235-29] TRACE org.apache.activemq.thread.TaskRunnerFactory - Created thread[ActiveMQ BrokerService[localhost] Task-1]: Thread[#44,ActiveMQ BrokerService[localhost] Task-1,5,main]
[qtp101478235-29] WARN org.eclipse.jetty.server.HttpChannel - /
org.eclipse.jetty.websocket.core.exception.WebSocketException: not upgraded: selected a protocol not present in offered protocols
	at org.eclipse.jetty.websocket.core.server.internal.AbstractHandshaker.upgradeRequest(AbstractHandshaker.java:105)
	at org.eclipse.jetty.websocket.core.server.internal.HandshakerSelector.upgradeRequest(HandshakerSelector.java:39)
	at org.eclipse.jetty.websocket.core.server.WebSocketMappings.upgrade(WebSocketMappings.java:241)
	at org.eclipse.jetty.websocket.server.JettyWebSocketServlet.service(JettyWebSocketServlet.java:181)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:587)
	at org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:764)
	at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:529)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:131)
	at org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:598)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:122)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:223)
	at org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1381)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:176)
	at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:484)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:174)
	at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1303)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:129)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:122)
	at org.eclipse.jetty.server.Server.handle(Server.java:563)
	at org.eclipse.jetty.server.HttpChannel$RequestDispatchable.dispatch(HttpChannel.java:1598)
	at org.eclipse.jetty.server.HttpChannel.dispatch(HttpChannel.java:753)
	at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:501)
	at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:287)
	at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:314)
	at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:100)
	at org.eclipse.jetty.io.SelectableChannelEndPoint$1.run(SelectableChannelEndPoint.java:53)
	at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:969)
	at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.doRunJob(QueuedThreadPool.java:1194)
	at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1149)
	at java.base/java.lang.Thread.run(Thread.java:1570)
```

However, another point that could cause the problem is in:
org.apache.activemq.transport.ws.jetty11.WSServlet.class

It looks like the requested sub-protocols are empty, which may be another reason why no upgrade of the connection can be done.

It seems in org.eclipse.jetty.websocket.core.server.WebSocketNegotiation the HTTP header fields are checked.
It looks like a SEC_WEBSOCKET_SUBPROTOCOL("Sec-WebSocket-Protocol"), field is expected, which seems not to be in the client request.


# References

I have come accross: https://stackoverflow.com/questions/63814992/using-wars-in-jetty-embedded-websocket-getting-no-factory-for-upgrade
But I think this applies to an earlier Jetty version.
I still tried to apply this by patching the WSTransportServer class that comes with ActiveMQ but it didnt work.

# Disclaimer

Maybe I am on a wrong track or overlook something important.
If anyone has any clue regarding how to get this working, please let me know.

