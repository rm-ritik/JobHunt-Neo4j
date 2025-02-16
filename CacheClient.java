
import net.spy.memcached.MemcachedClient;
import java.io.IOException;
import java.net.InetSocketAddress;

public class CacheClient {
    private MemcachedClient client;

    public CacheClient(String hostname, int port) throws IOException {
        this.client = new MemcachedClient(new InetSocketAddress(hostname, port));
    }

    public void set(String key, int expireSeconds, Object value) {
        client.set(key, expireSeconds, value);
    }

    public Object get(String key) {
        return client.get(key);
    }

    public boolean exists(String key) {
        return get(key) != null;
    }
}

