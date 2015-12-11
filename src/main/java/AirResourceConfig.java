import com.lz.sse.AirSseBroadcastResource;
import com.lz.sse.AirSsePubSubResource;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by lizhaoz on 2015/12/10.
 */
@ApplicationPath("/v1/*")
public class AirResourceConfig extends ResourceConfig{
    public AirResourceConfig() {
        //SseFeature(在2.8版本之后会自动探测)，用以标识该服务具备处理SSE的特征
        //AirSsePubSubResource是发布-订阅资源类
        //AirSseBroadcastResource是广播资源类
        super(SseFeature.class,AirSsePubSubResource.class, AirSseBroadcastResource.class);
    }
}
