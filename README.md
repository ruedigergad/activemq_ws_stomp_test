# About

This is a small example of a problem I came across when trying to use STOMP over WebSockets with ActiveMQ 6.x.

# How to Run

```
mvn compile exec:java -Dexec.mainClass="com.ruedigergad.activemq_ws_stomp_test.App"
```

# Analysis

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

# References

I have come accross: https://stackoverflow.com/questions/63814992/using-wars-in-jetty-embedded-websocket-getting-no-factory-for-upgrade
But I think this applies to an earlier Jetty version.
I still tried to apply this by patching the WSTransportServer class that comes with ActiveMQ but it didnt work.

# Disclaimer

Maybe I am on a wrong track or overlook something important.
If anyone has any clue regarding how to get this working, please let me know.

