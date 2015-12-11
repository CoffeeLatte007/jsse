package com.lz.sse;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.message.internal.TracingLogger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by lizhaoz on 2015/12/10.
 */
@Path("pubsub")
public class AirSsePubSubResource {
    //建立eventOutput信道用于写入OutboundEvent(出站事件)
    private static EventOutput eventOutput=new EventOutput();
    @GET//提供SSE事件输出通道的资源方法
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput publishMessage() throws IOException{

        return  eventOutput;
    }
    @POST
    public Boolean saveMessage(String message) throws IOException {
        eventOutput.write(new OutboundEvent.Builder().
                id(System.nanoTime() + "").
                name("post message").
                data(String.class, message).
                build());
        return true;
    }
}
