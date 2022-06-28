
package chill.web;

import chill.util.Redis;
import com.google.gson.Gson;
import chill.env.ChillEnv;
import org.eclipse.jetty.server.session.AbstractSessionDataStore;
import org.eclipse.jetty.server.session.SessionData;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.stream.Collectors;

public class RedisSessionStore extends AbstractSessionDataStore {

    private static final String SESSION_PREFIX = "jetty:sessions:";

    // TODO - move to factor/wrapper
    Gson gson = new Gson();

    @Override
    public void doStore(String id, SessionData data, long lastSaveTime) {
        Redis.exec(r -> r.set(SESSION_PREFIX + id, gson.toJson(data)));
    }

    @Override
    public SessionData doLoad(String id) {
        String session = Redis.eval(redis -> redis.get(SESSION_PREFIX + id));
        if (session != null) {
            return gson.fromJson(session, SessionData.class);
        } else {
            return null;
        }
    }

    @Override
    public Set<String> doGetExpired(Set<String> candidates) {
        return candidates.stream()
                .map(s -> Redis.eval( r -> r.get(s)))
                .map(data -> gson.fromJson(data, SessionData.class))
                .filter(sessionData -> sessionData.isExpiredAt(System.currentTimeMillis()))
                .map(SessionData::getId).collect(Collectors.toSet());
    }

    @Override
    public boolean isPassivating() {
        return false;
    }

    @Override
    public boolean exists(String id) {
        String session = Redis.eval(r -> r.get(SESSION_PREFIX + id));
        return session != null;
    }

    @Override
    public boolean delete(String id) {
        Redis.exec(r -> r.del(SESSION_PREFIX + id));
        return true;
    }
}