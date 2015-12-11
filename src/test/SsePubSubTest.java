import com.lz.sse.AirSsePubSubResource;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Created by lizhaoz on 2015/12/10.
 */

public class SsePubSubTest extends JerseyTest {
    private static final String ROOT_PATH = "pubsub";
    private static final int READ_TIMEOUT = 30000;
    @Override
    protected Application configure() {
        return new ResourceConfig(AirSsePubSubResource.class, SseFeature.class);
    }
//    @Override
//    protected void configureClient(ClientConfig config) {
//        AtupClientUtil.buildeApacheConfig(config);
//        config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
//        config.register(SseFeature.class);
//    }
@Test
public void testEventSource() throws InterruptedException, URISyntaxException {
    final int testCount = 10;
    final String messagePrefix = "pubsub-";
    final CountDownLatch latch = new CountDownLatch(testCount);
    //在eventSource实例化过程中，会从这个端点向服务器发出请求
    final EventSource eventSource = new EventSource(target().path(ROOT_PATH)) {
        private int i;

        @Override
        public void onEvent(InboundEvent inboundEvent) {
            try {
                System.out.println("Received: " + inboundEvent.getId() + ":" + inboundEvent.getName() + ":" + new String(inboundEvent.getRawData()));
                Assert.assertEquals(messagePrefix + i++, inboundEvent.readData(String.class));
                latch.countDown();
            } catch (ProcessingException e) {
                e.printStackTrace();
            }
        }
    };
    for (int i = 0; i < testCount; i++) {
        target().path(ROOT_PATH).request().post(Entity.text(messagePrefix + i));
    }
    try {
        latch.await();
    } finally {
        eventSource.close();
    }
}

}
