package backend;
import java.io.IOException;
import java.net.InetSocketAddress;

import redis.clients.jedis.Jedis;
import net.spy.memcached.MemcachedClient;

import backend.BackendType;
import backend.SerializeUtil;

public class Storage {
    private Jedis jedis = null;
    private MemcachedClient memcached = null;
    private BackendType type;
    private int expire = 3600;
    
    public Storage(BackendType type) throws IOException {
    	this.type = type;
    	if (type == BackendType.Redis) {
    		this.jedis = new Jedis("localhost");
    	} else if (type == BackendType.Memcached) {
    		this.memcached = new MemcachedClient(
    			    new InetSocketAddress("localhost", 11211));
    	} else {
    		return;
    	}
    }
    
    /*
     * return 0 on success, 1 on fail
     * */
    public int set(String key, Object obj) {
    	switch (this.type) {
    		case Redis:
    			this.jedis.set(key.getBytes(), SerializeUtil.serialize(obj));
    			return 0;
    		case Memcached:
    			this.memcached.set(key, this.expire, obj);
    			return 0;
    		default:
    			return 1;
    	}
    }
    
    public Object get(String key) {
    	switch (this.type) {
			case Redis:
				return SerializeUtil.unserialize(this.jedis.get(key.getBytes()));
			case Memcached:
				return this.memcached.get(key);
			default:
				return null;
    	}
    }
}